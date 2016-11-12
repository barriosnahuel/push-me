package com.github.barriosnahuel.vossosunboton;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.github.barriosnahuel.vossosunboton.feature.home.HomeActivity;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public abstract class AbstractActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(R.layout.activity_base);
        setSupportActionBar((Toolbar) findViewById(R.id.base_toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

        FrameLayout container = (FrameLayout) findViewById(R.id.base_container);
        LayoutInflater.from(this).inflate(layoutResID, container);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            startActivity(new Intent(this, HomeActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
