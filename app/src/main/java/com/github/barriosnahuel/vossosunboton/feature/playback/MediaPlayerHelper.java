package com.github.barriosnahuel.vossosunboton.feature.playback;

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

import java.io.FileDescriptor;
import java.io.IOException;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import timber.log.Timber;

public final class MediaPlayerHelper {

    private MediaPlayerHelper() {
        // Do nothing.
    }

    /**
     * @param context     The execution context.
     * @param mediaPlayer The media player to setup.
     * @param file        The path of the sound's file.
     * @return <code>true</code> when call to {@link MediaPlayer#setDataSource(Context, Uri)} is ok.
     */
    public static boolean setupSoundSource(@NonNull final Context context,
                                           @NonNull final MediaPlayer mediaPlayer,
                                           @NonNull final String file) throws IOException {

        Uri soundFileUri = Uri.fromFile(FileUtils.getFile(context, file));
        if (ContentResolver.SCHEME_CONTENT.equals(soundFileUri.getScheme())) {
            Timber.d("Sound uri is a content uri");

            soundFileUri = getSoundPath(context, soundFileUri);
        }

        mediaPlayer.reset();
        mediaPlayer.setDataSource(context, soundFileUri);
        return true;
    }

    /**
     * @param context     The execution context.
     * @param mediaPlayer The media player to setup.
     * @param rawResId    The ID of the raw resource to link to the media player.
     * @return <code>true</code> when call to {@link MediaPlayer#setDataSource(FileDescriptor, long, long)} is ok.
     */
    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", justification = "It throws false-positives when using try-with-resources https://github.com/spotbugs/spotbugs/issues/756")
    public static boolean setupSoundSource(@NonNull final Context context,
                                           @NonNull final MediaPlayer mediaPlayer,
                                           @RawRes final int rawResId) throws IOException {

        if (rawResId == 0) {
            Tracker.INSTANCE.track(new RuntimeException("Bundled sound identified but no raw resource ID provided. Value can't be 0."));
            return false;
        }

        try (AssetFileDescriptor fileDescriptor = context.getResources().openRawResourceFd(rawResId)) {
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
            return true;
        }
    }

    @SuppressFBWarnings(value = "RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", justification = "It throws false-positives when using try-with-resources https://github.com/spotbugs/spotbugs/issues/756")
    private static Uri getSoundPath(@NonNull final Context context, @NonNull final Uri contentUriSource) {
        final String[] projection = {MediaStore.Audio.Media.DATA};
        final String path;
        try (Cursor cursor = context.getContentResolver().query(contentUriSource, projection, null, null, null)) {
            cursor.moveToFirst();

            path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        }

        return Uri.parse(path);
    }
}
