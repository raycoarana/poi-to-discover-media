package com.raycoarana.poitodiscover.pipeline.tasks

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.pipeline.Task
import javax.inject.Inject

class CleanUpTask @Inject constructor() : Task {
    override fun execute(context: Context) {
        //Nothing by now!
    }
}