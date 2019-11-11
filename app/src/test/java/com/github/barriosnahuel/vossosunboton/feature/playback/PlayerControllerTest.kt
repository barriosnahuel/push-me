package com.github.barriosnahuel.vossosunboton.feature.playback

import android.media.MediaPlayer
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class PlayerControllerTest {

    @Before
    fun setup() {
        println("prueba mock constructor")
        mockkConstructor(MediaPlayer::class)
        println("prueba post mock constructor")
    }

    @Ignore("Because it passes when running only this type but fails when running the whole project.")
    @Test
    fun `on stopPlayingSound when media player is playing should stop it`() {
        givenAMediaPlayerCurrentlyPlayingASound()

        whenCallingPlayerControllerStop()

        thenItShouldStopMediaPlayer()
    }

    @After
    fun cleanup() {
        unmockkAll()
    }

    private fun givenAMediaPlayerCurrentlyPlayingASound() {
        println("prueba mock constructor")
        mockkConstructor(MediaPlayer::class)
        println("prueba post mock constructor")
        every { anyConstructed<MediaPlayer>().isPlaying } returns true
        every { anyConstructed<MediaPlayer>().stop() } answers { nothing }
    }

    private fun whenCallingPlayerControllerStop() {
        mockkObject(PlayerController.instance)
        PlayerController.instance.stopPlayingSound()
    }

    private fun thenItShouldStopMediaPlayer() {
        verifySequence {
            anyConstructed<MediaPlayer>().isPlaying
            anyConstructed<MediaPlayer>().stop()
        }
    }
}