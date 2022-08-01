package com.vama.eiliyaabedini

import android.app.Application
import com.vama.eiliyaabedini.di.appModule
import com.vama.eiliyaabedini.di.dataModule
import com.vama.eiliyaabedini.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class VamaApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Start Koin
        startKoin {
            androidLogger()
            androidContext(this@VamaApplication)
            modules(appModule, networkModule, dataModule)
        }
    }
}
