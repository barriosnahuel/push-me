package com.github.barriosnahuel.vossosunboton.feature.playback

import android.content.Context
import com.github.barriosnahuel.vossosunboton.model.Sound

internal object PlayerControllerFactory {
    internal var instance: PlayerController = PlayerControllerImpl()
}

internal interface PlayerController {

    fun startPlayingSound(
        context: Context,
        sound: Sound
    )

    fun stopPlayingSound()

    fun setOnStartStopListener(listener: PlayerControllerListener)
}

internal interface PlayerControllerListener {
    fun onPlayerStop(sound: Sound)
    fun onPlayerStart(sound: Sound)
}
