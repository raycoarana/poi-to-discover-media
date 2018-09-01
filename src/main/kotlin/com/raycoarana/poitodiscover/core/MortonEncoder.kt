package com.raycoarana.poitodiscover.core

import javax.inject.Inject

class MortonEncoder @Inject constructor() {

    fun encode(latitude: Double, longitude: Double): Long {
        var vLatitude = latitude
        var vLongitude = longitude
        if (vLatitude < 0) vLatitude += 180.0
        if (vLongitude < 0) vLongitude += 360.0

        var latw = (vLatitude * 0xffffffffL / 360.0).toLong()
        var lngw = (vLongitude * 0xffffffffL / 360.0).toLong()

        latw = widen(latw)
        lngw = widen(lngw)
        return lngw or (latw shl 1)
    }

    private fun widen(value: Long): Long {
        var workingValue = value
        workingValue = workingValue or (workingValue shl 16)
        workingValue = workingValue and 0x0000ffff0000ffffL
        workingValue = workingValue or (workingValue shl 8)
        workingValue = workingValue and 0x00ff00ff00ff00ffL
        workingValue = workingValue or (workingValue shl 4)
        workingValue = workingValue and 0x0f0f0f0f0f0f0f0fL
        workingValue = workingValue or (workingValue shl 2)
        workingValue = workingValue and 0x3333333333333333L
        workingValue = workingValue or (workingValue shl 1)
        workingValue = workingValue and 0x5555555555555555L
        return workingValue
    }

    fun decode(value: Long): Pair<Double, Double> {
        var lat = unwiden((value shr 1)).toDouble()
        var lng = unwiden(value).toDouble()
        lat *= 360.0 / 0xffffffff
        lng *= 360.0 / 0xffffffff
        if (lat >= 90) {
            lat -= 180
        }
        if (lng >= 180) {
            lng -= 360
        }
        return Pair(lat, lng)
    }

    private fun unwiden(value: Long): Long {
        var workingValue = value
        workingValue = workingValue and 0x5555555555555555
        workingValue = workingValue or (workingValue shr 1)
        workingValue = workingValue and 0x3333333333333333
        workingValue = workingValue or (workingValue shr 2)
        workingValue = workingValue and 0x0f0f0f0f0f0f0f0f
        workingValue = workingValue or (workingValue shr 4)
        workingValue = workingValue and 0x00ff00ff00ff00ff
        workingValue = workingValue or (workingValue shr 8)
        workingValue = workingValue and 0x0000ffff0000ffff
        workingValue = workingValue or (workingValue shr 16)
        workingValue = workingValue and 0x00000000ffffffff
        return workingValue
    }
}