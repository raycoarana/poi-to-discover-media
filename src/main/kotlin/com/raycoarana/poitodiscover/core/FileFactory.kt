package com.raycoarana.poitodiscover.core

import com.raycoarana.poitodiscover.domain.File
import com.raycoarana.poitodiscover.domain.Folder
import javax.inject.Inject

class FileFactory @Inject constructor() {
    fun createFile(fileName: String, parentFolder: Folder): File = fileName.asFileIn(parentFolder)
}