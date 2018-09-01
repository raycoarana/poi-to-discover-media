package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.core.appendCrLf
import com.raycoarana.poitodiscover.core.appendLf
import com.raycoarana.poitodiscover.core.asUtf8WriterIn
import com.raycoarana.poitodiscover.core.sha1
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.pipeline.Task
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.io.Writer
import javax.inject.Inject

class GenerateMetadataTask @Inject constructor() : Task {
    override fun execute(context: Context) {

        val stringWriter = StringWriter()
        generateMetadataFile(context, stringWriter)
        val metadataChecksum = ByteArrayInputStream(stringWriter.toString().toByteArray()).sha1(context.hashBlockSize).first()
        generateMetadataFile(context, FILE_NAME_METAINFO.asUtf8WriterIn(context.outputFolder), metadataChecksum)
    }

    private fun generateMetadataFile(context: Context, writer: Writer, metadataChecksum: String? = null) {
        writer.use {
            val baseFolderName = context.nameWithoutSpaces

            writer.appendCrLf("#################################################################################")
            writer.appendCrLf("#   ")
            writer.appendCrLf("# This is the description file for the Software Update of Volkswagen components")
            writer.appendCrLf("#")
            writer.appendCrLf("#################################################################################")
            writer.appendCrLf()
            writer.appendCrLf("[common]")
            writer.appendCrLf("Vendor = \"Volkswagen\"")
            writer.appendCrLf("Variant = \"FM?-H-*-*-*\"")
            writer.appendCrLf("Variant2 = \"17206\"")
            writer.appendCrLf("Variant3 = \"17207\"")
            writer.appendCrLf("Variant4 = \"37206\"")
            writer.appendCrLf("Variant5 = \"37207\"")
            writer.appendCrLf("Variant6 = \"47203\"")
            writer.appendCrLf("Variant7 = \"47204\"")
            writer.appendCrLf("Variant8 = \"17216\"")
            writer.appendCrLf("Variant9 = \"17217\"")
            writer.appendCrLf("Variant10 = \"37212\"")
            writer.appendCrLf("Variant11 = \"37213\"")
            writer.appendCrLf("Variant12 = \"47208\"")
            writer.appendCrLf("Variant13 = \"47209\"")
            writer.appendCrLf("Variant14 = \"17212\"")
            writer.appendCrLf("Variant15 = \"37208\"")
            writer.appendCrLf("Variant16 = \"47205\"")
            writer.appendCrLf("Variant17 = \"17222\"")
            writer.appendCrLf("Variant18 = \"47210\"")
            writer.appendCrLf("Variant19 = \"17224\"")
            writer.appendCrLf("Variant20 = \"37214\"")
            writer.appendCrLf("Variant21 = \"17210\"")
            writer.appendCrLf("Variant22 = \"17220\"")
            writer.appendCrLf("Variant23 = \"17245\"")
            writer.appendCrLf("Variant24 = \"17246\"")
            writer.appendCrLf("Variant25 = \"17250\"")
            writer.appendCrLf("Variant26 = \"17251\"")
            writer.appendCrLf("Variant27 = \"17247\"")
            writer.appendCrLf("Variant28 = \"17252\"")
            writer.appendCrLf("Variant29 = \"17254\"")
            writer.appendCrLf("Variant30 = \"17255\"")
            writer.appendCrLf("Variant31 = \"17268\"")
            writer.appendCrLf("Variant32 = \"17269\"")
            writer.appendCrLf("Variant33 = \"17270\"")
            writer.appendCrLf("Variant34 = \"17271\"")
            writer.appendCrLf("Variant35 = \"47213\"")
            writer.appendCrLf("Variant36 = \"47214\"")
            writer.appendCrLf("Region = \"Europe\"")
            writer.appendCrLf("Region2 = \"RoW\"")
            writer.appendCrLf("Region3 = \"USA\"")
            if (metadataChecksum != null) {
                writer.appendCrLf("MetafileChecksum = \"$metadataChecksum\"")
            }
            writer.appendCrLf()
            writer.appendCrLf("[common_Release_1]")
            writer.appendCrLf("Name = \"MIB 2 High ${context.name}\"")
            writer.appendCrLf("Path = \"./$baseFolderName/MIB2HIGH\"")
            writer.appendCrLf("Variant = \"FM?-H-*-*-*\"")
            writer.appendCrLf("Region = \"Europe\"")
            writer.appendCrLf("Region2 = \"RoW\"")
            writer.appendCrLf("Region3 = \"USA\"")
            writer.appendCrLf()
            writer.appendCrLf("[common_Release_2]")
            writer.appendCrLf("Name = \"MIB 2 Standard Delphi ${context.name}\"")
            writer.appendCrLf("Path = \"./$baseFolderName/MIB2DE\"")
            writer.appendCrLf("Variant = \"17245\"")
            writer.appendCrLf("Variant2 = \"17246\"")
            writer.appendCrLf("Variant3 = \"17250\"")
            writer.appendCrLf("Variant4 = \"17251\"")
            writer.appendCrLf("Variant5 = \"17247\"")
            writer.appendCrLf("Variant6 = \"17252\"")
            writer.appendCrLf("Variant7 = \"17254\"")
            writer.appendCrLf("Variant8 = \"17255\"")
            writer.appendCrLf("Variant9 = \"17268\"")
            writer.appendCrLf("Variant10 = \"17269\"")
            writer.appendCrLf("Variant11 = \"17270\"")
            writer.appendCrLf("Variant12 = \"17271\"")
            writer.appendCrLf("Region = \"Europe\"")
            writer.appendCrLf("Region2 = \"RoW\"")
            writer.appendCrLf("Region3 = \"USA\"")
            writer.appendCrLf()
            writer.appendCrLf("[common_Release_3]")
            writer.appendCrLf("Name = \"MIB 2 Standard TechniSat ${context.name}\"")
            writer.appendCrLf("Path = \"./$baseFolderName/MIB2TSD\"")
            writer.appendCrLf("Variant = \"17206\"")
            writer.appendCrLf("Variant2 = \"17207\"")
            writer.appendCrLf("Variant3 = \"37206\"")
            writer.appendCrLf("Variant4 = \"37207\"")
            writer.appendCrLf("Variant5 = \"47203\"")
            writer.appendCrLf("Variant6 = \"47204\"")
            writer.appendCrLf("Variant7 = \"17216\"")
            writer.appendCrLf("Variant8 = \"17217\"")
            writer.appendCrLf("Variant9 = \"37212\"")
            writer.appendCrLf("Variant10 = \"37213\"")
            writer.appendCrLf("Variant11 = \"47208\"")
            writer.appendCrLf("Variant12 = \"47209\"")
            writer.appendCrLf("Variant13 = \"17212\"")
            writer.appendCrLf("Variant14 = \"37208\"")
            writer.appendCrLf("Variant15 = \"47205\"")
            writer.appendCrLf("Variant16 = \"17222\"")
            writer.appendCrLf("Variant17 = \"47210\"")
            writer.appendCrLf("Variant18 = \"17224\"")
            writer.appendCrLf("Variant19 = \"37214\"")
            writer.appendCrLf("Variant20 = \"17210\"")
            writer.appendCrLf("Variant21 = \"17220\"")
            writer.appendCrLf("Variant22 = \"47213\"")
            writer.appendCrLf("Variant23 = \"47214\"")
            writer.appendCrLf("Region = \"Europe\"")
            writer.appendCrLf("Region2 = \"RoW\"")
            writer.appendCrLf("Region3 = \"USA\"")
            writer.appendCrLf()
            writer.appendLf("[Signature]")
            writer.appendLf("signature1 = \"9880af1b5d5456de15affd708b3b74b2\"")
            writer.appendLf("signature2 = \"3cf0dbe1d0baf902e43229a07cfb9281\"")
            writer.appendLf("signature3 = \"3c76dea04775d174ac4f937445ae538b\"")
            writer.appendLf("signature4 = \"84d056d12279144f13341a7ccec3a7a2\"")
            writer.appendLf("signature5 = \"39120b41cbf137351aa6a08c732b3521\"")
            writer.appendLf("signature6 = \"5a224666234d8700e6c46bf2a943452c\"")
            writer.appendLf("signature7 = \"b06daa662fc8d9a1aee981c0a5df7c1a\"")
            writer.append("signature8 = \"4bcd566ead66604069d58ab0a9f77d14\"")
        }
    }

    companion object {
        const val FILE_NAME_METAINFO = "metainfo2.txt"
    }
}
