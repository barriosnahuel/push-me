package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import android.widget.ToggleButton
import com.github.barriosnahuel.vossosunboton.feature.playback.PlayerControllerFactory
import com.github.barriosnahuel.vossosunboton.model.Sound
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.Test

internal class PlaybackClickListenerTest {

    @Test
    fun `onClick on a view that is not pressed should start an audio button`() {
        val button = givenARecentCheckedView()

        whenClickingOn(button)

        thenItShouldStartPlayingAnAudio()
    }

    @Test
    fun `onClick on a view that is currently pressed should stop the audio button`() {
        val button = givenARecentUncheckedView()

        whenClickingOn(button)

        thenItShouldStopPlayingTheAudio()
    }

    private fun thenItShouldStartPlayingAnAudio() {
        verify(exactly = 1) { PlayerControllerFactory.instance.startPlayingSound(any(), any(), any()) }
    }

    private fun thenItShouldStopPlayingTheAudio() {
        verify(exactly = 1) { PlayerControllerFactory.instance.stopPlayingSound() }
    }

    private fun whenClickingOn(button: View) {
        mockkObject(PlayerControllerFactory)
        every { PlayerControllerFactory.instance.startPlayingSound(any(), any(), any()) } answers { nothing }
        every { PlayerControllerFactory.instance.stopPlayingSound() } answers { nothing }

        PlaybackClickListener(mockk(), Sound("a name", null)).onClick(button)
    }

    private fun givenARecentCheckedView(): View {
        val button = givenAView()
        every { button.isChecked } returns true
        return button
    }

    private fun givenARecentUncheckedView(): View {
        val button = givenAView()
        every { button.isChecked } returns false
        return button
    }

    private fun givenAView(): ToggleButton {
        val button = mockk<ToggleButton>()
        every { button.context } returns mockk()
        return button
    }
}