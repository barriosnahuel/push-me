package com.github.barriosnahuel.vossosunboton.ui.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

/**
 * @param containerViewId the parent view for the given [nextFragment] .
 * @param currentFragmentTag the tag of the active fragment (if any). It should be used to hide the active one before showing the next.
 * @param nextFragment the fragment to add/attach and display to the user.
 */
internal fun FragmentManager.change(@IdRes containerViewId: Int, currentFragmentTag: String?, nextFragment: Fragment) {
    // TODO: 11/22/20 Animations should be moved outside the extension function because they are app's specific behaviour
    val transaction = beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

    currentFragmentTag?.let { currentTag ->
        findFragmentByTag(currentTag)?.let { fragment ->
            transaction.hide(fragment)
        }
    }

    val tag = nextFragment.getTagFromArguments()
    val existentFragment = findFragmentByTag(tag)
    if (existentFragment == null) {
        Timber.d("Desired section \"$tag\" isn't loaded yet, attaching...")
        transaction.add(containerViewId, nextFragment, tag)

    } else {
        Timber.d("Desired section \"$tag\" already loaded, moving...")
        transaction.show(existentFragment)
    }

    transaction.commit()
    executePendingTransactions()
}
