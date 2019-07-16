package com.github.barriosnahuel.vossosunboton.ui.home;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.feature.base.AbstractActivity;
import com.github.barriosnahuel.vossosunboton.model.data.manager.SoundDao;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with
 * user interaction.
 */
public class HomeActivity extends AbstractActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        }

        setupView();
    }

    /**
     * We don't want to open this activity again (default behavior at {@link AbstractActivity}) when the user taps on
     * the home icon.
     */
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return true;
    }

    private void setupView() {
        final RecyclerView buttonsContainer = findViewById(R.id.buttons_container);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        buttonsContainer.setHasFixedSize(true);

        // use a linear layout manager
        buttonsContainer.setLayoutManager(new LinearLayoutManager(this));

        final SoundDao soundsDao = new SoundDao();

        // specify an adapter (see also next example)
        buttonsContainer.setAdapter(new SoundsAdapter(getResources(), soundsDao.find(this)));
    }

}
