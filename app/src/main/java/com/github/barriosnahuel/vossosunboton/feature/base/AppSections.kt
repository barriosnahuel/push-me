package com.github.barriosnahuel.vossosunboton.feature.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.commons.android.intent.SafeIntent
import com.github.barriosnahuel.vossosunboton.ui.home.DynamicFragment

internal data class DeepLink(val host: String, val path: String) {

    /**
     * @return The deep-link that must be used to open a specific screen.
     */
    internal fun get(): String = String.format(
        "%s://%s%s",
        "sosunboton",
        host,
        path
    )
}

/**
 * Created by Nahuel Barrios on 11/12/16.
 */
internal class SectionFactory(private val supportFragmentManager: FragmentManager) {
    internal fun get(section: NavigationSections): Fragment {
        return supportFragmentManager.findFragmentByTag(section.tag) ?: section.createFragment()
    }
}

/**
 * Enumerates the different L1 sections of this app.
 * @property id the [androidx.annotation.IdRes] of each navigation menu section.
 * @property deeplink the deep link that can be used to navigate to that section.
 */
enum class NavigationSections(val id: Int, private val deeplink: DeepLink) {

    HOME(R.id.app_navigation_menu_item_home, DeepLink("home", "/")),
    FAVORITES(R.id.app_navigation_menu_item_favourites, DeepLink("favorites", "/")),
    EXPLORE(R.id.app_navigation_menu_item_explore, DeepLink("explore", "/"));

    val tag = deeplink.host

    /**
     * @param context The execution context.
     * @return A package-filtered intent to open this screen.
     */
    fun getIntent(context: Context): Intent {
        return SafeIntent(context, Uri.parse(this.deeplink.get()))
    }

    internal fun createFragment(): Fragment = DynamicFragment.newInstance(this.tag)

    companion object {
        internal fun findSectionById(id: Int): NavigationSections = values().first { it.id == id }
    }
}
