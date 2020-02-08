package com.github.barriosnahuel.vossosunboton.feature.playback

import android.content.Context
import android.media.MediaPlayer
import com.github.barriosnahuel.vossosunboton.commons.android.error.Tracker
import com.github.barriosnahuel.vossosunboton.model.Sound
import java.io.IOException

internal class PlayerControllerImpl(private val mediaPlayer: MediaPlayer = MediaPlayer()) : PlayerController {

    override fun startPlayingSound(
        context: Context,
        sound: Sound,
        listener: PlayerControllerListener
    ) {
        if (mediaPlayer.isPlaying) {
            // User clicked on a new button while still listening an audio, then we should toggle that running button.
            listener.onPlayerStop(Reasons.INTERCEPT)

            mediaPlayer.stop()
        }

        mediaPlayer.reset()

        val ready = if (sound.isBundled()) {
            MediaPlayerHelper.setupSoundSource(context, mediaPlayer, sound.rawRes)
        } else {
            MediaPlayerHelper.setupSoundSource(context, mediaPlayer, sound.file!!)
        }

        if (ready) {
            try {
                mediaPlayer.prepare()
            } catch (e: IOException) {
                Tracker.track(RuntimeException("Media player can't be prepared for playback.", e))
            }

            mediaPlayer.setOnCompletionListener { listener.onPlayerStop(Reasons.SOUND_END) }
            mediaPlayer.setOnSeekCompleteListener { it.pause() }

            listener.onPlayerStart()
            mediaPlayer.start()
        }
    }

    override fun stopPlayingSound() {
        // Here sound is on so we have to stop it.

        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }
}
