package com.github.barriosnahuel.vossosunboton.feature.home;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import com.github.barriosnahuel.vossosunboton.AbstractActivity;
import com.github.barriosnahuel.vossosunboton.R;
import com.github.barriosnahuel.vossosunboton.feature.addbutton.SoundDao;
import com.github.barriosnahuel.vossosunboton.feature.defaultaudios.PackagedAudios;
import com.github.barriosnahuel.vossosunboton.model.Sound;
import java.util.List;
import java.util.Set;

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with
 * user interaction.
 */
public class HomeActivity extends AbstractActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher);

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
        final RecyclerView buttonsContainer = (RecyclerView) findViewById(R.id.buttons_container);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        buttonsContainer.setHasFixedSize(true);

        // use a linear layout manager
        buttonsContainer.setLayoutManager(new LinearLayoutManager(this));

        final SoundDao soundsDao = new SoundDao();
        final Set<Sound> localSounds = soundsDao.find(this);

        final List<Sound> buttons = PackagedAudios.get(this);
        for (final Sound sound : localSounds) {
            buttons.add(0, sound);
        }

        // specify an adapter (see also next example)
        buttonsContainer.setAdapter(new SoundsAdapter(buttons));
    }

}
