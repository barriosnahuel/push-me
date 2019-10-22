package com.github.barriosnahuel.vossosunboton

import android.os.Build
import android.os.StrictMode
import com.crashlytics.android.Crashlytics
import java.util.concurrent.Executors

internal class StrictModeHelper {

    fun initializeWithDefaults() {
        setupThreadPolicy()
        setupVirtualMachinePolicy()
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

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        threadPolicyBuilder.penaltyListener(Executors.newSingleThreadExecutor(), StrictMode.OnThreadViolationListener {
            Crashlytics.logException(StrictModeException(it))
        })
    }

    StrictMode.setThreadPolicy(threadPolicyBuilder.build())
}

private fun setupVirtualMachinePolicy() {
    val vmPolicyBuilder = StrictMode.VmPolicy.Builder()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        vmPolicyBuilder.detectAll()
    } else {
        // Create VmPolicy Builder basing on Android Version because StrictMode does not check properly that GB remove activity before report an
        // InstanceCountViolation when API version is 21 or older. To avoid this error, we don't use [StrictMode.VmPolicy.Builder
        // .detectActivityLeaks] (inside [ ][StrictMode.VmPolicy.Builder.detectAll]) and call all other methods where applicable.
        // More info at: https://github.com/mercadolibre/mobile-android_testing/pull/37#discussion_r72981823

        vmPolicyBuilder.detectLeakedRegistrationObjects()
                .detectFileUriExposure()
                .detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects()
    }

    // #penaltyLog call is required for StrictModeNotifier
    vmPolicyBuilder.penaltyLog()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        vmPolicyBuilder.penaltyListener(Executors.newSingleThreadExecutor(), StrictMode.OnVmViolationListener {
            Crashlytics.logException(StrictModeException(it))
        })
    }

    StrictMode.setVmPolicy(vmPolicyBuilder.build())
}

private class StrictModeException(violation: Throwable) : RuntimeException(violation)