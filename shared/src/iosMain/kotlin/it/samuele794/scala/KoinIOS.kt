package it.samuele794.scala

import co.touchlab.kermit.Logger
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

fun initKoinIos(
//    appInfo: AppInfo,
    doOnStartup: () -> Unit
): KoinApplication = initKoin(
    koinApplication = {

    },
    appModule = module {
//        single<Settings> { AppleSettings(userDefaults) }
//        single { appInfo }
        single { doOnStartup }
    }
)

actual val platformModule = module {
//    single<SqlDriver> { NativeSqliteDriver(KaMPKitDb.Schema, "KampkitDb") }
//
//    single { Darwin.create() }
//
//    single { BreedCallbackViewModel(get(), getWith("BreedCallbackViewModel")) }
}

// Access from Swift to create a logger
@Suppress("unused")
fun Koin.loggerWithTag(tag: String) =
    get<Logger>(qualifier = null) { parametersOf(tag) }

//@Suppress("unused") // Called from Swift
//object KotlinDependencies : KoinComponent {
//    fun getBreedViewModel() = getKoin().get<BreedCallbackViewModel>()
//}