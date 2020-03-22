package com.github.barriosnahuel.vossosunboton.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.github.barriosnahuel.vossosunboton.R
import com.github.barriosnahuel.vossosunboton.model.Sound
import com.github.barriosnahuel.vossosunboton.model.data.manager.SoundDao
import timber.log.Timber

/**
 * Temp class useful for testing purposes only.
 */
enum class Query {
    HOME, EXPLORE, FAVORITES
}

internal class SoundsAdapter(private val homeView: HomeView, private val query: Query) : RecyclerView.Adapter<SoundViewHolder>() {

    private val sounds = SoundDao().find(homeView.currentView().context).filter {
        when (query) {
            Query.HOME -> !it.isBundled() // Currently we show the same sounds on home as well as favorites.
            Query.FAVORITES -> !it.isBundled()
            Query.EXPLORE -> it.isBundled()
        }
    }.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        return SoundViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.app_layout_button, parent, false))
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        val sound = sounds[position]

        holder.buttonLayout.findViewById<AppCompatTextView>(R.id.app_button_name).text = sound.name
        holder.buttonLayout.findViewById<AppCompatImageView>(R.id.app_action_play_pause).let {
            homeView.playbackClicksListener.addOnClickListener(it, sound)
        }

        holder.buttonLayout.findViewById<AppCompatImageView>(R.id.app_action_share).setOnClickListener(ShareClickListener(sound))
    }

    override fun getItemCount(): Int = sounds.size

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
//            homeView.showDeleteButtonFeedback(this, soundToRemove, position)
        }
    }

    fun restore(removedSound: Sound, originalPosition: Int) {
        sounds.add(originalPosition, removedSound)
        notifyItemInserted(originalPosition)
    }
}

internal class SoundViewHolder(var buttonLayout: View) : RecyclerView.ViewHolder(buttonLayout)
