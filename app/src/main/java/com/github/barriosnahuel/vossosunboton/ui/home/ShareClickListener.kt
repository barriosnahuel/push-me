package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.commons.android.error.Tracker
import com.github.barriosnahuel.vossosunboton.commons.android.ui.Feedback
import com.github.barriosnahuel.vossosunboton.feature.share.ShareFeature
import com.github.barriosnahuel.vossosunboton.model.Sound
import timber.log.Timber

internal class ShareClickListener(private val sound: Sound) : View.OnClickListener {

    override fun onClick(view: View) {
        Timber.v("ShareClickListener#onLongClick")

        val shareFeature = ShareFeature.instance
        try {
            shareFeature.share(view.context, sound)
        } catch (e: IllegalStateException) {
            Feedback.send(view.context, R.string.app_feedback_share_generic_error)
            Tracker.track(Throwable("Audio button couldn't be shared.", e))
        }
    }
}
