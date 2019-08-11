package com.github.barriosnahuel.vossosunboton.ui.home

import android.content.Intent
import android.view.View
import androidx.core.content.FileProvider
import com.github.barriosnahuel.vossosunboton.BuildConfig
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.commons.android.ui.Feedback
import com.github.barriosnahuel.vossosunboton.model.Sound
import timber.log.Timber
import java.io.File

internal class ShareClickListener(private val sound: Sound) : View.OnLongClickListener {

    /**
     * Check https://developer.android.com/training/secure-file-sharing/setup-sharing for more info.
     */
    private val authority = BuildConfig.APPLICATION_ID + ".fileprovider"

    override fun onLongClick(view: View): Boolean {
        Timber.v("ShareClickListener#onLongClick")

        if (sound.file == null) {
            Feedback.send(view.context, R.string.not_yet_implemented_error)
            return true
        }

        Timber.d("Sharing... %s: %s", sound.name, sound.file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "audio/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(view.context, authority, File(sound.file)))
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        view.context.startActivity(Intent.createChooser(shareIntent, view.context.getString(R.string.share_chooser_title)))

        return true
    }

}
