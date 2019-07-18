package com.github.barriosnahuel.vossosunboton.model

import androidx.annotation.RawRes

/**
 * Represents a sound (a.k.a. a button of this app).
 */
data class Sound(val name: String, val file: String?, @RawRes val rawRes: Int) {
    constructor(name: String, file: String?) : this(name, file, 0)
    constructor(name: String, @RawRes rawRes: Int = 0) : this(name, null, rawRes)
}
