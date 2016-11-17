package com.github.barriosnahuel.vossosunboton.feature.home;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.view.View;
import android.widget.ToggleButton;
import timber.log.Timber;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
/* default */ class PlaybackClickListener implements View.OnClickListener {

    private final MediaPlayer mediaPlayer;

    /**
     * Package-protected because method is used from an inner/anonymous class.
     */
    /* default */ ToggleButton button;

    /* default */ PlaybackClickListener(final MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

        if (this.mediaPlayer == null) {
            Timber.e("Can't create media player for the specified resource");
        } else {
            this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(final MediaPlayer mp) {
                    button.toggle();
                }
            });
            this.mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(final MediaPlayer mp) {
                    mp.pause();
                }
            });
        }
    }

    /* default */ PlaybackClickListener(@NonNull final Context context, @RawRes final int audioRes) {
        this(MediaPlayer.create(context, audioRes));
    }

    @Override
    public void onClick(final View v) {

        if (v instanceof ToggleButton) {
            button = (ToggleButton) v;
            if (mediaPlayer == null) {
                return;
            }

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(0);
            } else {
                mediaPlayer.start();
            }
        } else {
            Timber.e("Layout has changed... wrong!");
        }
    }
}