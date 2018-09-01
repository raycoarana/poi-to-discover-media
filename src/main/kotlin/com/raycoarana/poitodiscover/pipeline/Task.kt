package com.raycoarana.poitodiscover.pipeline

import com.raycoarana.poitodiscover.domain.Context

interface Task {
    fun execute(context: Context)
}