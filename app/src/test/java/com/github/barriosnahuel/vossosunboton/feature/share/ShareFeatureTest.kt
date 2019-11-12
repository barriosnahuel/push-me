package com.github.barriosnahuel.vossosunboton.feature.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider
import com.github.barriosnahuel.vossosunboton.AbstractRobolectricTest
import com.github.barriosnahuel.vossosunboton.model.Sound
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.spyk
import org.junit.Test

internal class ShareFeatureTest : AbstractRobolectricTest() {

    @Test
    fun `share when sound Uri is null must throw an exception`() {
        val sound = givenASoundWithNullUri()

        try {
            whenSharingThe(sound)
        } catch (e: IllegalStateException) {
            thenWeThrowAnIllegalStateException(e)
        }
    }

    @Test
    fun `share when sound URI is valid show disambiguation window`() {
        val sound = givenASound()

        val generatedIntent = whenSharingThe(sound)

        thenOSShowDisambiguationWindow(generatedIntent)
    }

    @Test
    fun `share when sound URI is valid sends the audio file`() {
        val sound = givenASound()

        val generatedIntent = whenSharingThe(sound)

        thenWeSendAnAudio(generatedIntent)
    }

    private fun givenASound() = Sound("my button name", "a/dummy/sound/uri")

    private fun givenASoundWithNullUri() = Sound("my button name")

    private fun whenSharingThe(sound: Sound): Intent {
        val mockedContext = spyk<Context>(ApplicationProvider.getApplicationContext<Context>())

        mockkStatic(FileProvider::class)
        every { FileProvider.getUriForFile(mockedContext, any(), any()) } returns Uri.EMPTY

        val slot = slot<Intent>()
        every { mockedContext.startActivity(capture(slot)) } answers { nothing }

        ShareFeature.instance.share(mockedContext, sound)

        return slot.captured
    }

    private fun thenWeThrowAnIllegalStateException(e: IllegalStateException) {
        assertThat(e.message).isEqualTo("File URI is mandatory")
    }

    private fun thenOSShowDisambiguationWindow(intent: Intent) {
        assertThat(intent.action).isEqualTo(Intent.ACTION_CHOOSER)
        assertThat(intent.extras?.containsKey(Intent.EXTRA_INTENT)).isTrue()
    }

    private fun thenWeSendAnAudio(intent: Intent) {
        val shareIntent = intent.getParcelableExtra<Intent>(Intent.EXTRA_INTENT)

        assertThat(shareIntent?.action).isEqualTo(Intent.ACTION_SEND)
        assertThat(shareIntent?.type).isEqualTo("audio/*")
        assertThat(shareIntent?.extras?.containsKey(Intent.EXTRA_STREAM)).isTrue()
    }
}