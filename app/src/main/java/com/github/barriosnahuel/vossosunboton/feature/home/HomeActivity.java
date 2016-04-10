package com.github.barriosnahuel.vossosunboton.feature.home;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ToggleButton;

import com.github.barriosnahuel.vossosunboton.AbstractActivity;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.feature.addbutton.Sound;
import com.github.barriosnahuel.vossosunboton.feature.addbutton.SoundDao;

import java.io.File;
import java.io.FileInputStream;
import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with user interaction.
 */
public class HomeActivity extends AbstractActivity {

    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        populateGrid();
    }

    /**
     * We don't want to open this activity again (default behavior at {@link AbstractActivity}) when the user taps on the home icon.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    private void populateGrid() {
        grid = (GridLayout) findViewById(R.id.grid);

//        addCustomButtons();
        addDefaultButtons();
    }

    private void addCustomButtons() {
        SoundDao soundsDao = new SoundDao();
        Set<Sound> sounds = soundsDao.find(this);

        for (Sound eachSound : sounds) {
            MediaPlayer mediaPlayer = new MediaPlayer();

            File file = getFileStreamPath(eachSound.getFile());
            boolean appliedForAll = file.setExecutable(true, false);

            if (!file.canExecute()) {
                boolean applied = file.setExecutable(true, true);
                if (!applied) {
                    Log.e(TAG, "Fallo!!!");
                }
            }
            try {
                setMediaPlayerDataSource(this, mediaPlayer, eachSound.getFile());
                mediaPlayer.prepare();
                addButton(eachSound.getName(), new MyClickListener(mediaPlayer));
            } catch (Exception e) {
                e.printStackTrace();
            }

//                File fileStreamPath = getFileStreamPath(eachSound.getFile());
//                FileInputStream fileInputStream = new FileInputStream(fileStreamPath);
//                mediaPlayer.setDataSource(fileInputStream.getFD());

//                Uri uri = Uri.fromFile();
//                mediaPlayer.setDataSource(this, Uri.parse(Uri.encode(uri.toString())));


        }
    }

    private void addButton(@StringRes int buttonName, @RawRes final int audioRes) {
        addButton(getString(buttonName), new MyClickListener(audioRes));
    }

    private void addButton(String buttonName, MyClickListener clickListener) {
        final ToggleButton button = new ToggleButton(this);
        button.setText(buttonName);
        button.setTextOff(buttonName);
        button.setOnClickListener(clickListener);

        grid.addView(button);

        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) button.getLayoutParams();
        layoutParams.setGravity(Gravity.CENTER_HORIZONTAL);
        button.setLayoutParams(layoutParams);
    }

    private void addDefaultButtons() {
        addButton(R.string.activar, R.raw.activar);
        addButton(R.string.ambulancia, R.raw.ambulancia);
        addButton(R.string.banfield, R.raw.banfield);
        addButton(R.string.campito, R.raw.campito);
        addButton(R.string.careta, R.raw.careta);
        addButton(R.string.chimichimi, R.raw.chimichimi);
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
        addButton(R.string.radio, R.raw.radio);
        addButton(R.string.todosimulado, R.raw.todosimulado);
        addButton(R.string.trescientosocholacon, R.raw.trecientosocholacon);
        addButton(R.string.unahorahaciendoluces, R.raw.unahorahaciendoluces);
        addButton(R.string.verdeoamarillo, R.raw.verdeoamarillo);
    }

    private class MyClickListener implements View.OnClickListener {

        private MediaPlayer mediaPlayer;

        private ToggleButton button;

        public MyClickListener(MediaPlayer mediaPlayer) {
            this.mediaPlayer = mediaPlayer;

            if (this.mediaPlayer == null) {
                Log.e(TAG, "Can't create media player for the specified resource");
            } else {
                this.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        MyClickListener.this.button.toggle();
                    }
                });
            }
        }

        public MyClickListener(@RawRes int audioRes) {
            this(MediaPlayer.create(HomeActivity.this, audioRes));
        }

        @Override
        public void onClick(final View v) {
            button = (ToggleButton) v;
            if (mediaPlayer == null) {
                return;
            }

            if (mediaPlayer.isPlaying()) {
                button.toggle();
            } else {
                mediaPlayer.start();
            }

        }
    }

    public static void setMediaPlayerDataSource(Context context,
                                                MediaPlayer mp, String fileInfo) throws Exception {

        if (fileInfo.startsWith("content://")) {
            try {
                Uri uri = Uri.parse(fileInfo);
                fileInfo = getRingtonePathFromContentUri(context, uri);
            } catch (Exception e) {
            }
        }

        try {
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                try {
                    setMediaPlayerDataSourcePreHoneyComb(context, mp, fileInfo);
                } catch (Exception e) {
                    setMediaPlayerDataSourcePostHoneyComb(context, mp, fileInfo);
                }
            } else {
                setMediaPlayerDataSourcePostHoneyComb(context, mp, fileInfo);
            }

        } catch (Exception e) {
            try {
                setMediaPlayerDataSourceUsingFileDescriptor(context, mp, fileInfo);
            } catch (Exception ee) {
                String uri = getRingtoneUriFromPath(context, fileInfo);
                mp.reset();
                mp.setDataSource(uri);
            }
        }
    }

    private static void setMediaPlayerDataSourcePreHoneyComb(Context context, MediaPlayer mp, String fileInfo) throws Exception {
        mp.reset();
        mp.setDataSource(fileInfo);
    }

    private static void setMediaPlayerDataSourcePostHoneyComb(Context context, MediaPlayer mp, String fileInfo) throws Exception {
        mp.reset();
        mp.setDataSource(context, Uri.parse(Uri.encode(fileInfo)));
    }

    private static void setMediaPlayerDataSourceUsingFileDescriptor(
            Context context, MediaPlayer mp, String fileInfo) throws Exception {
        File file = new File(fileInfo);
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
}
