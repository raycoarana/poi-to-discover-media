package com.raycoarana.poitodiscover.domain

enum class PoiType(val highId: Int, val tsdId: Int, val image: String) {
    ResidentialArea(0, 2001, "000_image.png"),
    Hidden(1, 2002, "001_image.png"),
    Fixed(2, 2003, "002_image.png"),
    Photo(3, 2004, "003_image.png"),
    Semaphores(4, 2005, "004_image.png"),
    SectionEnd(5, 2006, "005_image.png"),
    SectionStart(6, 2007, "006_image.png"),
    Tunnel(7, 2008, "007_image.png"),
    Unknown(-1, -1, "");

    fun getResId() = highId + 1
    fun getCategoryId() = highId + 1000
}