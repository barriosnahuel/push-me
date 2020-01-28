package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.commons.android.error.Tracker
import com.github.barriosnahuel.vossosunboton.commons.android.ui.Feedback
import com.github.barriosnahuel.vossosunboton.feature.share.ShareFeature
import com.github.barriosnahuel.vossosunboton.model.Sound
import timber.log.Timber

internal class ShareClickListener(private val sound: Sound) : View.OnLongClickListener {

    override fun onLongClick(view: View): Boolean {
        Timber.v("ShareClickListener#onLongClick")

        val shareFeature = ShareFeature.instance
        try {
            shareFeature.share(view.context, sound)
        } catch (e: IllegalStateException) {
            Feedback.send(view.context, R.string.not_yet_implemented_error)
            Tracker.track(Throwable("Audio button couldn't be shared.", e))
            return false
        }

        return true
    }
}
