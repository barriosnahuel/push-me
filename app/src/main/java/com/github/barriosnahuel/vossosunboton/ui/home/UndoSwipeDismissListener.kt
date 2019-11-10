package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.View
import com.github.barriosnahuel.vossosunboton.model.Sound

internal class UndoSwipeDismissListener(
        private val soundsAdapter: SoundsAdapter,
        private val soundToRemove: Sound,
        private val position: Int
) : View.OnClickListener {

    override fun onClick(v: View?) {
        soundsAdapter.restore(soundToRemove, position)
    }

}
