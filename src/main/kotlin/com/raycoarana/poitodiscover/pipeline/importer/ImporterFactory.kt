package com.raycoarana.poitodiscover.pipeline.importer

import com.raycoarana.poitodiscover.domain.SourceType
import javax.inject.Inject
import javax.inject.Provider

class ImporterFactory @Inject constructor(
        private val importFromGarminSpeedProvider: Provider<ImportFromMercadonaGarmin>,
        private val importFromMercadonaGarminProvider: Provider<ImportFromMercadonaGarmin>,
        private val importFromLufopGarminEuSpeedProvider: Provider<ImportFromLufopGarminEuSpeed>
){
    fun get(sourceType: SourceType): Importer =
        when(sourceType) {
            SourceType.GARMIN_SPEED -> importFromGarminSpeedProvider.get()
            SourceType.MERCADONA_GARMIN -> importFromMercadonaGarminProvider.get()
            SourceType.LUFOP_GARMIN_SOURCE -> importFromLufopGarminEuSpeedProvider.get()
        }
}
