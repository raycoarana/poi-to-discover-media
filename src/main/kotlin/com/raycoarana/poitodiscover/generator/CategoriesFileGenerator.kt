package com.raycoarana.poitodiscover.generator

import com.raycoarana.poitodiscover.core.appendLf
import com.raycoarana.poitodiscover.core.appendXmlHeader
import com.raycoarana.poitodiscover.core.asUtf8WriterIn
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import javax.inject.Inject

class CategoriesFileGenerator @Inject constructor() {

    fun generate(context: Context, parentFolder: Folder) {
        val poiTypes = context.poiByType.keys
        FILE_NAME_CATEGORIES.asUtf8WriterIn(parentFolder).use { writer ->
            writer.appendXmlHeader()
            writer.appendLf("<poicategories version=\"02010011\">")

            val orderedPoiTypes = poiTypes.sortedBy { it.highId }

            //Generate categories
            writer.appendLf("    <categories>")
            orderedPoiTypes.forEach {
                val resId = it.getResId()
                val categoryId = it.getCategoryId()
                val imagePath = "${BitmapsGenerator.FOLDER_NAME_BITMAPS}/${it.image}"
                writer.appendLf("        <category bitmapIndex=\"$resId\" warnable=\"true\" name=\"${it.highId}\" id=\"$categoryId\">")
                writer.appendLf("            <bitmap res_id=\"$resId\">$imagePath,0,0,39,39,-19,-39</bitmap>")
                writer.appendLf("        </category>")
            }
            writer.appendLf("    </categories>")

            //Generate types
            writer.appendLf("    <types>")
            orderedPoiTypes.forEach {
                val resId = it.getResId()
                val imagePath = "${BitmapsGenerator.FOLDER_NAME_BITMAPS}/${it.image}"
                writer.appendLf("        <type id=\"${it.highId}\">")
                writer.appendLf("            <bitmap res_id=\"$resId\" size=\"10\" module=\"0\">$imagePath,0,0,39,39,-19,-39</bitmap>")
                writer.appendLf("            <bitmap res_id=\"$resId\" size=\"10\" module=\"1\">$imagePath,0,0,39,39,-19,-39</bitmap>")
                writer.appendLf("            <zoomlevel max=\"60\" min=\"0\"/>")
                writer.appendLf("            <priority>1</priority>")
                writer.appendLf("            <code>${it.highId}</code>")
                writer.appendLf("        </type>")
            }
            writer.appendLf("    </types>")

            //Generate search
            writer.appendLf("    <search type=\"Generic\">")
            orderedPoiTypes.forEach {
                val categoryId = it.getCategoryId()
                writer.appendLf("        <category index=\"10\" id=\"$categoryId\">")
                writer.appendLf("            <type id=\"${it.highId}\"/>")
                writer.appendLf("        </category>")
            }
            writer.appendLf("    </search>")

            writer.appendLf("</poicategories>")
        }
    }

    companion object {
        const val FILE_NAME_CATEGORIES = "categories.pc"
    }
}