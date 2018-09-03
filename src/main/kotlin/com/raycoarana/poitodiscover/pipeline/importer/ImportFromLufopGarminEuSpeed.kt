package com.raycoarana.poitodiscover.pipeline.importer

import com.opencsv.CSVReaderBuilder
import com.raycoarana.poitodiscover.core.contains
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.Poi
import com.raycoarana.poitodiscover.domain.PoiType
import org.slf4j.Logger
import javax.inject.Inject

class ImportFromLufopGarminEuSpeed @Inject constructor(private val logger: Logger) : Importer {
    override fun execute(inputFolder: Folder): List<Poi> {
        val csvFiles = inputFolder.listFiles()
                .filter { it.extension.toLowerCase() == CSV_EXTENSION }

        logger.info("Detected ${csvFiles.size} POI files")

        return csvFiles.map {csvFile ->
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
            when(this) {
                in Regex("^((?!ES).)*Mobile.+") -> PoiType.Hidden
                in Regex("^((?!ES).)*Fixe.+") -> PoiType.Fixed
                in Regex(".+ZoneFR\\d{2,3}") -> PoiType.Fixed
                in Regex("^((?!ES).)*FeuRouge.+") -> PoiType.Semaphores
                in Regex("^((?!ES).)*Tunnel.+") -> PoiType.Tunnel
                in Regex("^((?!ES).)*TronconDebut.+") -> PoiType.SectionStart
                in Regex("^((?!ES).)*TronconFin.+") -> PoiType.SectionEnd
                else -> PoiType.Unknown
            }

    companion object {
        const val CSV_EXTENSION = "csv"
    }
}
