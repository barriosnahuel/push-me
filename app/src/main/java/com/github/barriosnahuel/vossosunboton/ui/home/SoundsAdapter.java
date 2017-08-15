package com.github.barriosnahuel.vossosunboton.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ToggleButton;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.data.model.Sound;
import hugo.weaving.DebugLog;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import timber.log.Timber;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
/* default */ class SoundsAdapter extends RecyclerView.Adapter<SoundViewHolder> {

    private final int marginPx;

    @NonNull
    private final List<Sound> sounds;

    /* default */ SoundsAdapter(@NonNull final Resources resources, @NonNull final List<Sound> sounds) {
        this.sounds = sounds;
        marginPx = resources.getDimensionPixelSize(R.dimen.material_horizontal_padding);
    }

    @Override
    public SoundViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final ToggleButton button =
            (ToggleButton) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button, parent, false);

        final RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) button.getLayoutParams();

        layoutParams.leftMargin = marginPx;
        layoutParams.rightMargin = marginPx;

        return new SoundViewHolder(button);
    }

    @DebugLog
    @Override
    public void onBindViewHolder(final SoundViewHolder holder, final int position) {
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

        final MediaPlayer mediaPlayer = new MediaPlayer();

        final String file = sound.getFile();

        PlaybackClickListener playbackClickListener = null;
        if (file == null) {
            playbackClickListener = new PlaybackClickListener(toggleButton.getContext(), sound.getRawRes());
        } else {
            try {
                setMediaPlayerDataSource(toggleButton.getContext(), mediaPlayer, file);
                mediaPlayer.prepare();
                playbackClickListener = new PlaybackClickListener(mediaPlayer);
            } catch (final Exception e) {
                Timber.e("Oops, you did it again... xD: %s", e.getMessage());
            }
        }

        // TODO: 11/16/16 What if listener is still null?
        toggleButton.setOnClickListener(playbackClickListener);

        toggleButton.setOnLongClickListener(view -> {
            // TODO: 8/14/17 Decouple this!

            Timber.d("Sharing... %s: %s", sound.getName(), sound.getFile());

            final Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(sound.getFile()));
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
     * // TODO: 11/16/16 Resolve this PMD warning!
     *
     * @param context
     * @param mediaPlayer
     * @param fileInfo
     * @throws Exception
     */
    @SuppressWarnings("PMD.AvoidReassigningParameters")
    private static void setMediaPlayerDataSource(
        final Context context
        , final MediaPlayer mediaPlayer
        , String fileInfo) throws Exception {

        if (fileInfo.startsWith("content://")) {
            try {
                final Uri uri = Uri.parse(fileInfo);
                fileInfo = getRingtonePathFromContentUri(context, uri);
            } catch (final Exception e) {
                Timber.e("Can't set MediaPlayer datasource: %s", e.getMessage());
            }
        }

        try {
            setMediaPlayerDataSourcePostHoneyComb(context, mediaPlayer, fileInfo);
        } catch (final Exception e) {
            try {
                setMediaPlayerDataSourceUsingFileDescriptor(mediaPlayer, fileInfo);
            } catch (final Exception ee) {
                final String uri = getRingtoneUriFromPath(context, fileInfo);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(uri);
            }
        }
    }

    @DebugLog
    private static void setMediaPlayerDataSourcePostHoneyComb(final Context context, final MediaPlayer mediaPlayer,
        final String fileInfo) throws Exception {

        mediaPlayer.reset();
        mediaPlayer.setDataSource(context, Uri.parse(Uri.encode(fileInfo)));
    }

    @DebugLog
    private static void setMediaPlayerDataSourceUsingFileDescriptor(final MediaPlayer mediaPlayer, final String fileInfo)
        throws Exception {

        final File file = new File(fileInfo);
        final FileInputStream inputStream = new FileInputStream(file);
        mediaPlayer.reset();
        mediaPlayer.setDataSource(inputStream.getFD());
        inputStream.close();
    }

    @DebugLog
    private static String getRingtoneUriFromPath(final Context context, final String path) {
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

    @DebugLog
    private static String getRingtonePathFromContentUri(final Context context, final Uri contentUri) {
        final String[] projection = { MediaStore.Audio.Media.DATA };
        final Cursor ringtoneCursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        ringtoneCursor.moveToFirst();

        final String path = ringtoneCursor.getString(ringtoneCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

        ringtoneCursor.close();
        return path;
    }
}
