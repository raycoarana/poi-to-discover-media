package com.raycoarana.poitodiscover.domain

data class Poi(
        var latitude: String,
        var longitude: String,
        var description: String,
        var type: PoiType
)