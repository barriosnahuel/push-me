package com.github.barriosnahuel.vossosunboton.ui.extensions

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

/**
 * Attaches (previously dettaching when necessary) the given fragment.
 * @param nextFragment
 * @param tag
 */
internal fun FragmentManager.attach(@IdRes fragmentContainer: Int, nextFragment: Fragment, tag: String) {

    val transaction = beginTransaction()
        .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)

    when (val currentFragment = findFragmentById(fragmentContainer)) {
        null -> {
            Timber.d("Add 1st fragment in the whole app for this activity")
            transaction.add(fragmentContainer, nextFragment, tag)
        }
        else -> {
            Timber.d("Detaching current visible fragment maintaining its state")
            transaction.detach(currentFragment)

            if (nextFragment.isDetached) {
                Timber.d("Attach next fragment restoring previously detached one' state")
                transaction.attach(nextFragment)
            } else {
                Timber.d("Add next fragment since it's the first time it is requested")
                transaction.add(fragmentContainer, nextFragment, tag)
            }
        }
    }

    transaction.commit()
}