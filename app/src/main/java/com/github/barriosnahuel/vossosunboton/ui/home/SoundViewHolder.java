package com.github.barriosnahuel.vossosunboton.ui.home;

import androidx.recyclerview.widget.RecyclerView;
import android.widget.ToggleButton;

/**
 * Created by Nahuel Barrios on 11/16/16.
 */
/* default */ class SoundViewHolder extends RecyclerView.ViewHolder {

    /* default */ ToggleButton toggleButton;

    /* default */ SoundViewHolder(ToggleButton v) {
        super(v);
        toggleButton = v;
    }
}
