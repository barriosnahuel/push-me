package com.github.barriosnahuel.vossosunboton

import android.app.Application
import timber.log.Timber

abstract class CustomBuildTypeApplication : Application() {

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())

        super.onCreate()

        Timber.d("Creating DEBUG application...")

        DebugTools.configure(this)
    }
}
