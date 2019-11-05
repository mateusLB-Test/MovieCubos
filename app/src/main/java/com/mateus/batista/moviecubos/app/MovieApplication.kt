package com.mateus.batista.moviecubos.app

import android.app.Application
import com.mateus.batista.moviecubos.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApplication)
            modules(listOf(dataModule, remoteModule, serviceModule, domainModule, viewModelModule))
        }
    }
}