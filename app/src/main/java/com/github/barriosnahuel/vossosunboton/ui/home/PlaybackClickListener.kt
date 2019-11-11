package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import android.widget.Checkable
import com.github.barriosnahuel.vossosunboton.feature.playback.PlayerController
import com.github.barriosnahuel.vossosunboton.model.Sound

internal class PlaybackClickListener(private val homeView: HomeView, private val sound: Sound) : View.OnClickListener {

    override fun onClick(v: View) {
        val button = v as Checkable

        if (button.isChecked) {
            // When here sound of the clicked view is off but mediaPlayer can be playing (or not)
            PlayerController.instance.startPlayingSound(
                    v.context,
                    sound,
                    button::toggle,
                    { homeView.currentPlayingButton?.toggle() },
                    { homeView.currentPlayingButton = button }
            )

        } else {
            // Here it should be playing
            PlayerController.instance.stopPlayingSound()
        }
    }
}