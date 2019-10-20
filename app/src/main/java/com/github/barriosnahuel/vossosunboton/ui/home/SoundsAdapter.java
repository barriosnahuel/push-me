package com.github.barriosnahuel.vossosunboton.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.commons.file.FileUtils;
import com.github.barriosnahuel.vossosunboton.model.Sound;
import com.github.barriosnahuel.vossosunboton.model.data.manager.SoundDao;

import java.io.File;
import java.io.FileInputStream;
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

                try {
                    setMediaPlayerDataSource(v.getContext(), mediaPlayer, sound.getFile(), sound.getRawRes());
                    mediaPlayer.prepare();
                } catch (final IllegalStateException | IOException e) {
                    Timber.e("Oops, you did it again... xD: %s", e.getMessage());
                }

                mediaPlayer.setOnCompletionListener(mediaPlayer -> button.toggle());
                mediaPlayer.setOnSeekCompleteListener(MediaPlayer::pause);

                currentlyPlaying = button;
                mediaPlayer.start();
            } else {
                // But here, sound for is on.

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
            }
        }

        /**
         * // TODO: 11/16/16 Resolve this PMD warning!
         *
         * @param context
         * @param mediaPlayer
         * @param file
         * @throws Exception
         */
        @SuppressWarnings("PMD.AvoidReassigningParameters")
        private void setMediaPlayerDataSource(
                @NonNull final Context context
                , @NonNull final MediaPlayer mediaPlayer
                , @Nullable final String file
                , @RawRes final int rawResId) throws IOException {

            if (file == null) {
                if (rawResId == 0) {
                    throw new IllegalStateException("Either the sound Uri or the raw resource ID are required.");
                }

                setMediaPlayerDataSourceRawRes(context, mediaPlayer, rawResId);
            } else {
                String fileInfo = FileUtils.getFile(context, file).toString();

                if (fileInfo.startsWith(ContentResolver.SCHEME_CONTENT + "://")) {
                    final Uri uri = Uri.parse(fileInfo);
                    fileInfo = getRingtonePathFromContentUri(context, uri);
                }

                try {
                    setMediaPlayerDataSourcePostHoneyComb(context, mediaPlayer, fileInfo);
                } catch (final IllegalStateException | IOException | IllegalArgumentException | SecurityException e) {
                    try {
                        setMediaPlayerDataSourceUsingFileDescriptor(mediaPlayer, fileInfo);
                    } catch (final SecurityException | IllegalStateException | IllegalArgumentException | IOException ee) {
                        final String uri = getRingtoneUriFromPath(context, fileInfo);
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(uri);
                    }
                }
            }
        }

        private void setMediaPlayerDataSourceRawRes(@NonNull final Context context,
                                                    @NonNull final MediaPlayer mediaPlayer,
                                                    @RawRes final int rawResId) {

            try (AssetFileDescriptor fileDescriptor = context.getResources().openRawResourceFd(rawResId)) {
                mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                        fileDescriptor.getLength());
            } catch (final IOException e) {
                Timber.e("Can't set data source from raw resource: %s", e.getMessage());
            }
        }

        private void setMediaPlayerDataSourcePostHoneyComb(@NonNull final Context context,
                                                           @NonNull final MediaPlayer mediaPlayer,
                                                           @NonNull final String fileInfo) throws IOException {

            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, Uri.parse(Uri.encode(fileInfo)));
        }

        /**
         * TODO: Stop ignoring this! Currently ignored because I'm upgrading dependencies version.
         */
        @SuppressWarnings("PMD.AvoidFileStream")
        private void setMediaPlayerDataSourceUsingFileDescriptor(@NonNull final MediaPlayer mediaPlayer, @NonNull final String fileInfo)
                throws IOException {

            final File file = new File(fileInfo);
            try (FileInputStream inputStream = new FileInputStream(file)) {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(inputStream.getFD());
            }
        }

        private String getRingtoneUriFromPath(@NonNull final Context context, @NonNull final String path) {
            final Uri ringtonesUri = MediaStore.Audio.Media.getContentUriForPath(path);
            final long id;
            try (Cursor ringtoneCursor = context.getContentResolver()
                    .query(ringtonesUri, null, MediaStore.Audio.Media.DATA + "='" + path + "'", null, null)) {
                ringtoneCursor.moveToFirst();

                id = ringtoneCursor.getLong(ringtoneCursor.getColumnIndex(MediaStore.Audio.Media._ID));
            }

            if (!ringtonesUri.toString().endsWith(String.valueOf(id))) {
                return ringtonesUri + "/" + id;
            }
            return ringtonesUri.toString();
        }

        private String getRingtonePathFromContentUri(@NonNull final Context context, @NonNull final Uri contentUri) {
            final String[] projection = { MediaStore.Audio.Media.DATA };
            final String path;
            try (Cursor ringtoneCursor = context.getContentResolver().query(contentUri, projection, null, null, null)) {
                ringtoneCursor.moveToFirst();

                path = ringtoneCursor.getString(ringtoneCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            }
            return path;
        }
    }
}
