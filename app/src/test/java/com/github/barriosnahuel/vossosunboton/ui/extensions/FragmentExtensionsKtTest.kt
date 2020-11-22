package com.github.barriosnahuel.vossosunboton.ui.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.verify
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class FragmentExtensionsKtTest {

    @Test
    fun `given a Fragment which is not added to a container when calling platform's tag() method it returns null`() {
        assertNull("It must be null by platform's definition", Fragment().tag)
    }

    @Test
    fun `given a Fragment which is not added to a container when calling extension getTagFromArguments() method it returns null`() {
        assertNull("It must be null because it's not loaded to any container", Fragment().getTagFromArguments())
    }

    @Test
    fun `given a Fragment when calling extension putTagInArguments() method it saves the tag to Fragment's arguments`() {
        val tagKey = "tag"
        val tagValue = "my custom tag"
        val fragment = Fragment()

        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putString(tagKey, tagValue) } returns Unit

        fragment.putTagInArguments(tagValue)

        verify(exactly = 1) { anyConstructed<Bundle>().putString(tagKey, tagValue) }

        val arguments = fragment.arguments
        assertNotNull("It must not be null because it must contains the tag", arguments)
    }
}