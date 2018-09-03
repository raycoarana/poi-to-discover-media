package com.raycoarana.poitodiscover.core

operator fun Regex.contains(text: CharSequence): Boolean = this.matches(text)
