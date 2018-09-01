package com.raycoarana.poitodiscover.pipeline.importer

import com.opencsv.CSVReaderBuilder
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.Poi
import com.raycoarana.poitodiscover.domain.PoiType
import com.raycoarana.poitodiscover.pipeline.Task
import org.slf4j.Logger
import javax.inject.Inject

/**
 * Read POIs from Garmin Speed 2xx-12xx-13xx-14xx-2xxx-3xxx
 */
class ImportFromGarminSpeed @Inject constructor(private val logger: Logger) : Task {
    override fun execute(context: Context) {
        val csvFiles = Folder(context.inputFolder, GARMIN_CONTENT_FOLDER)
                .listFiles()
                .filter { it.extension.toLowerCase() == CSV_EXTENSION }

        logger.info("Detected ${csvFiles.size} POI files")

        context.rawPois = csvFiles.map {csvFile ->
            val categoryName = csvFile.nameWithoutExtension
            val poiType = categoryName.toPoiType()
            val pois = CSVReaderBuilder(csvFile.utf8Reader())
                    .build()
                    .readAll()
                    .map { csvLine ->
                        val (longitude, latitude, description) = csvLine
                        Poi(latitude, longitude, description, poiType)
                    }

            logger.info("  ${pois.size} of type $categoryName ($poiType)")

            pois
        }.flatMap { it }
    }

    private fun String.toPoiType(): PoiType =
            when (this) {
                "R_BBS_APR" -> PoiType.ResidentialArea
                "R_BBS_camu_30" -> PoiType.Hidden
                "R_BBS_camu_40" -> PoiType.Hidden
                "R_BBS_camu_50" -> PoiType.Hidden
                "R_BBS_camu_60" -> PoiType.Hidden
                "R_BBS_camu_70" -> PoiType.Hidden
                "R_BBS_camu_80" -> PoiType.Hidden
                "R_BBS_camu_90" -> PoiType.Hidden
                "R_BBS_camu_100" -> PoiType.Hidden
                "R_BBS_camu_110" -> PoiType.Hidden
                "R_BBS_camu_120" -> PoiType.Hidden
                "R_BBS_camu_variable" -> PoiType.Hidden
                "R_BBS_fijos_20" -> PoiType.Fixed
                "R_BBS_fijos_30" -> PoiType.Fixed
                "R_BBS_fijos_40" -> PoiType.Fixed
                "R_BBS_fijos_50" -> PoiType.Fixed
                "R_BBS_fijos_60" -> PoiType.Fixed
                "R_BBS_fijos_70" -> PoiType.Fixed
                "R_BBS_fijos_80" -> PoiType.Fixed
                "R_BBS_fijos_90" -> PoiType.Fixed
                "R_BBS_fijos_100" -> PoiType.Fixed
                "R_BBS_fijos_110" -> PoiType.Fixed
                "R_BBS_fijos_120" -> PoiType.Fixed
                "R_BBS_fijos_variable" -> PoiType.Fixed
                "R_BBS_Foto" -> PoiType.Photo
                "R_BBS_semaforos" -> PoiType.Semaphores
                "R_BBS_tramo_final" -> PoiType.SectionFinal
                "R_BBS_tramo_inicio" -> PoiType.SectionStart
                "R_BBS_tunel" -> PoiType.Tunnel
                else -> PoiType.Unknown
            }

    companion object {
        const val CSV_EXTENSION = "csv"
        const val GARMIN_CONTENT_FOLDER = "garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores"
    }
}
