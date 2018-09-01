package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.domain.PoiType
import com.raycoarana.poitodiscover.pipeline.Task
import org.slf4j.Logger
import javax.inject.Inject

/**
 * ConsolidateTask POIs into main categories
 */
class ConsolidateTask @Inject constructor(private val logger: Logger) : Task {
    override fun execute(context: Context) {
        val groupByType = context.rawPois.groupBy { it.type }.toMutableMap()

        context.ignoreCategories.forEach { groupByType.remove(it) }
        groupByType.remove(PoiType.Unknown)

        logger.info("POIs after consolidating")
        groupByType.forEach {
            logger.info("  ${it.value.size} of type ${it.key}")
        }
        context.poiByType = groupByType
    }
}