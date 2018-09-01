package com.raycoarana.poitodiscover.domain

import org.slf4j.LoggerFactory
import java.io.File as JavaFile

class Folder(val javaFile: JavaFile) {
    constructor(parentFolder: Folder, name: String) : this(JavaFile(parentFolder.javaFile, name))
    constructor(name: String) : this(JavaFile(name))

    fun mkdirs() = javaFile.mkdirs()
    fun listFiles(): List<File> = javaFile.listFiles().filter { it.isFile }.map { File(it) }
    fun delete() {
        if(!javaFile.deleteRecursively()) {
            LoggerFactory.getLogger(Folder::class.java).info("Error deleting folder: ${this.javaFile.name}")
        }
    }
}

fun createTempFolder(): Folder {
    val file = JavaFile.createTempFile("poi-", "")
    file.delete()
    file.mkdirs()
    return Folder(file)
}