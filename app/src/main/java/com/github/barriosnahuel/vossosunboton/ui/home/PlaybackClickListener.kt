package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import android.widget.Checkable
import com.github.barriosnahuel.vossosunboton.feature.playback.PlayerControllerFactory
import com.github.barriosnahuel.vossosunboton.feature.playback.PlayerControllerListener
import com.github.barriosnahuel.vossosunboton.feature.playback.Reasons
import com.github.barriosnahuel.vossosunboton.model.Sound

internal class PlaybackClickListener(private val homeView: HomeView, private val sound: Sound) : View.OnClickListener {

    override fun onClick(v: View) {
        val button = v as Checkable

        if (button.isChecked) {
            // When here sound of the clicked view is off but mediaPlayer can be playing (or not)

            PlayerControllerFactory.instance.startPlayingSound(
                v.context,
                sound,
                object : PlayerControllerListener {
                    override fun onPlayerStop(intercept: Reasons) {
                        when (intercept) {
                            Reasons.INTERCEPT -> {
                                homeView.currentPlayingButton?.toggle()
                            }
                            Reasons.SOUND_END -> {
                                button.toggle()
                            }
                        }
                    }

                    override fun onPlayerStart() {
                        homeView.currentPlayingButton = button
                    }
                }
            )
        } else {
            // Here it should be playing

            PlayerControllerFactory.instance.stopPlayingSound()
        }
    }
}
