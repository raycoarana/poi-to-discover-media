package com.raycoarana.poitodiscover.domain

import java.io.File as JavaFile

class Folder(val javaFile: JavaFile) {
    constructor(parentFolder: Folder, name: String) : this(JavaFile(parentFolder.javaFile, name))
    constructor(name: String) : this(JavaFile(name))

    val name: String = javaFile.name

    fun mkdirs() = javaFile.mkdirs()
    fun listFiles(): List<File> = javaFile.listFilesOrEmpty().filter { it.isFile }.map { File(it) }

    fun listFilesRecursively(): List<File> = javaFile.listFilesOrEmpty().map {
        if (it.isDirectory) {
            Folder(it).listFiles()
        } else {
            listOf(File(it))
        }
    }.flatten()

    fun listFolders(): List<Folder> = javaFile.listFilesOrEmpty().filter { it.isDirectory }.map { Folder(it) }
    fun deleteChildren() = javaFile.listFilesOrEmpty().forEach { it.deleteRecursively() }

    fun deleteVisibleChildren() {
        javaFile.listFilesOrEmpty()
                .filter { !it.isHidden && !it.name.startsWith(".") }
                .forEach { it.deleteRecursively() }
    }

    private fun JavaFile.listFilesOrEmpty() = listFiles() ?: emptyArray()
}

fun createTempFolder(): Folder {
    val file = JavaFile.createTempFile("poi-", "")
    file.delete()
    file.mkdirs()
    return Folder(file)
}