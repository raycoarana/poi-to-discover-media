package com.raycoarana.poitodiscover.core

import com.raycoarana.poitodiscover.domain.File
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.pipeline.Pipeline
import java.io.InputStream
import java.io.Writer
import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermissions
import java.security.MessageDigest
import java.util.*
import javax.xml.bind.annotation.adapters.HexBinaryAdapter
import java.io.File as JavaFile

fun String.asUtf8WriterIn(folder: Folder): Writer = this.asFileIn(folder).utf8Writer()
fun String.asFileIn(folder: Folder): File = File(JavaFile(folder.javaFile, this).setup())

fun JavaFile.setup(): JavaFile {
    this.createNewFile()
    //Files.setPosixFilePermissions(this.toPath(), PosixFilePermissions.fromString("rwxr-xr-x"))
    return this
}

fun String.copyResourceTo(parentFolder: Folder) {
    this.asFileIn(parentFolder).outputStream().use { stream ->
        Pipeline::class.java.getResourceAsStream("/$this").copyTo(stream)
    }
}

fun InputStream.sha1(blockSize: Int): List<String> {
    val sha1 = MessageDigest.getInstance("SHA-1")
    this.use { input ->

        val buffer = ByteArray(blockSize)
        var len = input.read(buffer)
        val checksumList = ArrayList<String>()

        while (len != -1) {
            sha1.update(buffer, 0, len)
            len = input.read(buffer)
            checksumList.add(HexBinaryAdapter().marshal(sha1.digest()).toLowerCase())
        }

        return checksumList
    }
}
