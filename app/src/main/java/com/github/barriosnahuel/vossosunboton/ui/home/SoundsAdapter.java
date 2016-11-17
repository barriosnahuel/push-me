package com.github.barriosnahuel.vossosunboton.ui.home;

import android.content.Context;
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
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import timber.log.Timber;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
/* default */ class SoundsAdapter extends RecyclerView.Adapter<SoundViewHolder> {

    @NonNull
    private final List<Sound> sounds;

    /* default */ SoundsAdapter(@NonNull final List<Sound> sounds) {
        this.sounds = sounds;
    }

    @Override
    public SoundViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        // create a new view
        final ToggleButton button =
            (ToggleButton) LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_button, parent, false);

        // set the view's size, margins, paddings and layout parameters

        // TODO: 11/16/16 Apply margin!
//        @dimen/material_horizontal_padding

        return new SoundViewHolder(button);
    }

    @Override
    public void onBindViewHolder(final SoundViewHolder holder, final int position) {
        final Sound sound = sounds.get(position);

        holder.toggleButton.setText(sound.getName());
        holder.toggleButton.setTextOff(sound.getName());

        final MediaPlayer mediaPlayer = new MediaPlayer();

        final String file = sound.getFile();

        PlaybackClickListener playbackClickListener = null;
        if (file == null) {
            playbackClickListener = new PlaybackClickListener(holder.toggleButton.getContext(), sound.getRawRes());
        } else {
            try {
                setMediaPlayerDataSource(holder.toggleButton.getContext(), mediaPlayer, file);
                mediaPlayer.prepare();
                playbackClickListener = new PlaybackClickListener(mediaPlayer);
            } catch (final Exception e) {
                Timber.e("Oops, you did it again... xD: %s", e.getMessage());
            }
        }

        // TODO: 11/16/16 What if listener is still null?
        holder.toggleButton.setOnClickListener(playbackClickListener);
    }

    @Override
    public int getItemCount() {
        return sounds.size();
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
    public static void setMediaPlayerDataSource(
        final Context context
        , final MediaPlayer mediaPlayer
        , String fileInfo) throws Exception {

        if (fileInfo.startsWith("content://")) {
            try {
                final Uri uri = Uri.parse(fileInfo);
                fileInfo = getRingtonePathFromContentUri(context, uri);
            } catch (Exception e) {
                Timber.e("Can't set MediaPlayer datasource: %s", e.getMessage());
            }
        }

        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                try {
                    setMediaPlayerDataSourcePreHoneyComb(mediaPlayer, fileInfo);
                } catch (Exception e) {
                    setMediaPlayerDataSourcePostHoneyComb(context, mediaPlayer, fileInfo);
                }
            } else {
                setMediaPlayerDataSourcePostHoneyComb(context, mediaPlayer, fileInfo);
            }
        } catch (Exception e) {
            try {
                setMediaPlayerDataSourceUsingFileDescriptor(mediaPlayer, fileInfo);
            } catch (Exception ee) {
                String uri = getRingtoneUriFromPath(context, fileInfo);
                mediaPlayer.reset();
                mediaPlayer.setDataSource(uri);
            }
        }
    }

    private static void setMediaPlayerDataSourcePreHoneyComb(final MediaPlayer mp, final String fileInfo)
        throws Exception {

        mp.reset();
        mp.setDataSource(fileInfo);
    }

    private static void setMediaPlayerDataSourcePostHoneyComb(final Context context, final MediaPlayer mp,
        final String fileInfo) throws Exception {

        mp.reset();
        mp.setDataSource(context, Uri.parse(Uri.encode(fileInfo)));
    }

    private static void setMediaPlayerDataSourceUsingFileDescriptor(final MediaPlayer mp, final String fileInfo)
        throws Exception {

        final File file = new File(fileInfo);
        FileInputStream inputStream = new FileInputStream(file);
        mp.reset();
        mp.setDataSource(inputStream.getFD());
        inputStream.close();
    }

    private static String getRingtoneUriFromPath(Context context, String path) {
        Uri ringtonesUri = MediaStore.Audio.Media.getContentUriForPath(path);
        Cursor
            ringtoneCursor = context.getContentResolver()
            .query(ringtonesUri, null, MediaStore.Audio.Media.DATA + "='" + path + "'", null, null);
        ringtoneCursor.moveToFirst();

        long id = ringtoneCursor.getLong(ringtoneCursor.getColumnIndex(MediaStore.Audio.Media._ID));
        ringtoneCursor.close();

        if (!ringtonesUri.toString().endsWith(String.valueOf(id))) {
            return ringtonesUri + "/" + id;
        }
        return ringtonesUri.toString();
    }

    public static String getRingtonePathFromContentUri(Context context,
        Uri contentUri) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor ringtoneCursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        ringtoneCursor.moveToFirst();

        String path = ringtoneCursor.getString(ringtoneCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

        ringtoneCursor.close();
        return path;
    }
}
