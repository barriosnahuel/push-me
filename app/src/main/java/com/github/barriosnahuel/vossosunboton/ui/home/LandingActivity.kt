package com.github.barriosnahuel.vossosunboton.ui.home

import android.os.Bundle
import android.view.MenuItem
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.feature.base.AbstractActivity
import com.github.barriosnahuel.vossosunboton.feature.base.NavigationSections

/**
 * Home screen of this app. Currently is has a default UI, it's still on backlog to get a new fresh UI!
 */
class LandingActivity : AbstractActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.app_activity_root)
        bindToolbar()
        bindNavigation()

        switchFragment(NavigationSections.HOME)
    }

    override fun bindToolbar() {
        super.bindToolbar()
        supportActionBar?.setHomeAsUpIndicator(R.mipmap.app_ic_launcher)
    }

    /**
     * We don't want to open this activity again (default behavior at [AbstractActivity]) when the user taps on
     * the home icon.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
