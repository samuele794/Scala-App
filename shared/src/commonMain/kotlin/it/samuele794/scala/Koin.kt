package it.samuele794.scala

import co.touchlab.kermit.Logger
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.crashlytics.CrashlyticsLogWriter
import co.touchlab.kermit.platformLogWriter
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.create
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import it.samuele794.scala.api.maps.GoogleMapsApi
import it.samuele794.scala.repository.*
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.koin.core.KoinApplication
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun initKoin(koinApplication: KoinApplication.() -> Unit, appModule: Module): KoinApplication {
    Logger.addLogWriter(CrashlyticsLogWriter())

    val koinApplication = startKoin {
        koinApplication()
        modules(
            appModule,
            platformModule,
            coreModule
        )
    }

    // Dummy initialization logic, making use of appModule declarations for demonstration purposes.
    val koin = koinApplication.koin
    // doOnStartup is a lambda which is implemented in Swift on iOS side
//    val doOnStartup = koin.get<() -> Unit>()
//    doOnStartup.invoke()

    val kermit = koin.get<Logger> { parametersOf(null) }
    // AppInfo is a Kotlin interface with separate Android and iOS implementations
//    val appInfo = koin.get<AppInfo>()
    kermit.v { "App Id" }

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

    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = true
            isLenient = true
        }
    }

    factory(named("JsonClient")) {
        HttpClient {
            install(ContentNegotiation) {
                install(Logging)
            }
        }
    }

    factory {
        Ktorfit(baseUrl = BuildKonfig.GOOGLE_API_BASE_URL)
            .create<GoogleMapsApi>()
    }

    single<UserRepository> {
        UserRepositoryImpl(placeRepository = get())
    }

    single<PlaceRepository> {
        PlaceRepositoryImpl()
    }

    single {
        GoogleMapsRepository(get(), get(), get(named("GOOGLE_API_KEY")))
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

// Simple function to clean up the syntax a bit
fun KoinComponent.injectLogger(tag: String): Lazy<Logger> = inject { parametersOf(tag) }

expect val platformModule: Module