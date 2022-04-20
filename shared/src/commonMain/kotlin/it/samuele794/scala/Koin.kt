package it.samuele794.scala

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import kotlinx.datetime.Clock
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

fun initKoin(appModule: Module): KoinApplication {
    val koinApplication = startKoin {
        modules(
            appModule,
            platformModule,
            coreModule
        )
    }

    // Dummy initialization logic, making use of appModule declarations for demonstration purposes.
    val koin = koinApplication.koin
    // doOnStartup is a lambda which is implemented in Swift on iOS side
    val doOnStartup = koin.get<() -> Unit>()
    doOnStartup.invoke()

    val kermit = koin.get<Logger> { parametersOf(null) }
    // AppInfo is a Kotlin interface with separate Android and iOS implementations
//    val appInfo = koin.get<AppInfo>()
//    kermit.v { "App Id ${appInfo.appId}" }

    return koinApplication
}

private val coreModule = module {
//    single {
//        DatabaseHelper(
//            get(),
//            getWith("DatabaseHelper"),
//            Dispatchers.Default
//        )
//    }
//    single<DogApi> {
//        DogApiImpl(
//            getWith("DogApiImpl"),
//            get()
//        )
//    }
    single<Clock> {
        Clock.System
    }

    // platformLogWriter() is a relatively simple config option, useful for local debugging. For production
    // uses you *may* want to have a more robust configuration from the native platform. In KaMP Kit,
    // that would likely go into platformModule expect/actual.
    // See https://github.com/touchlab/Kermit
    val baseLogger =
        Logger(config = StaticConfig(logWriterList = listOf(platformLogWriter())), "KampKit")
    factory { (tag: String?) -> if (tag != null) baseLogger.withTag(tag) else baseLogger }

//    single {
//        BreedRepository(
//            get(),
//            get(),
//            get(),
//            getWith("BreedRepository"),
//            get()
//        )
//    }
}

expect val platformModule: Module