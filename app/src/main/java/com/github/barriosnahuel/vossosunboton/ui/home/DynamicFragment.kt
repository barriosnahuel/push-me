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
 * Use the [DynamicFragment.newInstance] factory method to create an instance of this fragment.
 */
internal class DynamicFragment : Fragment() {
    private var sectionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            sectionName = it.getString(ARGUMENT_SECTION_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val inflate = inflater.inflate(R.layout.app_fragment_dynamic_fragment, container, false)

        val recyclerView = inflate.findViewById<RecyclerView>(R.id.app_buttons_container)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val homeView = HomeViewImpl(recyclerView)
        val soundsAdapter = SoundsAdapter(homeView, findBestDataSetFor(sectionName))
        recyclerView.adapter = soundsAdapter

        ItemTouchHelper(SwipeDismissListener(soundsAdapter)).attachToRecyclerView(recyclerView)

        return inflate
    }

    private fun findBestDataSetFor(customView: String?): Query = when (customView) {
        NavigationSections.HOME.tag -> Query.HOME
        NavigationSections.FAVORITES.tag -> Query.FAVORITES
        NavigationSections.EXPLORE.tag -> Query.EXPLORE
        else -> Query.HOME
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
