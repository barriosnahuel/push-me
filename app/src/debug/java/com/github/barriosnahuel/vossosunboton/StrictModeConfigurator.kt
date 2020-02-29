package com.github.barriosnahuel.vossosunboton

import android.os.Build
import android.os.StrictMode
import com.github.barriosnahuel.vossosunboton.commons.android.error.Trackable
import java.util.concurrent.Executors

internal object StrictModeConfigurator {

    fun initializeWithDefaults(trackable: Trackable) {
        setupThreadPolicy(trackable)
        setupVirtualMachinePolicy(trackable)
    }
}

private fun setupThreadPolicy(trackable: Trackable) {
    val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
        .detectCustomSlowCalls()
        .detectNetwork()
        .penaltyLog()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        threadPolicyBuilder.detectResourceMismatches()
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        threadPolicyBuilder.penaltyListener(Executors.newSingleThreadExecutor(), StrictMode.OnThreadViolationListener {
            trackable.track(StrictModeException(it))
        })
    }

    StrictMode.setThreadPolicy(threadPolicyBuilder.build())
}

private fun setupVirtualMachinePolicy(trackable: Trackable) {
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

    vmPolicyBuilder.penaltyLog()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        vmPolicyBuilder.penaltyListener(Executors.newSingleThreadExecutor(), StrictMode.OnVmViolationListener {
            trackable.track(StrictModeException(it))
        })
    }

    StrictMode.setVmPolicy(vmPolicyBuilder.build())
}

private class StrictModeException(violation: Throwable) : RuntimeException(violation)