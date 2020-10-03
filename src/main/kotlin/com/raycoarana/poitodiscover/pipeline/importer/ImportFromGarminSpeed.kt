package com.raycoarana.poitodiscover.pipeline.importer

import com.opencsv.CSVReaderBuilder
import com.raycoarana.poitodiscover.core.contains
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.Poi
import com.raycoarana.poitodiscover.domain.PoiType
import org.slf4j.Logger
import javax.inject.Inject

/**
 * Read POIs from Garmin Speed 2xx-12xx-13xx-14xx-2xxx-3xxx
 */
class ImportFromGarminSpeed @Inject constructor(private val logger: Logger) : Importer {
    override fun execute(inputFolder: Folder): List<Poi> {
        val csvFiles = Folder(inputFolder, GARMIN_CONTENT_FOLDER)
                .listFilesRecursively()
                .filter { it.extension.toLowerCase() == CSV_EXTENSION }

        logger.info("Detected ${csvFiles.size} POI files")

        return csvFiles.map {csvFile ->
            val categoryName = csvFile.nameWithoutExtension
            val poiType = categoryName.toPoiType()
            CSVReaderBuilder(csvFile.utf8Reader())
                    .build()
                    .readAll()
                    .map { csvLine ->
                        val (longitude, latitude, description) = csvLine
                        Poi(latitude, longitude, description, poiType)
                    }
                    .also { logger.info("  ${it.size} of type $categoryName ($poiType)") }
        }.flatten()
    }

    private fun String.toPoiType(): PoiType =
            when(this) {
                "R_BBS_APR" -> PoiType.ResidentialArea
                in Regex("R_BBS_camu_.+") -> PoiType.Hidden
                in Regex("R_BBS_fijos_.+") -> PoiType.Fixed
                in Regex("R_BBS_fijos_.+") -> PoiType.Fixed
                "R_BBS_Foto" -> PoiType.Photo
                "R_BBS_semaforos" -> PoiType.Semaphores
                "R_BBS_tramo_final" -> PoiType.SectionEnd
                "R_BBS_tramo_inicio" -> PoiType.SectionStart
                "R_BBS_tunel" -> PoiType.Tunnel
                else -> PoiType.Unknown
            }

    companion object {
        const val CSV_EXTENSION = "csv"
        const val GARMIN_CONTENT_FOLDER = "garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores"
    }
}
