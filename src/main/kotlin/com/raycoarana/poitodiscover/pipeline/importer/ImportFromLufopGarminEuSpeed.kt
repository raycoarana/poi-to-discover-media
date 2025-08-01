package com.raycoarana.poitodiscover.pipeline.importer

import com.opencsv.CSVReaderBuilder
import com.raycoarana.poitodiscover.core.contains
import com.raycoarana.poitodiscover.domain.ImportConfig
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.Poi
import com.raycoarana.poitodiscover.domain.PoiType
import org.slf4j.Logger
import javax.inject.Inject

class ImportFromLufopGarminEuSpeed @Inject constructor(
    private val logger: Logger,
    private val config: ImportConfig
) : Importer {
    override fun execute(inputFolder: Folder): List<Poi> {
        val csvFiles = inputFolder.listFilesRecursively()
                .filter { it.extension.toLowerCase() == CSV_EXTENSION }

        logger.info("Detected ${csvFiles.size} POI files")

        return csvFiles.map { csvFile ->
            val categoryName = csvFile.nameWithoutExtension
            
            // Apply country filtering to determine if this file should be included
            val shouldIncludeFile = when {
                config.countryFilter != null -> {
                    // Include only POIs that match the specified country
                    categoryName.contains(config.countryFilter, ignoreCase = true)
                }
                config.excludeCountries.isNotEmpty() -> {
                    // Exclude POIs from specified countries
                    !config.excludeCountries.any { countryCode -> 
                        categoryName.contains(countryCode, ignoreCase = true) 
                    }
                }
                else -> true // Include all if no filtering is configured
            }
            
            if (shouldIncludeFile) {
                val poiType = categoryName.toPoiType()
                CSVReaderBuilder(csvFile.utf8Reader())
                    .build()
                    .readAll()
                    .map { csvLine ->
                        val (longitude, latitude, description) = csvLine
                        Poi(latitude, longitude, description, poiType)
                    }
                    .also { logger.info("  ${it.size} of type $categoryName ($poiType)") }
            } else {
                logger.info("  Filtering out $categoryName due to country filter configuration")
                emptyList<Poi>()
            }
        }.flatten()
    }

    private fun String.toPoiType(): PoiType {
        return when(this) {
            in Regex(".*Mobile.*", RegexOption.IGNORE_CASE) -> PoiType.Hidden
            in Regex(".*Fixe.*", RegexOption.IGNORE_CASE) -> PoiType.Fixed
            in Regex(".+ZoneFR\\d{2,3}") -> PoiType.Fixed
            in Regex(".*FeuRouge.*", RegexOption.IGNORE_CASE) -> PoiType.Semaphores
            in Regex(".*Tunnel.*", RegexOption.IGNORE_CASE) -> PoiType.Tunnel
            in Regex(".*TronconDebut.*", RegexOption.IGNORE_CASE) -> PoiType.SectionStart
            in Regex(".*TronconFin.*", RegexOption.IGNORE_CASE) -> PoiType.SectionEnd
            else -> PoiType.Unknown
        }
    }

    companion object {
        const val CSV_EXTENSION = "csv"
    }
}
