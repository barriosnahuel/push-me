package com.github.barriosnahuel.vossosunboton.feature.playback

import android.content.Context
import com.github.barriosnahuel.vossosunboton.model.Sound

internal object PlayerControllerFactory {
    internal var instance: PlayerController = PlayerControllerImpl()
}

internal interface PlayerController {

    fun startPlayingSound(
        context: Context,
        sound: Sound,
        listener: PlayerControllerListener
    )

    fun stopPlayingSound()
}

internal interface PlayerControllerListener {
    fun onPlayerStop(intercept: Reasons)
    fun onPlayerStart()
}

/**
 * Enumerates the different reasons that can cause a player to be stoped.
 */
internal enum class Reasons {

    /**
     * Indicates that the player has been stoped because a new sound has to be started.
     */
    INTERCEPT,

    /**
     * Indicates that the player stoped by itself after playing the full length of the sound.
     * @see android.media.MediaPlayer.OnCompletionListener.onCompletion
     */
    SOUND_END
}
