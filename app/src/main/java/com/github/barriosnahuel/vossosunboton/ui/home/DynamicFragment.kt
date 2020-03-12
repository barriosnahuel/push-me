package com.github.barriosnahuel.vossosunboton.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.feature.base.NavigationSections
import timber.log.Timber

private const val ARGUMENT_SECTION_NAME = "section"

/**
 * A simple [Fragment] subclass.
 * Use the [DynamicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DynamicFragment : Fragment() {
    private var sectionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionName = it.getString(ARGUMENT_SECTION_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val inflate = inflater.inflate(R.layout.app_fragment_dynamic_fragment, container, false)

        setupView(inflate.findViewById(R.id.app_buttons_container))

        return inflate
    }

    private fun setupView(inflate: RecyclerView) {
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        inflate.setHasFixedSize(true)

        // use a linear layout manager
        inflate.layoutManager = LinearLayoutManager(context)

        val homeView = HomeViewImpl(inflate)
        val soundsAdapter = SoundsAdapter(homeView, findBestDataSetFor(sectionName))
        inflate.adapter = soundsAdapter

        ItemTouchHelper(SwipeDismissListener(soundsAdapter)).attachToRecyclerView(inflate)
    }

    private fun findBestDataSetFor(customView: String?): Query = when (customView) {
        NavigationSections.HOME.tag -> Query.HOME
        NavigationSections.FAVORITES.tag -> Query.FAVORITES
        NavigationSections.EXPLORE.tag -> Query.EXPLORE
        else -> Query.HOME // TODO: Should be an error page?
    }

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment using the provided parameters.
         *
         * @param sectionName The name of the section to load in this dynamic fragment.
         * @return A new instance of fragment [DynamicFragment] for the specified [sectionName].
         */
        fun newInstance(sectionName: String): DynamicFragment {
            Timber.d("Creating fragment instance for %s", sectionName)
            return DynamicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARGUMENT_SECTION_NAME, sectionName)
                }
            }
        }
    }
}