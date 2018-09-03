package com.raycoarana.poitodiscover.domain

import java.io.File as JavaFile

class Folder(val javaFile: JavaFile) {
    constructor(parentFolder: Folder, name: String) : this(JavaFile(parentFolder.javaFile, name))
    constructor(name: String) : this(JavaFile(name))

    val name: String = javaFile.name

    fun mkdirs() = javaFile.mkdirs()
    fun listFiles(): List<File> = javaFile.listFiles().filter { it.isFile }.map { File(it) }
    fun listFolders(): List<Folder> = javaFile.listFiles().filter { it.isDirectory }.map { Folder(it) }
    fun deleteChilds() = javaFile.listFiles().forEach { it.deleteRecursively() }

    fun deleteVisibleChilds() {
        javaFile.listFiles()
                .filter { !it.isHidden && !it.name.startsWith(".") }
                .forEach { it.deleteRecursively() }
    }
}

fun createTempFolder(): Folder {
    val file = JavaFile.createTempFile("poi-", "")
    file.delete()
    file.mkdirs()
    return Folder(file)
}