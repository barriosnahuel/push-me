package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.model.Sound
import com.google.android.material.snackbar.Snackbar

internal interface LandingView {
    val playbackClicksListener: PlaybackClicksListener

    fun currentView(): ViewGroup
    fun showDeleteButtonFeedback(soundsAdapter: SoundsAdapter, soundToRemove: Sound, position: Int)
    fun showFeatureNotImplementedFeedback()
    fun showGenericErrorFeedback()
    fun updatePlayerButton(state: PlayerState, button: ImageView)
}

internal class LandingViewImpl(private val parentView: ViewGroup) : LandingView {

    override val playbackClicksListener = PlaybackClicksListener(this)

    override fun showFeatureNotImplementedFeedback() {
        Snackbar
                .make(currentView(), R.string.app_feedback_share_generic_error, Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun showDeleteButtonFeedback(soundsAdapter: SoundsAdapter, soundToRemove: Sound, position: Int) {
        Snackbar
                .make(currentView(), R.string.app_feedback_button_deleted, Snackbar.LENGTH_LONG)
                .setAction(R.string.app_undo, UndoSwipeDismissListener(soundsAdapter, soundToRemove, position))
                .setActionTextColor(ContextCompat.getColor(currentView().context, R.color.app_white))
                .addCallback(OnSnackbarDismissedListener(this, soundToRemove))
                .show()
    }

    override fun showGenericErrorFeedback() {
        Snackbar
                .make(currentView(), R.string.app_feedback_generic_error_contact_support, Snackbar.LENGTH_LONG)
                .show()
    }

    override fun updatePlayerButton(state: PlayerState, button: ImageView) {
        val nextDrawableResId = when (state) {
            PlayerState.PLAY -> R.drawable.app_ic_play_arrow_black_24dp
            PlayerState.PAUSE -> R.drawable.app_ic_pause_black_24dp
        }

        button.setImageDrawable(parentView.resources.getDrawable(nextDrawableResId, parentView.context.theme))
    }

    override fun currentView(): ViewGroup = parentView
}
