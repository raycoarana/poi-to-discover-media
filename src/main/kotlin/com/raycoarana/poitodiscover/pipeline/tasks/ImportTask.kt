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
            inputFolder.deleteChildren()
        }
        context.rawPois = poiList
    }

    private fun getSourceType(it: File): SourceType =
        when (it.name) {
            LUFOP_GARMIN_SOURCE -> SourceType.LUFOP_GARMIN_SOURCE
            else -> throw Exception("Unsupported source file: ${it.name}. Only LUFOP Garmin EU source is supported.")
        }

    companion object {
        const val LUFOP_GARMIN_SOURCE = "Lufop-Zones-de-danger-EU-CSV.zip"
    }
}
