package com.raycoarana.poitodiscover.pipeline.importer

import com.raycoarana.poitodiscover.domain.Folder
import com.raycoarana.poitodiscover.domain.Poi

interface Importer {
    fun execute(inputFolder: Folder): List<Poi>
}