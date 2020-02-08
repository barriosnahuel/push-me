package com.github.barriosnahuel.vossosunboton

import com.github.barriosnahuel.vossosunboton.commons.android.error.Trackable
import leakcanary.DefaultOnHeapAnalyzedListener
import leakcanary.OnHeapAnalyzedListener
import shark.HeapAnalysis
import shark.HeapAnalysisFailure
import shark.HeapAnalysisSuccess
import kotlin.reflect.KProperty

internal object LeakCanaryConfigurator {

    private var tracker: Trackable? = null

    fun initializeWithDefaults(tracker: Trackable) {
        this.tracker = tracker
    }

    operator fun getValue(thisRef: MemoryLeakTrackerService, property: KProperty<*>): Trackable = tracker!!
}

/**
 * Custom service that reports memory leaks detected by LeakCanary to our error tracking tool.
 */
class MemoryLeakTrackerService : OnHeapAnalyzedListener {

    private val tracker: Trackable by LeakCanaryConfigurator

    private val defaultListener = DefaultOnHeapAnalyzedListener.create()

    override fun onHeapAnalyzed(heapAnalysis: HeapAnalysis) {
        when (heapAnalysis) {
            is HeapAnalysisSuccess -> {
                heapAnalysis
                        .allLeaks
                        .toList()
                        .flatMap { leak ->
                            leak.leakTraces.map { leakTrace -> leak to leakTrace }
                        }.forEach { (leak, _) ->
                            tracker.track(MemoryLeakException(leak.shortDescription))
                        }
            }
            is HeapAnalysisFailure -> {
                tracker.track(RuntimeException("Heap analysis failed"))
            }
        }

        // Delegate to default behavior (notification and saving result)
        defaultListener.onHeapAnalyzed(heapAnalysis)
    }
}

private class MemoryLeakException(message: String) : RuntimeException(message)