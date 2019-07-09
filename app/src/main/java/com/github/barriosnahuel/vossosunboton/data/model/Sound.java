package com.github.barriosnahuel.vossosunboton.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class Sound {

    private final String name;

    @Nullable
    private final String file;

    @RawRes
    private int rawRes;

    public Sound(@NonNull final String name, @NonNull final String file) {
        this.name = name;
        this.file = file;
    }

    public Sound(@NonNull final String name, final int rawRes) {
        this.name = name;
        this.rawRes = rawRes;
        file = null;
    }

    public String getName() {
        return name;
    }

    @Nullable
    public String getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "Sound{" +
            "name='" + name + '\'' +
            ", file='" + file + '\'' +
            ", rawRes=" + rawRes +
            '}';
    }

    public int getRawRes() {
        return rawRes;
    }
}
