package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.core.appendCrLf
import com.raycoarana.poitodiscover.core.asUtf8WriterIn
import com.raycoarana.poitodiscover.core.sha1
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.generator.HashesFileGenerator
import com.raycoarana.poitodiscover.pipeline.Task
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.io.Writer
import javax.inject.Inject

class GenerateMib2DeMetadataTask @Inject constructor() : Task {
    override fun execute(context: Context) {
        val stringWriter = StringWriter()
        generateMetadataFile(context, stringWriter)
        val metadataChecksum = ByteArrayInputStream(stringWriter.toString().toByteArray()).sha1(context.hashBlockSize).first()
        generateMetadataFile(context, FILE_NAME_METAINFO.asUtf8WriterIn(context.mib2DeFolder), metadataChecksum)
    }

    private fun generateMetadataFile(context: Context, writer: Writer, metadataChecksum: String? = null) {
        writer.use {
            val baseFolderName = context.nameWithoutSpaces
            val dirFileSize = context.mib2HighHashesFileChecksum.fileSize + context.mib2HighPackageFolder.listFiles()
                    .first { file -> file.name == HashesFileGenerator.FILE_NAME_HASHES }
                    .length()

            writer.appendCrLf("#################################################################################")
            writer.appendCrLf("#  \t")
            writer.appendCrLf("# This is the description file for the Software Update of Volkswagen components")
            writer.appendCrLf("#")
            writer.appendCrLf("#################################################################################")
            writer.appendCrLf()
            writer.appendCrLf("[common]")
            writer.appendCrLf("skipSaveTrainName = \"true\"")
            writer.appendCrLf("vendor = \"Delphi\"")
            writer.appendCrLf("skipCheckSignatureAndVariant = \"true\"")
            writer.appendCrLf("region = \"Europe\"")
            writer.appendCrLf("region2 = \"RoW\"")
            writer.appendCrLf("region3 = \"USA\"")
            writer.appendCrLf("variant = \"17245\"")
            writer.appendCrLf("variant2 = \"17246\"")
            writer.appendCrLf("variant3 = \"17250\"")
            writer.appendCrLf("variant4 = \"17251\"")
            writer.appendCrLf("variant5 = \"17247\"")
            writer.appendCrLf("variant6 = \"17252\"")
            writer.appendCrLf("variant7 = \"17254\"")
            writer.appendCrLf("variant8 = \"17255\"")
            writer.appendCrLf("variant9 = \"17268\"")
            writer.appendCrLf("variant10 = \"17269\"")
            writer.appendCrLf("variant11 = \"17270\"")
            writer.appendCrLf("variant12 = \"17271\"")
            writer.appendCrLf("release = \"2012/2013\"")
            writer.appendCrLf("UserSWDL = \"true\"")
            writer.appendCrLf("skipMetaCRC = \"true\"")
            if (metadataChecksum != null) {
                writer.appendCrLf("MetafileChecksum = \"$metadataChecksum\"")
            }
            writer.appendCrLf("skipFileCopyCrc = \"true\"")
            writer.appendCrLf()
            writer.appendCrLf("[$baseFolderName]")
            writer.appendCrLf("VendorInfo = \"Delphi\"")
            writer.appendCrLf("DeviceDescription = \"${context.name} Packages\"")
            writer.appendCrLf()
            writer.appendCrLf("ConfigComponent = \"$baseFolderName\"")
            writer.appendCrLf("PacketName = \"Package\"")
            writer.appendCrLf("ConfigCheckPath = \"../MIB2HIGH/$baseFolderName/Package/0/default\"")
            writer.appendCrLf("ConfigFinalizePath = \"/media/swdl/packages/$baseFolderName/Package\"")
            writer.appendCrLf("IncludeModules = \",Package,\"")
            writer.appendCrLf()
            writer.appendCrLf("[$baseFolderName\\Package\\0\\default\\Dir]")
            writer.appendCrLf("CheckSumSize = \"${context.hashBlockSize}\"")
            writer.appendCrLf("CheckSum = \"${context.mib2HighHashesFileChecksum.value}\"")
            writer.appendCrLf("FileSize = \"$dirFileSize\"")
            writer.appendCrLf("CheckType = \"CheckReadData\"")
            writer.appendCrLf("DeleteDestinationDirBeforeCopy = \"true\"")
            writer.appendCrLf("Version = \"1\"")
            writer.appendCrLf("UpdateOnlyExisting = \"false\"")
            writer.appendCrLf("IsDestinationInFFS = \"false\"")
            writer.appendCrLf("Source = \"../../../../../MIB2HIGH/$baseFolderName/Package/0/default/\"")
            writer.appendCrLf("Destination = \"/media/swdl/packages/$baseFolderName/Package\"")
            writer.appendCrLf("DisplayName = \"${context.displayName}\"")
            writer.appendCrLf()
            writer.appendCrLf("[$baseFolderName\\InfoFile\\0\\default\\File]")
            writer.appendCrLf("CheckSumSize = \"${context.hashBlockSize}\"")
            writer.appendCrLf("CheckSum = \"${context.mib2HighUpdateFileChecksum.value}\"")
            writer.appendCrLf("FileSize = \"${context.mib2HighUpdateFileChecksum.fileSize}\"")
            writer.appendCrLf("Version = \"3\"")
            writer.appendCrLf("Source = \"../../../../../MIB2HIGH/$baseFolderName/InfoFile/0/default/Update.txt\"")
            writer.appendCrLf("Destination = \"/media/swdl/packages/$baseFolderName/\"")
            writer.appendCrLf("DisplayName = \"Database Information\"")
            writer.appendCrLf("DeleteDestinationDirBeforeCopy = \"false\"")
            writer.appendCrLf("UpdateOnlyExisting = \"false\"")
        }
    }

    companion object {
        const val FILE_NAME_METAINFO = "metainfo2.txt"
    }
}
