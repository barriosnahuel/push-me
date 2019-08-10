package com.github.barriosnahuel.vossosunboton

import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.StrictMode
import com.nshmura.strictmodenotifier.StrictModeNotifier

internal class StrictModeHelper {

    /**
     * More info about the notifier app at [github.com/nshmura/strictmode-notifier](https://github.com/nshmura/strictmode-notifier).
     */
    fun initializeWithDefaults(application: Application) {
        StrictModeNotifier.install(application)

        Handler().post(::setupStrictModePolicies)
    }
}

/**
 * https://developer.android.com/reference/android/os/StrictMode.html
 */
private fun setupStrictModePolicies() {
    StrictMode.setThreadPolicy(getStrictModeThreadPolicy().build())

    StrictMode.setVmPolicy(getStrictModeVmPolicy().build())
}

/**
 * @return `null` if you want to disable strict mode threading policy
 */
private fun getStrictModeThreadPolicy(): StrictMode.ThreadPolicy.Builder {
    val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
            .detectCustomSlowCalls()
            .detectNetwork()
            .permitDiskReads()
            .penaltyLog() // Required for StrictModeNotifier!

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        threadPolicyBuilder.detectResourceMismatches()
    }

    return threadPolicyBuilder
}

/**
 * @return `null` if you want to disable strict mode VM policy.
 */
private fun getStrictModeVmPolicy(): StrictMode.VmPolicy.Builder {
    val builder = StrictMode.VmPolicy.Builder()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        builder.detectAll()
    } else {
        // Create VmPolicy Builder basing on Android Version because StrictMode does not check properly that GB remove
        // activity before report an InstanceCountViolation when API version is 21 or older. To avoid this error, we don't
        // use [StrictMode.VmPolicy.Builder.detectActivityLeaks] (inside [ ][StrictMode.VmPolicy.Builder.detectAll]) and call all other methods
        // where applicable. More info at: https://github.com/mercadolibre/mobile-android_testing/pull/37#discussion_r72981823

        builder.detectLeakedRegistrationObjects()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure()
        }

        builder.detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
    }

    // #penaltyLog call is required for StrictModeNotifier
    builder.penaltyLog()

    return builder
}