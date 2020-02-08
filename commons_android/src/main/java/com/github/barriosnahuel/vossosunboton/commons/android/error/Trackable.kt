package com.github.barriosnahuel.vossosunboton.commons.android.error

import com.crashlytics.android.Crashlytics
import timber.log.Timber

/**
 * Simple interface to hide the implementation details of our error tracking tool.
 */
interface Trackable {

    /**
     * Report the given `throwable` to our tracking tool.
     */
    fun track(throwable: Throwable)
}

/**
 * Current implementation of the [Trackable] interface.
 * This tracker should be used along the entire project whenever you want to track some error.
 */
object Tracker : Trackable {

    override fun track(throwable: Throwable) {
        Timber.e("Tracking error to Firebase Crashlytics: %s", throwable.message)
        Crashlytics.logException(throwable)
    }
}
