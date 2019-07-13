package com.github.barriosnahuel.vossosunboton.model

import androidx.annotation.RawRes

data class Sound(val name: String?, val file: String?, @RawRes val rawRes: Int?) {
    constructor(name: String?, file: String?) : this(name, file, 0)
    constructor(name: String?, @RawRes rawRes: Int?) : this(name, null, rawRes)
}
