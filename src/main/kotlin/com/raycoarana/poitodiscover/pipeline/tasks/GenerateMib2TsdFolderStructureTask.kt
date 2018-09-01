package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.pipeline.Task
import javax.inject.Inject

class GenerateMib2TsdFolderStructureTask @Inject constructor() : Task {
    override fun execute(context: Context) {
        context.radarPoisFolder = Folder(context.outputFolder, context.nameWithoutSpaces)
        context.radarPoisFolder.mkdirs()

        context.mib2TsdFolder = Folder(context.radarPoisFolder, "MIB2TSD")
        context.mib2TsdFolder.mkdirs()

        context.mib2TsdInfoFileFolder = Folder(context.mib2TsdFolder, "${context.nameWithoutSpaces}/InfoFile/1/default")
        context.mib2TsdInfoFileFolder.mkdirs()

        context.mib2TsdPackageFolder = Folder(context.mib2TsdFolder, "${context.nameWithoutSpaces.toLowerCase()}/ppoidb/1/default")
        context.mib2TsdPackageFolder.mkdirs()
    }
}
