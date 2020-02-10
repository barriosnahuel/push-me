package com.github.barriosnahuel.vossosunboton.feature.base;

import android.view.LayoutInflater;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.github.barriosnahuel.vossosunboton.R;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes final int layoutResID) {
        super.setContentView(R.layout.app_activity_root);
        setSupportActionBar(findViewById(R.id.base_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater.from(this).inflate(layoutResID, findViewById(R.id.base_container));
    }

}
