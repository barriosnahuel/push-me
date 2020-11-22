package com.github.barriosnahuel.vossosunboton.feature.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.ui.extensions.change
import com.google.android.material.bottomnavigation.BottomNavigationView
import timber.log.Timber

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
abstract class AbstractActivity : AppCompatActivity() {

    private var activeSection: NavigationSections? = null

    protected open fun bindToolbar() {
        setSupportActionBar(findViewById(R.id.app_toolbar))
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    protected fun bindNavigation() {
        findViewById<BottomNavigationView>(R.id.app_navigation)
            .setOnNavigationItemSelectedListener { item: MenuItem ->
                if (showFragment(NavigationSections.findSectionById(item.itemId))) {
                    item.isChecked = true // Set selected menu item as the active one
                }
                false
            }
    }

    /**
     * Show to the given [NavigationSections] by handling current/previous fragment actions.
     */
    internal fun showFragment(nextSection: NavigationSections): Boolean {
        val nextFragment = SectionFactory(supportFragmentManager).get(nextSection)

        if (activeSection == nextSection) {
            Timber.d("Desired section \"${nextSection.tag}\" is currently active. Nothing will change.")
            return false
        }

        supportFragmentManager.change(R.id.app_content_container, activeSection?.tag, nextFragment)
        activeSection = nextSection

        return true
    }
}
