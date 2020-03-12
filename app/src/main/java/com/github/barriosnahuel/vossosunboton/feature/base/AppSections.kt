package com.github.barriosnahuel.vossosunboton.feature.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.commons.android.intent.SafeIntent
import com.github.barriosnahuel.vossosunboton.ui.home.DynamicFragment

data class DeepLink(val host: String, val path: String) {

    /**
     * @return The deep-link that must be used to open a specific screen.
     */
    internal fun get(): String {
        return String.format(
            "%s://%s%s"
            , "sosunboton"
            , host
            , path
        )
    }
}

/**
 * Created by Nahuel Barrios on 11/12/16.
 */
class SectionFactory(private val supportFragmentManager: FragmentManager) {
    fun get(section: NavigationSections): Fragment {
        return supportFragmentManager.findFragmentByTag(section.tag) ?: section.createFragment()
    }
}

enum class NavigationSections(val id: Int, private val deeplink: DeepLink) {

    HOME(R.id.app_navigation_menu_item_home, DeepLink("home", "/")),
    FAVORITES(R.id.app_navigation_menu_item_favourites, DeepLink("favorites", "/")),
    EXPLORE(R.id.app_navigation_menu_item_explore, DeepLink("explore", "/"));

    val tag = deeplink.host

    /**
     * @param context The execution context.
     * @return A package-filtered intent to open this screen.
     */
    fun getIntent(context: Context?): Intent {
        return SafeIntent(context!!, Uri.parse(this.deeplink.get()))
    }

    fun createFragment(): Fragment = DynamicFragment.newInstance(this.tag)

    companion object {
        fun findSectionById(id: Int): NavigationSections = values().first { it.id == id }
    }
}

