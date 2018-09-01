package com.raycoarana.poitodiscover

import com.raycoarana.poitodiscover.domain.Context
import com.raycoarana.poitodiscover.pipeline.Pipeline
import com.raycoarana.poitodiscover.pipeline.TasksProvider
import dagger.Component
import dagger.Module
import dagger.Provides
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*
import javax.inject.Named

@Module
class MainModule(val context: Context) {
    init {
        Class.forName("org.sqlite.JDBC")
    }

    @Provides fun provideLogger(): Logger = LoggerFactory.getLogger("default")
    @Provides fun providePipeline(tasksProvider: TasksProvider, logger: Logger): Pipeline = Pipeline(tasksProvider, context, logger)
    @Provides fun provideLocale(): Locale = context.locale
    @Provides @Named("langs") fun provideLangs(): Array<String> = arrayOf("default", "de_DE", "en_GB", "en_SA", "es_ES", "fr_FR", "it_IT", "nl_NL", "pt_PT", "ru_RU", "pl_PL", "cs_CZ")
}

@Component(modules = [MainModule::class])
interface MainComponent {
    fun buildPipeline() : Pipeline
}