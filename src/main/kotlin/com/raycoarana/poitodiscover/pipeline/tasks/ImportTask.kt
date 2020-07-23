package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.*
import com.raycoarana.poitodiscover.pipeline.Task
import com.raycoarana.poitodiscover.pipeline.importer.ImporterFactory
import javax.inject.Inject

class ImportTask @Inject constructor(
        private val importerFactory: ImporterFactory
) : Task {
    override fun execute(context: Context) {
        val poiList = ArrayList<Poi>()
        val inputFolder = createTempFolder()
        context.inputFiles.forEach {
            val sourceType = getSourceType(it)
            it.decompressTo(inputFolder)
            poiList.addAll(importerFactory.get(sourceType).execute(inputFolder))
            inputFolder.deleteChilds()
        }
        context.rawPois = poiList
    }

    private fun getSourceType(it: File): SourceType =
        when (it.name) {
            GARMIN_SOURCE -> SourceType.GARMIN_SPEED
            LUFOP_GARMIN_SOURCE -> SourceType.LUFOP_GARMIN_SOURCE
            MERCADONA_GARMIN_SOURCE -> SourceType.MERCADONA_GARMIN
            else -> throw Exception("Unsupported source file")
        }

    companion object {
        const val GARMIN_SOURCE = "garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip"
        const val LUFOP_GARMIN_SOURCE = "Lufop-Zones-de-danger-EU-CSV.zip"
        const val MERCADONA_GARMIN_SOURCE = "Mercadona.zip"
    }
}
