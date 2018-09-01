package com.raycoarana.poitodiscover.domain

import com.raycoarana.poitodiscover.generator.IconsGenerator
import java.util.*

class Context(
        inputFileName: String,
        outputFolderName: String,
        val locale: Locale,
        val name: String,
        val displayName: String,
        val date: String,
        val hashBlockSize: Int
) {
    val inputFile = File(inputFileName)
    val outputFolder = Folder(outputFolderName)
    val nameWithoutSpaces: String = name.replace(" ", "")

    // Workplace
    lateinit var rawPois: List<Poi>
    lateinit var poiByType: Map<PoiType, List<Poi>>

    // Workplace folders
    lateinit var inputFolder: Folder
    lateinit var radarPoisFolder: Folder
    lateinit var mib2HighFolder: Folder
    lateinit var mib2HighInfoFileFolder: Folder
    lateinit var mib2HighPackageFolder: Folder
    lateinit var mib2TsdFolder: Folder
    lateinit var mib2TsdInfoFileFolder: Folder
    lateinit var mib2TsdPackageFolder: Folder
    lateinit var mib2DeFolder: Folder

    // MIB2HIGH files checksum for MIB2DE
    lateinit var mib2HighUpdateFileChecksum: Checksum
    lateinit var mib2HighHashesFileChecksum: Checksum

    /**
     * TODO: Hack to keep the files order, this should be delete when confirmed order doesn't matter
     */
    fun hashesFileOrderOf(folder: String, name: String?): Int =
            if (folder == IconsGenerator.FOLDER_NAME_ICON) {
                arrayOf(
                        "icon/002_image.png",
                        "icon/001_image.png",
                        "poidata.db3",
                        "icon/000_image.png",
                        "icon/004_image.png",
                        "icon/003_image.png",
                        "icon/006_image.png",
                        "icon/005_image.png",
                        "icon/007_image.png"
                )
            } else {
                arrayOf(
                        "bitmaps.xml",
                        "lang_map.xml",
                        "bitmaps/003_image.png",
                        "categories.pc",
                        "strings_de-DE.xml",
                        "bitmaps/002_image.png",
                        "bitmaps/000_image.png",
                        "bitmaps/005_image.png",
                        "versions.xml",
                        "bitmaps/001_image.png",
                        "bitmaps/006_image.png",
                        "poidata.db",
                        "bitmaps/004_image.png",
                        "bitmaps/007_image.png"
                )
            }.indexOf(name)
}