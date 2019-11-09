package com.github.barriosnahuel.vossosunboton

import android.app.Application
import com.github.barriosnahuel.vossosunboton.commons.android.error.Trackable
import com.squareup.leakcanary.AnalysisResult
import com.squareup.leakcanary.DisplayLeakService
import com.squareup.leakcanary.HeapDump
import com.squareup.leakcanary.LeakCanary
import kotlin.reflect.KProperty

internal object LeakCanaryConfigurator {

    private var tracker: Trackable? = null

    fun initializeWithDefaults(application: Application, tracker: Trackable) {
        LeakCanary.install(application)
        this.tracker = tracker
    }

    operator fun getValue(thisRef: MemoryLeakTrackerService, property: KProperty<*>): Trackable = tracker!!
}

/**
 * Custom service that reports memory leaks detected by LeakCanary to our error tracking tool.
 */
class MemoryLeakTrackerService : DisplayLeakService() {

    private val tracker: Trackable by LeakCanaryConfigurator

    override fun afterDefaultHandling(heapDump: HeapDump, result: AnalysisResult, leakInfo: String) {
        if (!result.leakFound || result.excludedLeak) {
            return
        }

        tracker.track(MemoryLeakException(result.leakTraceAsFakeException()))
    }
}

private class MemoryLeakException(e: Throwable) : RuntimeException(e)