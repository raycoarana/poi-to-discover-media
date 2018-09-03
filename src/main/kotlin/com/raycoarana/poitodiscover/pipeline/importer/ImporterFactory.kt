package com.raycoarana.poitodiscover.pipeline.importer

import com.raycoarana.poitodiscover.domain.SourceType
import com.raycoarana.poitodiscover.pipeline.Task
import javax.inject.Inject
import javax.inject.Provider

class ImporterFactory @Inject constructor(
        private val importFromGarminSpeedProvider: Provider<ImportFromGarminSpeed>,
        private val importFromLufopGarminEuSpeedProvider: Provider<ImportFromLufopGarminEuSpeed>
){
    fun get(sourceType: SourceType): Importer =
        when(sourceType) {
            SourceType.GARMIN_SPEED -> importFromGarminSpeedProvider.get()
            SourceType.LUFOP_GARMIN_SOURCE -> importFromLufopGarminEuSpeedProvider.get()
            else -> throw Exception("Not supported sourceType")
        }
}
