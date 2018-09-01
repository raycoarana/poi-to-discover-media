package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.SourceType
import com.raycoarana.poitodiscover.domain.createTempFolder
import com.raycoarana.poitodiscover.pipeline.Task
import com.raycoarana.poitodiscover.pipeline.importer.ImporterFactory
import javax.inject.Inject

class ImportTask @Inject constructor(
        private val importerFactory: ImporterFactory
) : Task {
    override fun execute(context: Context) {
        val sourceType = when(context.inputFile.name) {
            GARMIN_SOURCE -> SourceType.GARMIN_SPEED
            else -> throw Exception("Unsupported source file")
        }

        context.inputFolder = createTempFolder()
        context.inputFile.decompressTo(context.inputFolder)
        importerFactory.get(sourceType).execute(context)
    }

    companion object {
        const val GARMIN_SOURCE = "garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip"
    }
}
