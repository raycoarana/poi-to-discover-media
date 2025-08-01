package com.raycoarana.poitodiscover.pipeline.importer

import com.raycoarana.poitodiscover.domain.SourceType
import javax.inject.Inject
import javax.inject.Provider

class ImporterFactory @Inject constructor(
        private val importFromLufopGarminEuSpeedProvider: Provider<ImportFromLufopGarminEuSpeed>
){
    fun get(sourceType: SourceType): Importer =
        when(sourceType) {
            SourceType.LUFOP_GARMIN_SOURCE -> importFromLufopGarminEuSpeedProvider.get()
        }
}
