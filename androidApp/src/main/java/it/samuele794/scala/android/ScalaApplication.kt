package it.samuele794.scala.android

import android.app.Application
import it.samuele794.scala.initKoin
import org.koin.dsl.module

class ScalaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(
            module {

            }
        )
    }
}