package com.github.barriosnahuel.vossosunboton.ui.home

import android.widget.ImageView
import com.github.barriosnahuel.vossosunboton.feature.playback.PlayerControllerFactory
import com.github.barriosnahuel.vossosunboton.model.Sound
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test

internal class PlaybackClicksListenerTest {

    @Test
    fun `onClick on a view that is not pressed should start an audio button`() {
        val soundView = givenARecentClickedStoppedSoundView()

        whenClickingOn(soundView)

        thenItShouldStartPlayingAnAudio(soundView.first)
    }

    @Test
    fun `onClick on a view that is currently pressed should stop the audio button`() {
        val soundView = givenARecentClickedPlayingSoundView()

        whenClickingOn(soundView)

        thenItShouldStopPlayingTheAudio()
    }

    private fun thenItShouldStartPlayingAnAudio(soundToPlay: Sound) {
        val playerController = PlayerControllerFactory.instance

        verify(exactly = 1) { playerController.setOnStartStopListener(any()) }
        verify(exactly = 1) { playerController.startPlayingSound(any(), soundToPlay) }
    }

    private fun thenItShouldStopPlayingTheAudio() {
        val playerController = PlayerControllerFactory.instance

        verify(exactly = 1) { playerController.setOnStartStopListener(any()) }
        verify(exactly = 1) { playerController.stopPlayingSound() }
    }

    private fun whenClickingOn(soundView: Pair<Sound, ImageView>) {
        mockkObject(PlayerControllerFactory)

        every { PlayerControllerFactory.instance.setOnStartStopListener(any()) } answers { nothing }
        val listener = PlaybackClicksListener(mockk())

        every { soundView.second.setOnClickListener(any()) } answers { nothing }
        listener.addOnClickListener(soundView.second, soundView.first)

        every { PlayerControllerFactory.instance.startPlayingSound(any(), soundView.first) } answers { nothing }
        every { PlayerControllerFactory.instance.stopPlayingSound() } answers { nothing }
        listener.onClick(soundView.second)
    }

    private fun givenARecentClickedStoppedSoundView(): Pair<Sound, ImageView> {
        val button = givenAView()
        val sound = givenAStoppedSound()
        return Pair(sound, button)
    }

    private fun givenAStoppedSound(): Sound = Sound("cool button")

    private fun givenAPlayingSound(): Sound {
        val sound = givenAStoppedSound()
        sound.isPlaying = true
        return sound
    }

    private fun givenARecentClickedPlayingSoundView(): Pair<Sound, ImageView> {
        val button = givenAView()
        val sound = givenAPlayingSound()
        return Pair(sound, button)
    }

    private fun givenAView(): ImageView {
        val button = mockk<ImageView>()
        every { button.context } returns mockk()

        return button
    }
}
