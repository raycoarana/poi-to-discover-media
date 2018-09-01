package com.raycoarana.poitodiscover.domain

import com.raycoarana.poitodiscover.core.sha1
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.io.File as JavaFile

/**
 * Wraps a Java File for easy testing
 */
class File constructor(private val javaFile: JavaFile) {
    constructor(folder: Folder, filename: String) : this(JavaFile(folder.javaFile, filename))
    constructor(filename: String) : this(JavaFile(filename))

    val name: String = javaFile.name
    val absolutePath: String = javaFile.absolutePath
    val extension: String = javaFile.extension
    val nameWithoutExtension: String = javaFile.nameWithoutExtension

    fun utf8Reader(): Reader = javaFile.reader(Charsets.UTF_8)
    fun utf8Writer(): Writer = javaFile.writer(Charsets.UTF_8)
    fun inputStream(): InputStream = javaFile.inputStream()
    fun outputStream(): OutputStream = javaFile.outputStream()
    fun sha1(blockSize: Int): List<String> = FileInputStream(javaFile).sha1(blockSize)
    fun length(): Long = javaFile.length()
    fun relativePathTo(parentFolder: Folder): String = javaFile.relativeTo(parentFolder.javaFile).path
    fun exists(): Boolean = javaFile.exists()
    fun createNewFile(): Boolean = javaFile.createNewFile()

    fun decompressTo(inputFolder: Folder) {
        ZipFile(this.absolutePath).use { zip ->
            zip.entries().asSequence().forEach { entry ->
                zip.getInputStream(entry).use { input ->
                    val file = File(inputFolder, entry.name)
                    if(!entry.isDirectory) {
                        file.mkdirs()
                        file.createNewFile()
                        file.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                }
            }
        }
    }

    fun mkdirs() {
        if(!javaFile.parentFile.exists() && !javaFile.parentFile.mkdirs()) {
            throw Exception("Can not create parent directories: ${javaFile.parentFile.absolutePath}")
        }
    }
}