package com.raycoarana.poitodiscover.core

import java.util.*
import javax.inject.Inject

class Translations @Inject constructor(locale: Locale) {
    private var messages: ResourceBundle = ResourceBundle.getBundle("MessagesBundle", locale)

    fun get(key: String) : String = messages.getString(key)
}