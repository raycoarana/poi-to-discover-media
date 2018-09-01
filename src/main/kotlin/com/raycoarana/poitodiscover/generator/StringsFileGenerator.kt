package com.raycoarana.poitodiscover.generator

import com.raycoarana.poitodiscover.core.Translations
import com.raycoarana.poitodiscover.core.appendLf
import com.raycoarana.poitodiscover.core.appendXmlHeader
import com.raycoarana.poitodiscover.core.asUtf8WriterIn
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import javax.inject.Inject

class StringsFileGenerator @Inject constructor(private val translations: Translations) {
    fun generate(context: Context, parentFolder: Folder) {
        FILE_NAME_STRINGS.asUtf8WriterIn(parentFolder).use { writer ->
            writer.appendXmlHeader()
            writer.appendLf("<strings>")
            context.poiByType.keys.sortedBy { it.highId }
                    .forEach {
                        val label = translations.get(it.toString())
                        writer.appendLf("    <string type=\"0\" id=\"${it.highId}\">")
                        writer.appendLf("        <lang lang=\"de-DE\">")
                        writer.appendLf("            <text>$label</text>")
                        writer.appendLf("        </lang>")
                        writer.appendLf("    </string>")
                    }
            writer.appendLf("</strings>")
        }
    }

    companion object {
        const val FILE_NAME_STRINGS = "strings_de-DE.xml"
    }
}