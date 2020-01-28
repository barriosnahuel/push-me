package com.github.barriosnahuel.vossosunboton.feature.share

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.github.barriosnahuel.vossosunboton.BuildConfig
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.commons.file.getFile
import com.github.barriosnahuel.vossosunboton.model.Sound
import timber.log.Timber

internal interface ShareFeature {

    /**
     * @throws IllegalStateException when any required parameter is `null`
     */
    fun share(context: Context, sound: Sound)

    companion object {
        val instance: ShareFeature by lazy { ShareFeatureImpl() }
    }
}

private class ShareFeatureImpl : ShareFeature {

    private val authority = BuildConfig.APPLICATION_ID + ".fileprovider"

    /**
     * Check https://developer.android.com/training/secure-file-sharing/setup-sharing for more info.
     */
    override fun share(context: Context, sound: Sound) {
        checkNotNull(sound.file) { "File URI is mandatory" }

        Timber.d("Generating chooser Intent for button... %s: %s", sound.name, sound.file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "audio/*"

        val file = getFile(context, sound.file!!)
        val buttonFileContentUri: Uri?
        try {
            buttonFileContentUri = FileProvider.getUriForFile(context, authority, file)
        } catch (e: IllegalArgumentException) {
            throw IllegalStateException("Button content uri couldn't be created.", e)
        }

        Timber.d("Sharing... %s: %s", sound.name, sound.file)
        buttonFileContentUri.let {
            shareIntent.putExtra(Intent.EXTRA_STREAM, it)
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_chooser_title)))
        }
    }
}
