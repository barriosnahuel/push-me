package com.github.barriosnahuel.vossosunboton.feature.base

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.ui.extensions.getTagFromArguments
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

        changeSection(activeSection?.tag, nextFragment)
        activeSection = nextSection

        return true
    }

    /**
     * @param currentFragmentTag the tag of the active fragment (if any). It should be used to hide the active one before showing the next.
     * @param nextFragment the fragment to add/attach and display to the user.
     */
    private fun changeSection(currentFragmentTag: String?, nextFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

        currentFragmentTag?.let { currentTag ->
            supportFragmentManager.findFragmentByTag(currentTag)?.let { fragment ->
                transaction.hide(fragment)
            }
        }

        val tag = nextFragment.getTagFromArguments()
        val existentFragment = supportFragmentManager.findFragmentByTag(tag)
        if (existentFragment == null) {
            Timber.d("Desired section \"$tag\" isn't loaded yet, attaching...")
            transaction.add(R.id.app_content_container, nextFragment, tag)
        } else {
            Timber.d("Desired section \"$tag\" already loaded, moving...")
            transaction.show(existentFragment)
        }

        transaction.commit()
        supportFragmentManager.executePendingTransactions()
    }
}
