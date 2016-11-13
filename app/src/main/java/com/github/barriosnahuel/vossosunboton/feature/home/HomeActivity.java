package com.github.barriosnahuel.vossosunboton.feature.home;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ToggleButton;
import com.github.barriosnahuel.vossosunboton.AbstractActivity;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.feature.addbutton.Sound;
import com.github.barriosnahuel.vossosunboton.feature.addbutton.SoundDao;
import java.io.File;
import java.io.FileInputStream;
import java.util.Set;
import timber.log.Timber;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with
 * user interaction.
 */
public class HomeActivity extends AbstractActivity {

    private LinearLayout buttonsContainer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        populateGrid();
    }

    /**
     * We don't want to open this activity again (default behavior at {@link AbstractActivity}) when the user taps on
     * the home icon.
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return true;
    }

    private void populateGrid() {
        buttonsContainer = (LinearLayout) findViewById(R.id.buttons_container);

        addCustomButtons();
        addDefaultButtons();
    }

    private void addCustomButtons() {
        SoundDao soundsDao = new SoundDao();
        Set<Sound> sounds = soundsDao.find(this);

        for (Sound eachSound : sounds) {
            MediaPlayer mediaPlayer = new MediaPlayer();

            try {
                setMediaPlayerDataSource(this, mediaPlayer, eachSound.getFile());
                mediaPlayer.prepare();
                addButton(eachSound.getName(), new MyClickListener(mediaPlayer));
            } catch (final Exception e) {
                Timber.e("Oops, you did it again... xD: %s", e.getMessage());
            }
        }
    }

    private void addButton(@StringRes int buttonName, @RawRes final int audioRes) {
        addButton(getString(buttonName), new MyClickListener(audioRes));
    }

    private void addButton(@NonNull final String buttonName, final MyClickListener clickListener) {
        final ToggleButton button =
            (ToggleButton) LayoutInflater.from(this).inflate(R.layout.layout_button, null, false);
        button.setText(buttonName);
        button.setTextOff(buttonName);
        button.setOnClickListener(clickListener);

        buttonsContainer.addView(button);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        button.setLayoutParams(layoutParams);
    }

    private class MyClickListener implements View.OnClickListener {

        private final MediaPlayer mediaPlayer;

        /**
         * Package-protected because method is used from an inner/anonymous class.
         */
        /* default */ ToggleButton button;

        /* default */ MyClickListener(final MediaPlayer mediaPlayer) {
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

        /* default */ MyClickListener(@RawRes final int audioRes) {
            this(MediaPlayer.create(HomeActivity.this, audioRes));
        }

        @Override
        public void onClick(final View v) {
            button = (ToggleButton) v;
            if (mediaPlayer == null) {
                return;
            }

            if (mediaPlayer.isPlaying()) {
                mediaPlayer.seekTo(0);
            } else {
                mediaPlayer.start();
            }

        }
    }

    public static void setMediaPlayerDataSource(
        final Context context
        , final MediaPlayer mediaPlayer
        , String fileInfo) throws Exception {

        if (fileInfo.startsWith("content://")) {
            try {
                final Uri uri = Uri.parse(fileInfo);
                fileInfo = getRingtonePathFromContentUri(context, uri);
            } catch (Exception e) {
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
        Cursor ringtoneCursor = context.getContentResolver().query(ringtonesUri, null, MediaStore.Audio.Media.DATA + "='" + path + "'", null, null);
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

    private void addDefaultButtons() {
        addButton(R.string.activar, R.raw.activar);
        addButton(R.string.ambulancia, R.raw.ambulancia);
        addButton(R.string.banfield, R.raw.banfield);
        addButton(R.string.campito, R.raw.campito);
        addButton(R.string.careta, R.raw.careta);
        addButton(R.string.chimichimi, R.raw.chimichimi);
        addButton(R.string.chiquichiqui, R.raw.chiquichiqui);
        addButton(R.string.corrientes, R.raw.corrientes);
        addButton(R.string.crosscountry, R.raw.crosscountry);
        addButton(R.string.derecha, R.raw.derecha);
        addButton(R.string.dondeestan, R.raw.dondeestan);
        addButton(R.string.dondeestan2, R.raw.dondeestan2);
        addButton(R.string.dondeestas, R.raw.dondeestas);
        addButton(R.string.estamosyendo, R.raw.estamosyendo);
        addButton(R.string.guardaconcramer, R.raw.guardaconcramer);
        addButton(R.string.guardaeltaxi, R.raw.guardaeltaxi);
        addButton(R.string.izquierda, R.raw.izquierda);
        addButton(R.string.izquierdaizquierda, R.raw.izquierdaizquierda);
        addButton(R.string.lacamionetaimposible, R.raw.lacamionetaimposible);
        addButton(R.string.meseguis, R.raw.meseguis);
        addButton(R.string.nahu, R.raw.nahuuu);
        addButton(R.string.nochedesexo, R.raw.nochedesexo);
        addButton(R.string.ochominutos, R.raw.llegamosenochominutos);
        addButton(R.string.otrotema, R.raw.otrotema);
        addButton(R.string.pampayvinedos, R.raw.pampayvinedos);
        addButton(R.string.parademandarmsjs, R.raw.parademandarmsjs);
        addButton(R.string.pasaloenverdeoamarillo, R.raw.pasaloenverdeoamarillo);
        addButton(R.string.pelotudomarchaatras, R.raw.pelotudomarchaatras);
        addButton(R.string.pepa, R.raw.pepa);
        addButton(R.string.pickypicky, R.raw.pickypicky);
        addButton(R.string.pistera, R.raw.pistera);
        addButton(R.string.poracahastaelsemaforo, R.raw.poracahastaelsemaforo);
        addButton(R.string.poronga, R.raw.poronga);
        addButton(R.string.quepasaconnahu, R.raw.quepasaconnahu);
        addButton(R.string.quierofiesta, R.raw.quierofiesta);
        addButton(R.string.radio, R.raw.radio);
        addButton(R.string.tresminutos, R.raw.tresminutos);
        addButton(R.string.todosimulado, R.raw.todosimulado);
        addButton(R.string.trescientosocholacon, R.raw.trecientosocholacon);
        addButton(R.string.unahorahaciendoluces, R.raw.unahorahaciendoluces);
        addButton(R.string.verdeoamarillo, R.raw.verdeoamarillo);
    }
}
