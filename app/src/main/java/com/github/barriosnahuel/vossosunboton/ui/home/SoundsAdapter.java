package com.github.barriosnahuel.vossosunboton.ui.home;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.commons.android.error.Tracker;
import com.github.barriosnahuel.vossosunboton.feature.playback.MediaPlayerHelper;
import com.github.barriosnahuel.vossosunboton.model.Sound;
import com.github.barriosnahuel.vossosunboton.model.data.manager.SoundDao;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
/* default */ class SoundsAdapter extends RecyclerView.Adapter<SoundViewHolder> {

    @NonNull
    private final HomeView homeView;

    @NonNull
    private final List<Sound> sounds;

    /**
     * The player. We're using just one for all sounds for better performance.
     */
    /* default */ final MediaPlayer mediaPlayer;

    private int marginPx = -1;

    /**
     * The view that is currently playing a {@link Sound}.
     */
    /* default */ Checkable currentlyPlaying;

    /* default */ SoundsAdapter(@NonNull final HomeView homeView) {
        this.sounds = new SoundDao().find(homeView.currentView().getContext());
        this.homeView = homeView;
        mediaPlayer = new MediaPlayer();
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final ToggleButton button =
                (ToggleButton) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button, parent, false);

        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) button.getLayoutParams();

        if (marginPx == -1) {
            marginPx = parent.getResources().getDimensionPixelSize(R.dimen.feature_base_material_horizontal_padding);
        }

        layoutParams.leftMargin = marginPx;
        layoutParams.rightMargin = marginPx;

        return new SoundViewHolder(button);
    }

    @Override
    public void onBindViewHolder(@NonNull final SoundViewHolder holder, final int position) {
        final ToggleButton toggleButton = holder.toggleButton;

        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) toggleButton.getLayoutParams();
        if (isFirst(position)) {
            layoutParams.topMargin = marginPx;
            layoutParams.bottomMargin = 0;
        } else if (isLast(position)) {
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = marginPx;
        } else {
            layoutParams.topMargin = 0;
            layoutParams.bottomMargin = 0;
        }

        final Sound sound = sounds.get(position);

        toggleButton.setText(sound.getName());
        toggleButton.setTextOff(sound.getName());

        toggleButton.setOnClickListener(new PlaybackClickListener(sound));
        toggleButton.setOnLongClickListener(new ShareClickListener(sound));
    }

    @Override
    public int getItemCount() {
        return sounds.size();
    }

    private boolean isFirst(final int position) {
        return position == 0;
    }

    private boolean isLast(final int position) {
        return getItemCount() - 1 == position;
    }

    /**
     * @param position position in the adapter of the item to remove.
     */
    public void remove(final int position) {
        final Sound soundToRemove = sounds.get(position);

        sounds.remove(position);
        notifyItemRemoved(position);

        if (soundToRemove.isBundled()) {
            Timber.w("Delete feature for bundled buttons is not yet released, button won't be deleted. Button: %s", soundToRemove.getName());
            homeView.showFeatureNotImplementedFeedback();
        } else {
            homeView.showDeleteButtonFeedback(this, soundToRemove, position);
        }
    }

    public void restore(@NonNull final Sound removedSound, final int originalPosition) {
        sounds.add(originalPosition, removedSound);
        notifyItemInserted(originalPosition);
    }

    /**
     * Created by Nahuel Barrios on 11/16/16.
     */
    /* default */ class PlaybackClickListener implements View.OnClickListener {

        private final Sound sound;

        /* default */ PlaybackClickListener(final Sound sound) {
            this.sound = sound;
        }

        @Override
        public void onClick(final View v) {
            final Checkable button = (ToggleButton) v;

            if (button.isChecked()) {
                // When here, sound of the clicked view is off

                if (mediaPlayer.isPlaying()) {
                    // User clicked on a new button while still listening an audio, then we should toggle that running button.
                    currentlyPlaying.toggle();

                    mediaPlayer.stop();
                }

                mediaPlayer.reset();

                final boolean ready;
                if (sound.isBundled()) {
                    ready = MediaPlayerHelper.setupSoundSource(v.getContext(), mediaPlayer, sound.getRawRes());
                } else {
                    ready = MediaPlayerHelper.setupSoundSource(v.getContext(), mediaPlayer, sound.getFile());
                }

                if (ready) {
                    try {
                        mediaPlayer.prepare();
                    } catch (final IOException e) {
                        Tracker.INSTANCE.track(new RuntimeException("Media player can't be prepared for playback.", e));
                    }

                    mediaPlayer.setOnCompletionListener(mediaPlayer -> button.toggle());
                    mediaPlayer.setOnSeekCompleteListener(MediaPlayer::pause);

                    currentlyPlaying = button;
                    mediaPlayer.start();
                }

            } else {
                // But here, sound is on.

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        }
    }
}
