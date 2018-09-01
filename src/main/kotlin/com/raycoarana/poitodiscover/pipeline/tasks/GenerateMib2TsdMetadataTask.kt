package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.core.appendCrLf
import com.raycoarana.poitodiscover.core.asUtf8WriterIn
import com.raycoarana.poitodiscover.core.sha1
import com.raycoarana.poitodiscover.domain.Checksum
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.generator.HashesFileGenerator
import com.raycoarana.poitodiscover.generator.IconsGenerator
import com.raycoarana.poitodiscover.generator.UpdateFileGenerator
import com.raycoarana.poitodiscover.pipeline.Task
import java.io.ByteArrayInputStream
import java.io.StringWriter
import java.io.Writer
import javax.inject.Inject

class GenerateMib2TsdMetadataTask @Inject constructor(
        private val updateFileGenerator: UpdateFileGenerator,
        private val iconsGenerator: IconsGenerator,
        private val hashesFileGenerator: HashesFileGenerator
) : Task {
    private lateinit var updateFileChecksum: Checksum
    private lateinit var hashesFileChecksum: Checksum

    override fun execute(context: Context) {
        updateFileChecksum = updateFileGenerator.generate(context, context.mib2TsdInfoFileFolder)
        iconsGenerator.generate(context, context.mib2TsdPackageFolder)

        //After generate all files, generate hashes file
        hashesFileChecksum = hashesFileGenerator.generate(context, context.mib2TsdPackageFolder, IconsGenerator.FOLDER_NAME_ICON)

        //Then generate metainfo file
        generateMetadataFile(context, context.mib2TsdFolder)
    }

    private fun generateMetadataFile(context: Context, parentFolder: Folder) {
        val stringWriter = StringWriter()
        generateMetadataFile(context, stringWriter)
        val metadataChecksum = ByteArrayInputStream(stringWriter.toString().toByteArray()).sha1(context.hashBlockSize).first()
        generateMetadataFile(context, FILE_NAME_METAINFO.asUtf8WriterIn(parentFolder), metadataChecksum)
    }

    private fun generateMetadataFile(context: Context, writer: Writer, metadataChecksum: String? = null) {
        writer.use {
            val baseFolderName = context.nameWithoutSpaces.toLowerCase()

            writer.appendCrLf("#################################################################################")
            writer.appendCrLf("#   ")
            writer.appendCrLf("# This is the description file for the Software Update of Volkswagen components")
            writer.appendCrLf("#")
            writer.appendCrLf("#################################################################################")
            writer.appendCrLf()
            writer.appendCrLf("[common]")
            if (metadataChecksum != null) {
                writer.appendCrLf("MetafileChecksum = \"$metadataChecksum\"")
            }
            writer.appendCrLf("Release = \"${context.name}\"")
            writer.appendCrLf("MUVersion = \"----\"")
            writer.appendCrLf("Vendor = \"TechniSat\"")
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
            writer.appendCrLf("SkipSaveTrainName = \"true\"")
            writer.appendCrLf("UserSWDL = \"true\"")
            writer.appendCrLf("Engineering = \"false\"")
            writer.appendCrLf("Region = \"Europe\"")
            writer.appendCrLf("Region2 = \"RoW\"")
            writer.appendCrLf("Region3 = \"USA\"")
            writer.appendCrLf("DelMmeBackups = \"true\"")
            writer.appendCrLf()
            writer.appendCrLf("[$baseFolderName]")
            writer.appendCrLf("VendorInfo = \"TechniSat\"")
            writer.appendCrLf("DeviceDescription = \"$baseFolderName\"")
            writer.appendCrLf("DeviceRelease = \"3\"")
            writer.appendCrLf("RequiredVersionOfDM = \"0\"")
            writer.appendCrLf("MainUnitDevice = \"true\"")
            writer.appendCrLf()

            writer.appendCrLf("[$baseFolderName\\ppoidb\\1\\default\\Dir]")
            writer.appendCrLf("CheckSumSize = \"${context.hashBlockSize}\"")
            writer.appendCrLf("CheckSum = \"${hashesFileChecksum.value}\"")
            writer.appendCrLf("FileSize = \"${hashesFileChecksum.fileSize}\"")
            writer.appendCrLf("Source = \"\"")
            writer.appendCrLf("Destination = \"/tsd/var/nav/$baseFolderName/\"")
            writer.appendCrLf("Version = \"1\"")
            writer.appendCrLf()
            writer.appendCrLf("[$baseFolderName\\InfoFile\\1\\default\\File]")
            writer.appendCrLf("CheckSumSize = \"${context.hashBlockSize}\"")
            writer.appendCrLf("CheckSum = \"${updateFileChecksum.value}\"")
            writer.appendCrLf("FileSize = \"${updateFileChecksum.fileSize}\"")
            writer.appendCrLf("Source = \"Update.txt\"")
            writer.appendCrLf("Destination = \"/tsd/etc/PoiUpdate.txt\"")
            writer.appendCrLf("Version = \"1\"")
        }
    }

    companion object {
        const val FILE_NAME_METAINFO = "metainfo2.txt"
    }
}
