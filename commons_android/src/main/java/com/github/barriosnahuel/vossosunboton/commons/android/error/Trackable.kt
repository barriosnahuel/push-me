package com.github.barriosnahuel.vossosunboton.commons.android.error

import com.crashlytics.android.Crashlytics
import timber.log.Timber

interface Trackable {
    fun track(throwable: Throwable)
}

object Tracker : Trackable {

    override fun track(throwable: Throwable) {
        Timber.e("Tracking error to Firebase Crashlytics: %s", throwable.message)
        Crashlytics.logException(throwable)
    }
}