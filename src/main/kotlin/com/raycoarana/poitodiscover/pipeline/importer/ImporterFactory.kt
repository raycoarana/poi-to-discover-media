package com.raycoarana.poitodiscover.pipeline.importer

import com.raycoarana.poitodiscover.domain.SourceType
import com.raycoarana.poitodiscover.pipeline.Task
import javax.inject.Inject
import javax.inject.Provider

class ImporterFactory @Inject constructor(
        private val importFromGarminSpeedProvider: Provider<ImportFromGarminSpeed>
){
    fun get(sourceType: SourceType): Task =
        when(sourceType) {
            SourceType.GARMIN_SPEED -> importFromGarminSpeedProvider.get()
            else -> throw Exception("Not supported sourceType")
        }
}
