package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.commons.android.ui.Feedback
import com.github.barriosnahuel.vossosunboton.feature.share.ShareFeature
import com.github.barriosnahuel.vossosunboton.model.Sound
import timber.log.Timber

internal class ShareClickListener(private val sound: Sound) : View.OnLongClickListener {

    override fun onLongClick(view: View): Boolean {
        Timber.v("ShareClickListener#onLongClick")

        val share = ShareFeature.get()
        try {
            share.share(view.context, sound)
        } catch (e: IllegalStateException) {
            Feedback.send(view.context, R.string.not_yet_implemented_error)
            return false
        }

        return true
    }

}
