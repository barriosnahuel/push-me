package com.github.barriosnahuel.vossosunboton.feature.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.github.barriosnahuel.vossosunboton.BuildConfig
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.model.Sound
import timber.log.Timber
import java.io.File

internal interface ShareFeature {

    fun share(context: Context, sound: Sound)

    companion object {
        fun get(): ShareFeature = ShareFeatureImpl()
    }
}

private class ShareFeatureImpl : ShareFeature {

    /**
     * Check https://developer.android.com/training/secure-file-sharing/setup-sharing for more info.
     */
    private val authority = BuildConfig.APPLICATION_ID + ".fileprovider"

    override fun share(context: Context, sound: Sound) {
        checkNotNull(sound.file) { "File URI is mandatory" }

        Timber.d("Sharing... %s: %s", sound.name, sound.file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "audio/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(context, authority, File(sound.file)))
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_chooser_title)))
    }
}