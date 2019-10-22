package com.github.barriosnahuel.vossosunboton

import android.app.Application
import com.github.barriosnahuel.vossosunboton.commons.android.error.Trackable
import com.squareup.leakcanary.AnalysisResult
import com.squareup.leakcanary.DisplayLeakService
import com.squareup.leakcanary.HeapDump
import com.squareup.leakcanary.LeakCanary
import kotlin.reflect.KProperty


object LeakCanaryConfigurator {

    private var tracker: Trackable? = null

    fun initializeWithDefaults(application: Application, tracker: Trackable) {
        LeakCanary.install(application)
        this.tracker = tracker
    }

    operator fun getValue(thisRef: MemoryLeakTrackerService, property: KProperty<*>): Trackable = tracker!!
}


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