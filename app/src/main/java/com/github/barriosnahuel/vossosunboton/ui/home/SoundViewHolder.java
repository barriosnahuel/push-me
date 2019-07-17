package com.github.barriosnahuel.vossosunboton.ui.home;

import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
/* default */ class SoundViewHolder extends RecyclerView.ViewHolder {

    /* default */ ToggleButton toggleButton;

    /* default */ SoundViewHolder(final ToggleButton v) {
        super(v);
        toggleButton = v;
    }
}
