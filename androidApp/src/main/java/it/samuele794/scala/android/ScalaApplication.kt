package it.samuele794.scala.android

import android.app.Application
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import it.samuele794.scala.initKoin
import it.samuele794.scala.viewmodel.auth.AuthViewModel
import it.samuele794.scala.viewmodel.auth.NativeAuth
import it.samuele794.scala.viewmodel.login.LoginViewModel
import it.samuele794.scala.viewmodel.onboarding.OnBoardingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

class ScalaApplication : Application() {
    override fun onCreate() {
        Firebase.initialize(this)

        super.onCreate()
        initKoin(
            koinApplication = {
                androidContext(this@ScalaApplication)
            },
            appModule = module {
                factory { NativeAuth(get(), BuildConfig.GOOGLE_AUTH_APIKEY) }

                viewModel { AuthViewModel(get(), get { parametersOf("AuthViewModel") }) }
                viewModel { parameters ->
                    LoginViewModel(
                        get { parametersOf("LoginViewModel") },
                        parameters.get(),
                        get()
                    )
                }
                viewModel { OnBoardingViewModel(get { parametersOf("OnBoardingViewModel") }) }
            }
        )
    }
}