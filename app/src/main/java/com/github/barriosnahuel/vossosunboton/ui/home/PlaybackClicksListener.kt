package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import android.widget.ImageView
import com.github.barriosnahuel.vossosunboton.feature.playback.PlayerControllerFactory
import com.github.barriosnahuel.vossosunboton.feature.playback.PlayerControllerListener
import com.github.barriosnahuel.vossosunboton.model.Sound

interface PlaybackHandler {
    fun addOnClickListener(button: ImageView, sound: Sound)
}

internal enum class PlayerState {
    PLAY, PAUSE
}

internal class PlaybackClicksListener(private val homeView: HomeView) : View.OnClickListener, PlaybackHandler, PlayerControllerListener {

    init {
        PlayerControllerFactory.instance.setOnStartStopListener(this)
    }

    private val soundViewMapping: MutableMap<String, Pair<Sound, ImageView>> = mutableMapOf()

    override fun onClick(v: View) {
        val sound = soundViewMapping.filterValues { it.second == v }.entries.first().value.first
        if (sound.isPlaying) {
            // Clicked view should be playing

            PlayerControllerFactory.instance.stopPlayingSound()
        } else {
            // When here sound of the clicked view is off but mediaPlayer can be playing (or not)

            PlayerControllerFactory.instance.startPlayingSound(v.context, sound)
        }
    }

    override fun addOnClickListener(button: ImageView, sound: Sound) {
        soundViewMapping[sound.name] = Pair(sound, button)
        button.setOnClickListener(this)
    }

    override fun onPlayerStart(sound: Sound) {
        sound.isPlaying = true
        homeView.updatePlayerButton(PlayerState.PAUSE, soundViewMapping[sound.name]!!.second)
    }

    override fun onPlayerStop(sound: Sound) {
        sound.isPlaying = false
        homeView.updatePlayerButton(PlayerState.PLAY, soundViewMapping[sound.name]!!.second)
    }
}
