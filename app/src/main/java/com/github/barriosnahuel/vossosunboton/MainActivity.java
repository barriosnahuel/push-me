package com.github.barriosnahuel.vossosunboton;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.RawRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ToggleButton;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Used for log messages.
     */
    private final String TAG = getClass().getSimpleName();

    private GridLayout grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        populateGrid();
    }

    private void populateGrid() {
        grid = (GridLayout) findViewById(R.id.grid);

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

    private void addButton(@StringRes int stringRes, @RawRes final int audioRes) {
        final ToggleButton button = new ToggleButton(this);
        button.setText(stringRes);
        button.setTextOff(getString(stringRes));
        button.setOnClickListener(new MyClickListener(button, audioRes));

        grid.addView(button);

        GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) button.getLayoutParams();
        layoutParams.setGravity(Gravity.CENTER_HORIZONTAL);
        button.setLayoutParams(layoutParams);
    }

    private class MyClickListener implements View.OnClickListener {

        private final ToggleButton button;

        private MediaPlayer mediaPlayer;

        public MyClickListener(ToggleButton button, @RawRes int audioRes) {
            this.button = button;
            mediaPlayer = MediaPlayer.create(MainActivity.this, audioRes);

            if (mediaPlayer == null) {
                Log.e(TAG, "Can't create media player for the specified resource");
            } else {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        MyClickListener.this.button.toggle();
                    }
                });
            }
        }

        @Override
        public void onClick(final View v) {
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
}
