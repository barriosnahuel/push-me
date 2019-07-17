package com.github.barriosnahuel.vossosunboton.feature.base;

import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes final int layoutResID) {
        super.setContentView(R.layout.feature_base_activity_base);
        setSupportActionBar((Toolbar) findViewById(R.id.base_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater.from(this).inflate(layoutResID, (FrameLayout) findViewById(R.id.base_container));
    }

}
