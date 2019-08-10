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

        Handler().post {
            setupThreadPolicy()
            setupVirtualMachinePolicy()
        }
    }
}

private fun setupThreadPolicy() {
    val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
            .detectCustomSlowCalls()
            .detectNetwork()
            .permitDiskReads()
            .penaltyLog() // Required for StrictModeNotifier!

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        threadPolicyBuilder.detectResourceMismatches()
    }

    StrictMode.setThreadPolicy(threadPolicyBuilder.build())
}

private fun setupVirtualMachinePolicy() {
    val vmPolicyBuilder = StrictMode.VmPolicy.Builder()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        vmPolicyBuilder.detectAll()
    } else {
        // Create VmPolicy Builder basing on Android Version because StrictMode does not check properly that GB remove
        // activity before report an InstanceCountViolation when API version is 21 or older. To avoid this error, we don't
        // use [StrictMode.VmPolicy.Builder.detectActivityLeaks] (inside [ ][StrictMode.VmPolicy.Builder.detectAll]) and call all other methods
        // where applicable. More info at: https://github.com/mercadolibre/mobile-android_testing/pull/37#discussion_r72981823

        vmPolicyBuilder.detectLeakedRegistrationObjects()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            vmPolicyBuilder.detectFileUriExposure()
        }

        vmPolicyBuilder.detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
    }

    // #penaltyLog call is required for StrictModeNotifier
    vmPolicyBuilder.penaltyLog()

    StrictMode.setVmPolicy(vmPolicyBuilder.build())
}