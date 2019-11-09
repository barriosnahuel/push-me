package com.github.barriosnahuel.vossosunboton.feature.share

import android.content.Context
import com.github.barriosnahuel.vossosunboton.model.Sound
import com.google.common.truth.Truth
import org.junit.Test
import org.mockito.Mockito.mock

internal class ShareFeatureImplTest {

    @Test
    fun `share when sound URI is null must throw an exception`() {
        val sound = givenASoundWithNullUri()

        try {
            whenTryingToShareIt(sound)
        } catch (e: IllegalStateException) {
            thenWeThrowAnIllegalStateException(e)
        }
    }

    private fun givenASoundWithNullUri() = Sound("name")

    private fun whenTryingToShareIt(sound: Sound) {
        ShareFeature.instance.share(mock(Context::class.java), sound)
    }

    private fun thenWeThrowAnIllegalStateException(e: IllegalStateException) {
        Truth.assertThat(e.message).isEqualTo("File URI is mandatory")
    }
}