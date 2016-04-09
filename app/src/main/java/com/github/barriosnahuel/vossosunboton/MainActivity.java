package com.github.barriosnahuel.vossosunboton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main_activity);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));
    }


}
