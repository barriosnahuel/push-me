package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.ViewGroup
import android.widget.Checkable
import androidx.core.content.ContextCompat
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.model.Sound
import com.google.android.material.snackbar.Snackbar

internal interface HomeView {
    fun currentView(): ViewGroup
    fun showDeleteButtonFeedback(soundsAdapter: SoundsAdapter, soundToRemove: Sound, position: Int)
    fun showFeatureNotImplementedFeedback()
    fun showGenericErrorFeedback()

    /**
     * The view that is currently playing a [Sound].
     */
    var currentPlayingButton: Checkable?
}

internal class HomeViewImpl(private val parentView: ViewGroup, override var currentPlayingButton: Checkable? = null) : HomeView {

    override fun showFeatureNotImplementedFeedback() {
        Snackbar
                .make(currentView(), R.string.app_feedback_custom_buttons_cant_be_deleted_yet, Snackbar.LENGTH_SHORT)
                .show()
    }

    override fun showDeleteButtonFeedback(soundsAdapter: SoundsAdapter, soundToRemove: Sound, position: Int) {
        Snackbar
                .make(currentView(), R.string.app_button_deleted, Snackbar.LENGTH_LONG)
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

    override fun currentView(): ViewGroup = parentView
}
