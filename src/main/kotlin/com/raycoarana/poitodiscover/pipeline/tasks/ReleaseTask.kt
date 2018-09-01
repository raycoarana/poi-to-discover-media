package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.File
import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.pipeline.Task
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import javax.inject.Inject

class ReleaseTask @Inject constructor() : Task {
    override fun execute(context: Context) {
        val currentTime = Date().time
        val zipFile = File(context.outputFolder, "Release-${context.date}-$currentTime.zip")

        val files = context.outputFolder.listFiles()
        val folders = context.outputFolder.listFolders()
        ZipOutputStream(zipFile.outputStream()).use {
            it.putFolders(context.outputFolder, folders)
            it.putFiles(context.outputFolder, files)
        }
    }

    private fun ZipOutputStream.putFiles(rootFolder: Folder, files: List<File>) {
        files.filter { !it.name.startsWith(".") }
                .forEach {
                    this.putNextEntry(ZipEntry(it.relativePathTo(rootFolder)))
                    it.inputStream().copyTo(this)
                    this.closeEntry()
                }
    }

    private fun ZipOutputStream.putFolders(rootFolder: Folder, folders: List<Folder>) {
        folders.filter { !it.name.startsWith(".") }
                .forEach {
                    this.putFolders(rootFolder, it.listFolders())
                    this.putFiles(rootFolder, it.listFiles())
                }
    }
}

