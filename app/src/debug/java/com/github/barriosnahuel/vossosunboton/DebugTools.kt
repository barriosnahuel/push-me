package com.github.barriosnahuel.vossosunboton

import android.app.Application
import com.facebook.stetho.Stetho
import com.squareup.leakcanary.LeakCanary

internal class DebugTools {

    companion object {
        @JvmStatic
        fun configure(application: Application) {
            LeakCanary.install(application)
            Stetho.initializeWithDefaults(application)
            StrictModeHelper().initializeWithDefaults()
        }
    }
}