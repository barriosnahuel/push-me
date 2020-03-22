package com.github.barriosnahuel.vossosunboton.ui.home

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.barriosnahuel.vossosunboton.R

internal class ButtonListItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val topSpacingResId = if (parent.getChildAdapterPosition(view) == 0) {
            // First item...
            R.dimen.app_appbar_scrollable_content_overlap_margin
        } else {
            R.dimen.app_default_view_spacing_2x
        }

        outRect.top = view.resources.getDimensionPixelSize(topSpacingResId)
        outRect.right = view.resources.getDimensionPixelSize(R.dimen.app_default_view_spacing_2x)
        outRect.left = view.resources.getDimensionPixelSize(R.dimen.app_default_view_spacing_2x)

        if (parent.adapter?.itemCount == parent.getChildAdapterPosition(view) + 1) {
            // Last item...
            outRect.bottom = view.resources.getDimensionPixelSize(R.dimen.app_default_view_spacing_2x)
        }
    }
}
