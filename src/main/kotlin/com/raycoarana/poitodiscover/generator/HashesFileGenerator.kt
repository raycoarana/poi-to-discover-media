package com.raycoarana.poitodiscover.generator

import com.raycoarana.poitodiscover.core.appendCrLf
import com.raycoarana.poitodiscover.core.appendField
import com.raycoarana.poitodiscover.core.asFileIn
import com.raycoarana.poitodiscover.domain.Checksum
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.File
import com.raycoarana.poitodiscover.domain.Folder
import java.io.Writer
import javax.inject.Inject

class HashesFileGenerator @Inject constructor() {
    fun generate(context: Context, parentFolder: Folder, resourcesFolderName: String): Checksum {
        val filesInFolder = parentFolder.listFiles()
        val filesInBitmapsFolder = Folder(parentFolder, resourcesFolderName).listFiles()

        val files = filesInFolder.union(filesInBitmapsFolder.asIterable())
                .sortedBy { context.hashesFileOrderOf(resourcesFolderName, it.relativePathTo(parentFolder)) }

        val hashesFile = FILE_NAME_HASHES.asFileIn(parentFolder)
        hashesFile.utf8Writer().use { writer ->
            files.forEach { writer.writeFileHash(it, it.relativePathTo(parentFolder), context.hashBlockSize) }
        }

        return Checksum(files.map { it.length() }.sum(), hashesFile.sha1(context.hashBlockSize).first())
    }

    private fun Writer.writeFileHash(file: File, path: String, blockSize: Int) {
        appendField("FileName", path)
        appendField("FileSize", file.length())
        appendField("CheckSumSize", blockSize)
        file.sha1(blockSize).forEachIndexed { index, s ->
            val id = if (index == 0) "" else "$index"
            appendField("CheckSum$id", s)
        }
        appendCrLf()
    }

    companion object {
        const val FILE_NAME_HASHES = "hashes.txt"
    }
}
