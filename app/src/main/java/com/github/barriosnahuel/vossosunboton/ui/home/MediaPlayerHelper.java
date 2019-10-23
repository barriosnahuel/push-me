package com.github.barriosnahuel.vossosunboton.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;

import com.github.barriosnahuel.vossosunboton.commons.android.error.Tracker;
import com.github.barriosnahuel.vossosunboton.commons.file.FileUtils;

import java.io.IOException;

import timber.log.Timber;

/* default */ final class MediaPlayerHelper {

    private MediaPlayerHelper() {
        // Do nothing.
    }

    /* default */ static boolean setupMediaPlayer(@NonNull final Context context,
                                    @NonNull final MediaPlayer mediaPlayer,
                                    @NonNull final String file) {
        String soundFilePath = FileUtils.getFile(context, file).toString();

        if (soundFilePath.startsWith(ContentResolver.SCHEME_CONTENT + "://")) {
            Timber.d("Sound uri is a content uri");

            soundFilePath = getSoundPath(context, Uri.parse(soundFilePath));
        }

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(context, Uri.parse(soundFilePath));
            return true;
        } catch (final IOException e) {
            Tracker.INSTANCE.track(new RuntimeException("User custom button is not playable", e));
        }

        return false;
    }

    /* default */ static boolean setupSoundSource(@NonNull final Context context,
                                    @NonNull final MediaPlayer mediaPlayer,
                                    @RawRes final int rawResId) {

        if (rawResId == 0) {
            Tracker.INSTANCE.track(new RuntimeException("Bundled sound identified but no raw resource ID provided. Value can't be 0."));
            return false;
        }

        try (AssetFileDescriptor fileDescriptor = context.getResources().openRawResourceFd(rawResId)) {
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            return true;
        } catch (final IOException e) {
            Tracker.INSTANCE.track(new RuntimeException("Bundled button is not playable", e));
        }

        return false;
    }

    private static String getSoundPath(@NonNull final Context context, @NonNull final Uri contentUriSource) {
        final String[] projection = {MediaStore.Audio.Media.DATA};
        final String path;
        try (Cursor cursor = context.getContentResolver().query(contentUriSource, projection, null, null, null)) {
            cursor.moveToFirst();

            path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        }
        return path;
    }
}
