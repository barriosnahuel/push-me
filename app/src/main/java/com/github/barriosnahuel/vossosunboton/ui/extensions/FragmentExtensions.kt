package com.github.barriosnahuel.vossosunboton.ui.extensions

import android.os.Bundle
import androidx.fragment.app.Fragment

private const val ARGUMENT_TAG_VALUE = "tag"

internal fun Fragment.getTagFromArguments(): String? = arguments?.getString(ARGUMENT_TAG_VALUE)

internal fun Fragment.putTagInArguments(tag: String) {
    arguments = (arguments ?: Bundle()).apply {
        putString(ARGUMENT_TAG_VALUE, tag)
    }
}
