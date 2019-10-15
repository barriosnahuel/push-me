package com.github.barriosnahuel.vossosunboton

import androidx.multidex.MultiDexApplication
import timber.log.Timber

internal abstract class CustomBuildTypeApplication : MultiDexApplication() {

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())

        super.onCreate()

        Timber.d("Creating DEBUG application...")

        DebugTools.configure(this)
    }
}
