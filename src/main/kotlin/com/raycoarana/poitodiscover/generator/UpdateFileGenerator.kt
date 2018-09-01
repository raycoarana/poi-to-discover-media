package com.raycoarana.poitodiscover.generator

import com.raycoarana.poitodiscover.core.FileFactory
import com.raycoarana.poitodiscover.core.appendCrLf
import com.raycoarana.poitodiscover.domain.Checksum
import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.Folder
import javax.inject.Inject
import javax.inject.Named

class UpdateFileGenerator @Inject constructor(
        private val fileFactory: FileFactory,
        @Named("langs") private val langs: Array<String>
) {
    fun generate(context: Context, parentFolder: Folder): Checksum {
        val updateFile = fileFactory.createFile(FILE_NAME_UPDATE, parentFolder)
        updateFile.utf8Writer().use { writer ->
            writer.appendCrLf("device=${context.nameWithoutSpaces}")
            writer.appendCrLf()

            langs.forEach { writer.appendCrLf("name.$it=${context.name}") }
            writer.appendCrLf()

            langs.forEach { writer.appendCrLf("version.$it=${context.date}") }
        }

        return Checksum(updateFile.length(), updateFile.sha1(context.hashBlockSize).first())
    }

    companion object {
        const val FILE_NAME_UPDATE = "Update.txt"
    }
}