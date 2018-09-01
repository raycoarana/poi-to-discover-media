package com.raycoarana.poitodiscover.generator

import com.raycoarana.poitodiscover.core.copyResourceTo
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import javax.inject.Inject

class IconsGenerator @Inject constructor() {
    fun generate(context: Context, parentFolder: Folder) {
        val bitmapsFolder = Folder(parentFolder, FOLDER_NAME_ICON)
        bitmapsFolder.mkdirs()
        context.poiByType.keys.forEach { it.image.copyResourceTo(bitmapsFolder) }
    }

    companion object {
        const val FOLDER_NAME_ICON = "icon"
    }
}