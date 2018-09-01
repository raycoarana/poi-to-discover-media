package com.raycoarana.poitodiscover

import com.raycoarana.poitodiscover.domain.Context
import java.io.File
import java.util.*

fun main(args : Array<String>) {
    //--- TODO

    val inputFileName = "/Users/rayco/Downloads/garminvelocidad 2xx-12xx-13xx-14xx-2xxx-3xxx y posteriores.zip"
    val outputFolder = "/Users/rayco/Downloads/radares_mk2"

    val file = File(outputFolder, "PersonalPOI")
    if(file.exists() && !file.deleteRecursively()) {
        throw Exception("Can not delete destination")
    }
    val file2 = File(outputFolder, "metainfo2.txt")
    if(file2.exists() && !file2.delete()) {
        throw Exception("Can not delete destination")
    }
    //file.mkdirs()

    //--- END TODO

    val (language, country) = "es_ES".split("_") //TODO Read from args
    val name = "Personal POI" //TODO Read from args
    val displayName = "Personal POIs" //TODO Read from args
    val date = "2018-08-29" //TODO Read from args
    val hashBlockSize: Int = 524288 //TODO Read from args

    val context = Context(
            inputFileName,
            outputFolder,
            Locale(language, country),
            name,
            displayName,
            date,
            hashBlockSize
    )
    val mainComponent = DaggerMainComponent.builder()
            .mainModule(MainModule(context))
            .build()

    val pipeline = mainComponent.buildPipeline()
    pipeline.execute()
}