package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.pipeline.Task
import javax.inject.Inject

class GenerateMib2DeFolderStructureTask @Inject constructor() : Task {
    override fun execute(context: Context) {
        context.radarPoisFolder = Folder(context.outputFolder, context.nameWithoutSpaces)
        context.radarPoisFolder.mkdirs()

        context.mib2DeFolder = Folder(context.radarPoisFolder, "MIB2DE")
        context.mib2DeFolder.mkdirs()
    }
}
