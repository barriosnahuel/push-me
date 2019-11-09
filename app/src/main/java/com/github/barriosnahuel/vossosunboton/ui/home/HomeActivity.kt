package com.github.barriosnahuel.vossosunboton.ui.home

import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.feature.base.AbstractActivity

/**
 * An example full-screen activity that shows and hides the system UI (i.e. status bar and navigation/system bar) with
 * user interaction.
 */
class HomeActivity : AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val supportActionBar = supportActionBar
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.ic_launcher)

        setupView()
    }

    /**
     * We don't want to open this activity again (default behavior at [AbstractActivity]) when the user taps on
     * the home icon.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }

    private fun setupView() {
        val buttonsContainer = findViewById<RecyclerView>(R.id.buttons_container)

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        buttonsContainer.setHasFixedSize(true)

        // use a linear layout manager
        buttonsContainer.layoutManager = LinearLayoutManager(this)

        val homeView = HomeViewImpl(buttonsContainer)
        val soundsAdapter = SoundsAdapter(homeView)
        buttonsContainer.adapter = soundsAdapter

        ItemTouchHelper(SwipeDismissListener(soundsAdapter)).attachToRecyclerView(buttonsContainer)
    }
}
