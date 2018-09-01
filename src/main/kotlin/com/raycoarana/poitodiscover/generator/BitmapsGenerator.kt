package com.raycoarana.poitodiscover.generator

import com.raycoarana.poitodiscover.core.appendLf
import com.raycoarana.poitodiscover.core.appendXmlHeader
import com.raycoarana.poitodiscover.core.asUtf8WriterIn
import com.raycoarana.poitodiscover.core.copyResourceTo
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.PoiType
import java.io.Writer
import javax.inject.Inject

class BitmapsGenerator @Inject constructor() {
    fun generate(context: Context, parentFolder: Folder) {
        generateBitmapsFile(parentFolder, context.poiByType.keys)
        generateBitmapsResources(parentFolder, context)
    }

    private fun generateBitmapsFile(packageFolder: Folder, keys: Set<PoiType>) {
        FILE_NAME_BITMAPS.asUtf8WriterIn(packageFolder)
                .use { writer ->
                    writer.appendXmlHeader()
                    writer.appendLf("<bitmaps count=\"${keys.size}\">")
                    keys.sortedBy { it.highId }.forEach { writer.writeResource(it) }
                    writer.appendLf("</bitmaps>")
                }
    }

    private fun Writer.writeResource(poiType: PoiType) {
        val id = poiType.getResId()
        val filename = "$FOLDER_NAME_BITMAPS/${poiType.image}"
        this.appendLf("    <resource id=\"$id\" name=\"$filename,0,0,39,39,-19,-39\"/>")
    }

    private fun generateBitmapsResources(packageFolder: Folder, context: Context) {
        val bitmapsFolder = Folder(packageFolder, FOLDER_NAME_BITMAPS)
        bitmapsFolder.mkdirs()
        context.poiByType.keys.forEach { it.image.copyResourceTo(bitmapsFolder) }
    }

    companion object {
        const val FOLDER_NAME_BITMAPS = "bitmaps"

        const val FILE_NAME_BITMAPS = "bitmaps.xml"
    }
}