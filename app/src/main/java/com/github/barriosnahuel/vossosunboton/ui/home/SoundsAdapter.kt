package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.model.Sound
import com.github.barriosnahuel.vossosunboton.model.data.manager.SoundDao
import timber.log.Timber

internal class SoundsAdapter(private val homeView: HomeView) : RecyclerView.Adapter<SoundViewHolder>() {

    private val sounds = SoundDao().find(homeView.currentView().context)

    private var marginPx = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val button = LayoutInflater.from(parent.context).inflate(R.layout.layout_button, parent, false) as ToggleButton

        val layoutParams = button.layoutParams as RecyclerView.LayoutParams

        if (marginPx == -1) {
            marginPx = parent.resources.getDimensionPixelSize(R.dimen.feature_base_material_horizontal_padding)
        }

        layoutParams.leftMargin = marginPx
        layoutParams.rightMargin = marginPx

        return SoundViewHolder(button)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val toggleButton = holder.toggleButton

        val layoutParams = toggleButton.layoutParams as RecyclerView.LayoutParams
        when {
            isFirst(position) -> {
                layoutParams.topMargin = marginPx
                layoutParams.bottomMargin = 0
            }
            isLast(position) -> {
                layoutParams.topMargin = 0
                layoutParams.bottomMargin = marginPx
            }
            else -> {
                layoutParams.topMargin = 0
                layoutParams.bottomMargin = 0
            }
        }

        val sound = sounds[position]

        toggleButton.text = sound.name
        toggleButton.textOff = sound.name

        toggleButton.setOnClickListener(PlaybackClickListener(homeView, sound))
        toggleButton.setOnLongClickListener(ShareClickListener(sound))
    }

    override fun getItemCount(): Int {
        return sounds.size
    }

    private fun isFirst(position: Int): Boolean {
        return position == 0
    }

    private fun isLast(position: Int): Boolean {
        return itemCount - 1 == position
    }

    /**
     * @param position position in the adapter of the item to remove.
     */
    fun remove(position: Int) {
        val soundToRemove = sounds[position]

        sounds.removeAt(position)
        notifyItemRemoved(position)

        if (soundToRemove.isBundled()) {
            Timber.w("Delete feature for bundled buttons is not yet released, button won't be deleted. Button: %s", soundToRemove.name)
            homeView.showFeatureNotImplementedFeedback()
        } else {
            homeView.showDeleteButtonFeedback(this, soundToRemove, position)
        }
    }

    fun restore(removedSound: Sound, originalPosition: Int) {
        sounds.add(originalPosition, removedSound)
        notifyItemInserted(originalPosition)
    }

}

internal class SoundViewHolder(var toggleButton: ToggleButton) : RecyclerView.ViewHolder(toggleButton)