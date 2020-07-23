package com.raycoarana.poitodiscover.pipeline.importer

import com.opencsv.CSVReaderBuilder
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.Poi
import com.raycoarana.poitodiscover.domain.PoiType
import org.slf4j.Logger
import javax.inject.Inject

/**
 * Read POIs from Garmin Speed 2xx-12xx-13xx-14xx-2xxx-3xxx
 */
class ImportFromMercadonaGarmin @Inject constructor(private val logger: Logger) : Importer {
    override fun execute(inputFolder: Folder): List<Poi> {
        val csvFiles = inputFolder.listFiles()
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
                "Mercadona" -> PoiType.Mercadona
                else -> PoiType.Unknown
            }

    companion object {
        const val CSV_EXTENSION = "csv"
    }
}
