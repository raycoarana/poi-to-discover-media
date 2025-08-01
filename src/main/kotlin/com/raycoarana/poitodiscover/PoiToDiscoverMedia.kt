package com.raycoarana.poitodiscover

import com.raycoarana.poitodiscover.domain.ImportConfig
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.PoiType
import java.text.SimpleDateFormat
import java.util.*

const val ARG_INPUT = "input"
const val ARG_OUTPUT = "output"
const val ARG_DATE = "date"
const val ARG_HASH_BLOCK_SIZE = "hashBlockSize"
const val ARG_LANGUAGE = "lang"
const val ARG_IGNORE_CATEGORIES = "ignore"
const val ARG_COUNTRY_FILTER = "countryFilter"
const val ARG_EXCLUDE_COUNTRIES = "excludeCountries"

const val DEFAULT_HASH_BLOCK_SIZE = 524288

val argDefaults = mapOf(
        ARG_INPUT to "",
        ARG_OUTPUT to "./output",
        ARG_DATE to SimpleDateFormat("YYYY-MM-dd").format(Date()),
        ARG_HASH_BLOCK_SIZE to DEFAULT_HASH_BLOCK_SIZE.toString(),
        ARG_LANGUAGE to "es_ES",
        ARG_IGNORE_CATEGORIES to "",
        ARG_COUNTRY_FILTER to "",
        ARG_EXCLUDE_COUNTRIES to ""
)

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        printHelp()
        return
    }

    val argumentMap = argDefaults.toMutableMap()
    args.forEach {
        val (key, value) = it.split("=")
        argumentMap[key] = value
    }

    val inputFileName = argumentMap[ARG_INPUT]!!.split(",")
    val outputFolderName = argumentMap[ARG_OUTPUT]!!
    val (language, country) = argumentMap[ARG_LANGUAGE]!!.split("_")
    val name = "Personal POI"
    val displayName = "Personal POIs"
    val date = argumentMap[ARG_DATE]!!
    val hashBlockSize: Int = argumentMap[ARG_HASH_BLOCK_SIZE]!!.toIntOrNull() ?: DEFAULT_HASH_BLOCK_SIZE

    val validCategories = PoiType.values().map { it.toString() }.toSet()
    val ignoreCategories = argumentMap[ARG_IGNORE_CATEGORIES]!!
            .split(",")
            .filter { !it.isBlank() }
            .filter { validCategories.contains(it) }
            .map { PoiType.valueOf(it) }

    // Parse import configuration from command line arguments
    val countryFilter = argumentMap[ARG_COUNTRY_FILTER]!!.takeIf { it.isNotBlank() }
    val excludeCountries = argumentMap[ARG_EXCLUDE_COUNTRIES]!!
            .split(",")
            .filter { it.isNotBlank() }

    val importConfig = ImportConfig(
            countryFilter = countryFilter,
            excludeCountries = excludeCountries
    )

    val outputFolder = Folder(outputFolderName)
    outputFolder.mkdirs()
    outputFolder.deleteVisibleChildren()

    val context = Context(
            inputFileName,
            outputFolderName,
            Locale(language, country),
            name,
            displayName,
            date,
            hashBlockSize,
            ignoreCategories,
            importConfig
    )
    val mainComponent = DaggerMainComponent.builder()
            .mainModule(MainModule(context))
            .build()

    val pipeline = mainComponent.buildPipeline()
    pipeline.execute()
}

fun printHelp() {
    println("POI to VW Discover Media/Pro")
    println()
    println("Usage:")
    println()
    println("\tinput=[listOfPathToInputZipFiles]\t(Ex: input=File1.zip,File2.zip)")
    println("\toutput=[pathToOutput]\t(Default: ./output)")
    println("\tlang=[languageIsoCode]\t(Default: es_ES)")
    println("\tdate=[dateOfPois]\t(Default: YYYY-MM-DD)")
    println("\thashBlockSize=[sizeOfChecksum]\t(Default: 524288)")
    println("\tignore=[commaSeparatedListOfCategoriesToIgnore]\t(Default: None)\t(Ex: ignore=Hidden,Photo)")
    println("\tcountryFilter=[countryCode]\t(Default: None)\t(Ex: countryFilter=FR - include only French POIs)")
    println("\texcludeCountries=[commaSeparatedListOfCountryCodes]\t(Default: None)\t(Ex: excludeCountries=ES,IT)")
    println()
    println("Input files supported:")
    println("\t- Lufop-Zones-de-danger-EU-CSV.zip")
    println()
    println("Country filtering examples:")
    println("\t- Include only French POIs: countryFilter=FR")
    println("\t- Exclude Spanish POIs: excludeCountries=ES")
    println("\t- Exclude multiple countries: excludeCountries=ES,IT,DE")
    println("\t- No filtering (include all): don't specify countryFilter or excludeCountries")
    println()
    println("Categories to use in ignore:")
    println("\t- ResidentialArea")
    println("\t- Hidden")
    println("\t- Fixed")
    println("\t- Photo")
    println("\t- Semaphores")
    println("\t- SectionEnd")
    println("\t- SectionStart")
    println("\t- Tunnel")
    println()
}
