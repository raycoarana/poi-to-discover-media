package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.core.appendCrLf
import com.raycoarana.poitodiscover.core.asUtf8WriterIn
import com.raycoarana.poitodiscover.core.copyResourceTo
import com.raycoarana.poitodiscover.core.sha1
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.generator.*
import com.raycoarana.poitodiscover.pipeline.Task
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.io.Writer
import javax.inject.Inject

class GenerateMib2HighMetadataTask @Inject constructor(
        private val hashesFileGenerator: HashesFileGenerator,
        private val bitmapsGenerator: BitmapsGenerator,
        private val stringsFileGenerator: StringsFileGenerator,
        private val categoriesFileGenerator: CategoriesFileGenerator,
        private val updateFileGenerator: UpdateFileGenerator
) : Task {
    override fun execute(context: Context) {
        context.mib2HighUpdateFileChecksum = updateFileGenerator.generate(context, context.mib2HighInfoFileFolder)

        categoriesFileGenerator.generate(context, context.mib2HighPackageFolder)
        stringsFileGenerator.generate(context, context.mib2HighPackageFolder)
        bitmapsGenerator.generate(context, context.mib2HighPackageFolder)

        FILE_NAME_LANG_MAP.copyResourceTo(context.mib2HighPackageFolder)
        FILE_NAME_VERSIONS.copyResourceTo(context.mib2HighPackageFolder)

        //Only after generating all, we can generate hashes file
        context.mib2HighHashesFileChecksum = hashesFileGenerator.generate(context, context.mib2HighPackageFolder, BitmapsGenerator.FOLDER_NAME_BITMAPS)

        //Once we have hashes file, we can generate the metadata file
        generateMetadataFile(context, context.mib2HighFolder)
    }

    private fun generateMetadataFile(context: Context, parentFolder: Folder) {
        val stringWriter = StringWriter()
        generateMetadataFile(context, stringWriter)
        val metadataChecksum = ByteArrayInputStream(stringWriter.toString().toByteArray()).sha1(context.hashBlockSize).first()
        generateMetadataFile(context, FILE_NAME_METAINFO.asUtf8WriterIn(parentFolder), metadataChecksum)
    }

    private fun generateMetadataFile(context: Context, writer: Writer, metadataChecksum: String? = null) {
        writer.use {
            writer.appendCrLf("#################################################################################")
            writer.appendCrLf("#   ")
            writer.appendCrLf("# This is the description file for the Software Update of Volkswagen components")
            writer.appendCrLf("#")
            writer.appendCrLf("#################################################################################")
            writer.appendCrLf()
            writer.appendCrLf("[common]")
            writer.appendCrLf("skipSaveTrainName = \"true\"")
            writer.appendCrLf("vendor = \"ESO\"")
            writer.appendCrLf("skipCheckSignatureAndVariant = \"true\"")
            writer.appendCrLf("region = \"Europe\"")
            writer.appendCrLf("region2 = \"RoW\"")
            writer.appendCrLf("region3 = \"USA\"")
            writer.appendCrLf("variant = \"FM?-H-*-*-*\"")
            writer.appendCrLf("release = \"2012/2013\"")
            writer.appendCrLf("UserSWDL = \"true\"")
            writer.appendCrLf("skipMetaCRC = \"true\"")
            if (metadataChecksum != null) {
                writer.appendCrLf("MetafileChecksum = \"$metadataChecksum\"")
            }
            writer.appendCrLf("skipFileCopyCrc = \"true\"")
            writer.appendCrLf()
            writer.appendCrLf("[${context.nameWithoutSpaces}]")
            writer.appendCrLf("VendorInfo = \"ESO\"")
            writer.appendCrLf("DeviceDescription = \"${context.name} Packages\"")
            writer.appendCrLf()
            writer.appendCrLf("ConfigComponent = \"${context.nameWithoutSpaces}\"")
            writer.appendCrLf("PacketName = \"Package\"")
            writer.appendCrLf("ConfigCheckPath = \"${context.nameWithoutSpaces}/Package/0/default\"")
            writer.appendCrLf("ConfigFinalizePath = \"/net/mmx/mnt/navdb/${context.nameWithoutSpaces}/Package\"")
            writer.appendCrLf("IncludeModules = \",Package,\"")
            writer.appendCrLf()
            writer.appendCrLf("[${context.nameWithoutSpaces}\\Package\\0\\default\\Dir]")
            writer.appendCrLf("CheckSumSize = \"${context.hashBlockSize}\"")
            writer.appendCrLf("CheckSum = \"${context.mib2HighHashesFileChecksum.value}\"")
            writer.appendCrLf("FileSize = \"${context.mib2HighHashesFileChecksum.fileSize}\"")
            writer.appendCrLf("CheckType = \"CheckReadData\"")
            writer.appendCrLf("DeleteDestinationDirBeforeCopy = \"true\"")
            writer.appendCrLf("Version = \"1\"")
            writer.appendCrLf("UpdateOnlyExisting = \"false\"")
            writer.appendCrLf("IsDestinationInFFS = \"false\"")
            writer.appendCrLf("Source = \"\"")
            writer.appendCrLf("Destination = \"/net/mmx/mnt/navdb/${context.nameWithoutSpaces}/Package\"")
            writer.appendCrLf("DisplayName = \"${context.displayName}\"")
            writer.appendCrLf()
            writer.appendCrLf("[${context.nameWithoutSpaces}\\InfoFile\\0\\default\\File]")
            writer.appendCrLf("CheckSumSize = \"${context.hashBlockSize}\"")
            writer.appendCrLf("CheckSum = \"${context.mib2HighUpdateFileChecksum.value}\"")
            writer.appendCrLf("FileSize = \"${context.mib2HighUpdateFileChecksum.fileSize}\"")
            writer.appendCrLf("Version = \"3\"")
            writer.appendCrLf("Source = \"Update.txt\"")
            writer.appendCrLf("Destination = \"/net/mmx/mnt/navdb/${context.nameWithoutSpaces}/Update.txt\"")
            writer.appendCrLf("DisplayName = \"Database Information\"")
            writer.appendCrLf("DeleteDestinationDirBeforeCopy = \"true\"")
            writer.appendCrLf("UpdateOnlyExisting = \"false\"")
        }
    }

    companion object {
        const val FILE_NAME_METAINFO = "metainfo2.txt"
        const val FILE_NAME_LANG_MAP = "lang_map.xml"
        const val FILE_NAME_VERSIONS = "versions.xml"
    }
}
