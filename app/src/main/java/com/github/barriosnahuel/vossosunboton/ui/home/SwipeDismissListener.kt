package com.github.barriosnahuel.vossosunboton.ui.home

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.barriosnahuel.vossosunboton.R
import timber.log.Timber

private const val DRAG_DIRECTIONS: Int = 0
private const val SWIPE_DIRECTIONS: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

internal class SwipeDismissListener constructor(private val adapter: SoundsAdapter) :
        ItemTouchHelper.SimpleCallback(DRAG_DIRECTIONS, SWIPE_DIRECTIONS) {

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        Timber.v("Moved view at ${viewHolder.oldPosition} to ${viewHolder.layoutPosition}")
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Timber.v("Swiped view at ${viewHolder.layoutPosition} to ${parse(direction)}")

        adapter.remove(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val icon: Drawable? = ContextCompat.getDrawable(recyclerView.context, R.drawable.app_ic_delete_primary_color_36dp)
        if (icon == null) {
            Timber.e("Delete icon on swipe not shown to the user")
            return
        }

        val itemView = viewHolder.itemView
        val viewPadding = recyclerView.resources.getDimensionPixelSize(R.dimen.app_default_view_spacing_2x)
        val halfIcon = icon.intrinsicHeight / 2
        val top = itemView.top + ((itemView.bottom - itemView.top) / 2 - halfIcon)
        val bottom = top + icon.intrinsicHeight

        var left = 0
        var right = 0

        when {
            dX > 0 -> {
                Timber.v("onChildDraw when swiping right...")

                if (dX > viewPadding) {
                    left = itemView.left + viewPadding
                    right = itemView.left + viewPadding + icon.intrinsicWidth
                }
            }
            dX < 0 -> {
                Timber.v("onChildDraw when swiping left...")

                if (dX < -viewPadding) {
                    left = itemView.right - viewPadding - halfIcon * 2
                    right = itemView.right - viewPadding
                }
            }
        }

        icon.setBounds(left, top, right, bottom)
        icon.draw(c)
    }

    private fun parse(direction: Int): String {
        return when (direction) {
            ItemTouchHelper.LEFT -> "left"
            ItemTouchHelper.RIGHT -> "right"
            else -> direction.toString()
        }
    }
}
