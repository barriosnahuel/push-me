package com.github.barriosnahuel.vossosunboton.ui.home;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barriosnahuel.vossosunboton.BuildConfig;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.commons.android.ui.Feedback;
import com.github.barriosnahuel.vossosunboton.model.Sound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import timber.log.Timber;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
/* default */ class SoundsAdapter extends RecyclerView.Adapter<SoundViewHolder> {

    /**
     * Check https://developer.android.com/training/secure-file-sharing/setup-sharing for more info.
     */
    private static final String FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".fileprovider";
    private final int marginPx;

    @NonNull
    private final List<Sound> sounds;

    /**
     * The player. We're using just one for all sounds for better performance.
     */
    /* default */ final MediaPlayer mediaPlayer;

    /**
     * The view that is currently playing a {@link Sound}.
     */
    /* default */ Checkable currentlyPlaying;

    /* default */ SoundsAdapter(@NonNull final Resources resources, @NonNull final List<Sound> sounds) {
        this.sounds = sounds;
        marginPx = resources.getDimensionPixelSize(R.dimen.feature_base_material_horizontal_padding);
        mediaPlayer = new MediaPlayer();
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        final ToggleButton button =
                (ToggleButton) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button, parent, false);

        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) button.getLayoutParams();

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

        toggleButton.setOnLongClickListener(view -> {
            // TODO: 8/14/17 Decouple this!

            if (sound.getFile() == null) {
                Feedback.send(view.getContext(), R.string.not_yet_implemented_error);
                return true;
            }

            Timber.d("Sharing... %s: %s", sound.getName(), sound.getFile());

            final Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            final Uri uriForFile = FileProvider.getUriForFile(view.getContext(),
                    FILE_PROVIDER_AUTHORITY, new File(sound.getFile()));
            shareIntent.putExtra(Intent.EXTRA_STREAM, uriForFile);
            shareIntent.setType("audio/*");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            view.getContext().startActivity(
                Intent.createChooser(shareIntent, view.getContext().getString(R.string.share_chooser_title)));

            return true;
        });
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
         * @param fileInfo
         * @throws Exception
         */
        @SuppressWarnings("PMD.AvoidReassigningParameters")
        private void setMediaPlayerDataSource(
                @NonNull final Context context
            , @NonNull final MediaPlayer mediaPlayer
            , @Nullable String fileInfo
                , @RawRes final int rawResId) throws IOException {

            if (rawResId == 0) {
                if (fileInfo == null) {
                    throw new IllegalArgumentException("Either the sound Uri or the raw resource ID are required.");
                }

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
            } else {
                setMediaPlayerDataSourceRawRes(context, mediaPlayer, rawResId);
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
            final FileInputStream inputStream = new FileInputStream(file);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(inputStream.getFD());
            inputStream.close();
        }

        private String getRingtoneUriFromPath(@NonNull final Context context, @NonNull final String path) {
            final Uri ringtonesUri = MediaStore.Audio.Media.getContentUriForPath(path);
            final Cursor ringtoneCursor = context.getContentResolver()
                .query(ringtonesUri, null, MediaStore.Audio.Media.DATA + "='" + path + "'", null, null);
            ringtoneCursor.moveToFirst();

            final long id = ringtoneCursor.getLong(ringtoneCursor.getColumnIndex(MediaStore.Audio.Media._ID));
            ringtoneCursor.close();

            if (!ringtonesUri.toString().endsWith(String.valueOf(id))) {
                return ringtonesUri + "/" + id;
            }
            return ringtonesUri.toString();
        }

        private String getRingtonePathFromContentUri(@NonNull final Context context, @NonNull final Uri contentUri) {
            final String[] projection = { MediaStore.Audio.Media.DATA };
            final Cursor ringtoneCursor = context.getContentResolver().query(contentUri, projection, null, null, null);
            ringtoneCursor.moveToFirst();

            final String path =
                ringtoneCursor.getString(ringtoneCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

            ringtoneCursor.close();
            return path;
        }
    }
}
