package com.github.barriosnahuel.vossosunboton.ui.home

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

private const val DRAG_DIRECTIONS: Int = 0
private const val SWIPE_DIRECTIONS: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

internal class SwipeDismissListener constructor(private val adapter: SoundsAdapter) : ItemTouchHelper.SimpleCallback(DRAG_DIRECTIONS, SWIPE_DIRECTIONS) {
    private val background: ColorDrawable = ColorDrawable(Color.RED)

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        Timber.v("Moved view at ${viewHolder.oldPosition} to ${viewHolder.layoutPosition}")
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        Timber.v("Swiped view at ${viewHolder.layoutPosition} to ${parse(direction)}")

        adapter.remove(viewHolder.adapterPosition)
    }

    override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val icon: Drawable? = ContextCompat.getDrawable(recyclerView.context, com.github.barriosnahuel.vossosunboton.R.drawable.baseline_delete_white_36)
        if (icon == null) {
            Timber.e("Delete icon on swipe not shown to the user")
            return
        }

        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        when {
            dX > 0 -> { // Swiping to the right
                val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
                val iconRight = itemView.left + iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(itemView.left, itemView.top,
                        itemView.left + dX.toInt() + backgroundCornerOffset,
                        itemView.bottom)
            }
            dX < 0 -> { // Swiping to the left
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                background.setBounds(itemView.right + dX.toInt() - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom)
            }
            else -> // view is unSwiped
                background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
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
