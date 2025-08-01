package com.raycoarana.poitodiscover.domain

data class ImportConfig(
    val countryFilter: String? = null, // null means include all countries, otherwise filter by this country code
    val excludeCountries: List<String> = emptyList() // list of country codes to exclude
)
