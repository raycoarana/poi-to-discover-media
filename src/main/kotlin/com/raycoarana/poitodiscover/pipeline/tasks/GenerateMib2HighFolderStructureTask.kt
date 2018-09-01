package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.pipeline.Task
import javax.inject.Inject

class GenerateMib2HighFolderStructureTask @Inject constructor() : Task {
    override fun execute(context: Context) {
        context.radarPoisFolder = Folder(context.outputFolder, context.nameWithoutSpaces)
        context.radarPoisFolder.mkdirs()

        context.mib2HighFolder = Folder(context.radarPoisFolder, "MIB2HIGH")
        context.mib2HighFolder.mkdirs()

        context.mib2HighInfoFileFolder = Folder(context.mib2HighFolder, "${context.nameWithoutSpaces}/InfoFile/0/default")
        context.mib2HighInfoFileFolder.mkdirs()

        context.mib2HighPackageFolder = Folder(context.mib2HighFolder, "${context.nameWithoutSpaces}/Package/0/default")
        context.mib2HighPackageFolder.mkdirs()
    }
}
