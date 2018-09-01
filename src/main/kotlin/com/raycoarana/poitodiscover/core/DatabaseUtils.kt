package com.raycoarana.poitodiscover.core

import com.raycoarana.poitodiscover.domain.File
import java.sql.Connection
import java.sql.DriverManager

fun File.openSqliteDatabase(): Connection {
    if (!this.exists()) {
        this.createNewFile()
    }
    return DriverManager.getConnection("jdbc:sqlite:${this.absolutePath}")
}
