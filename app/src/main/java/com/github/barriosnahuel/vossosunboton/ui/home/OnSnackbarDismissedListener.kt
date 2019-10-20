package com.github.barriosnahuel.vossosunboton.ui.home

import com.github.barriosnahuel.vossosunboton.model.Sound
import com.github.barriosnahuel.vossosunboton.model.data.manager.SoundDao
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

internal class OnSnackbarDismissedListener(private val view: HomeView, private val soundToRemove: Sound) : BaseTransientBottomBar.BaseCallback<Snackbar>() {

    override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
        super.onDismissed(transientBottomBar, event)

        if (event == DISMISS_EVENT_ACTION) return

        if (transientBottomBar == null) {
            Timber.e("Button won't be deleted. Can't get a valid Context to perform the operation.")
        } else {
            try {
                if (!SoundDao().delete(transientBottomBar.context, soundToRemove)) {
                    view.showGenericErrorFeedback()
                }
            } catch (e: IllegalStateException) {
                Timber.e("Button file couldn't be deleted. Button: %s", soundToRemove.name)
                view.showGenericErrorFeedback()
            }
        }
    }
}