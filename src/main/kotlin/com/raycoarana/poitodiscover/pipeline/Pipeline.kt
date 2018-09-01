package com.raycoarana.poitodiscover.pipeline

import com.raycoarana.poitodiscover.domain.Context
import org.slf4j.Logger
import javax.inject.Inject

class Pipeline @Inject constructor(private val tasks: TasksProvider,
                                   private val context: Context,
                                   private val logger: Logger) {
    fun execute() {
        tasks.get().forEach {
            val taskName = it.javaClass.simpleName
            logger.info("Starting with $taskName")
            it.execute(context)
            logger.info("Finished with $taskName")
        }
    }
}
