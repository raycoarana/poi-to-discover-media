package com.raycoarana.poitodiscover.pipeline

import com.raycoarana.poitodiscover.pipeline.tasks.*
import javax.inject.Inject
import javax.inject.Provider

class TasksProvider @Inject constructor(
        private val importTaskProvider: Provider<ImportTask>,
        private val consolidateTaskProvider: Provider<ConsolidateTask>,
        private val generateMib2HighFolderStructureTaskProvider: Provider<GenerateMib2HighFolderStructureTask>,
        private val generateMib2HighDatabaseTaskProvider: Provider<GenerateMib2HighDatabaseTask>,
        private val generateMib2HighMetadataTaskProvider: Provider<GenerateMib2HighMetadataTask>,
        private val generateMib2TsdFolderStructureTaskProvider: Provider<GenerateMib2TsdFolderStructureTask>,
        private val generateMib2TsdDatabaseTaskProvider: Provider<GenerateMib2TsdDatabaseTask>,
        private val generateMib2TsdMetadataTaskProvider: Provider<GenerateMib2TsdMetadataTask>,
        private val generateMib2DeFolderStructureTaskProvider: Provider<GenerateMib2DeFolderStructureTask>,
        private val generateMib2DeMetadataTaskProvider: Provider<GenerateMib2DeMetadataTask>,
        private val generateMetadataTaskProvider: Provider<GenerateMetadataTask>,
        private val cleanUpTaskProvider: Provider<CleanUpTask>
) {
    fun get(): List<Task> =
            listOf(
                    importTaskProvider.get(),
                    consolidateTaskProvider.get(),
                    generateMib2HighFolderStructureTaskProvider.get(),
                    generateMib2HighDatabaseTaskProvider.get(),
                    generateMib2HighMetadataTaskProvider.get(),
                    generateMib2TsdFolderStructureTaskProvider.get(),
                    generateMib2TsdDatabaseTaskProvider.get(),
                    generateMib2TsdMetadataTaskProvider.get(),
                    generateMib2DeFolderStructureTaskProvider.get(),
                    generateMib2DeMetadataTaskProvider.get(),
                    generateMetadataTaskProvider.get(),
                    cleanUpTaskProvider.get()
            )
}