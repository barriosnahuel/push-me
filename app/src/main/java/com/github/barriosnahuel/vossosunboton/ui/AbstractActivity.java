package com.github.barriosnahuel.vossosunboton.ui;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import com.github.barriosnahuel.vossosunboton.R;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes final int layoutResID) {
        super.setContentView(R.layout.activity_base);
        setSupportActionBar((Toolbar) findViewById(R.id.base_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater.from(this).inflate(layoutResID, (FrameLayout) findViewById(R.id.base_container));
    }

}
