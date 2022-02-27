package com.github.barriosnahuel.vossosunboton

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.leakcanary2.LeakCanary2FlipperPlugin
import com.facebook.flipper.plugins.navigation.NavigationFlipperPlugin
import com.facebook.soloader.SoLoader
import java.util.Calendar

internal object FlipperConfigurator {

    fun initializeWithDefaults(application: Application) {
        SoLoader.init(application, false)

        val flipperClient = AndroidFlipperClient.getInstance(application)
        flipperClient.addPlugin(InspectorFlipperPlugin(application, DescriptorMapping.withDefaults()))
        flipperClient.addPlugin(LeakCanary2FlipperPlugin())

        application.registerActivityLifecycleCallbacks(FlipperLifecycleCallbacks())
        flipperClient.addPlugin(NavigationFlipperPlugin.getInstance())

        flipperClient.start()
    }

    class FlipperLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
            NavigationFlipperPlugin.getInstance()
                .sendNavigationEvent(activity.intent.dataString, activity.localClassName, Calendar.getInstance().time)
        }

        override fun onActivityStarted(activity: Activity) {
        }

        override fun onActivityResumed(activity: Activity) {
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }
    }
}
