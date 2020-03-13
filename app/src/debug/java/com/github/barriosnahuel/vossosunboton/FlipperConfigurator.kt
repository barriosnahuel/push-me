package com.github.barriosnahuel.vossosunboton

import android.app.Application
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.soloader.SoLoader

internal object FlipperConfigurator {

    fun initializeWithDefaults(application: Application) {
        SoLoader.init(application, false)

        val flipperClient = AndroidFlipperClient.getInstance(application)
        flipperClient.addPlugin(InspectorFlipperPlugin(application, DescriptorMapping.withDefaults()))
        flipperClient.start()
    }
}
