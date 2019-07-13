package com.github.barriosnahuel.vossosunboton.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class Sound {

    /**
     * The name of this sound.
     */
    private final String name;

    /**
     * The url used to locate this sound on local storage.
     */
    @Nullable
    private final String file;

    /**
     * The Android RAW resource ID of this sound.
     */
    @RawRes
    private int rawRes;

    /**
     * @param name See {@link #name}.
     * @param file See {@link #file}.
     */
    public Sound(@NonNull final String name, @NonNull final String file) {
        this.name = name;
        this.file = file;
    }

    /**
     * @param name   See {@link #name}.
     * @param rawRes See {@link #rawRes}.
     */
    public Sound(@NonNull final String name, final int rawRes) {
        this.name = name;
        this.rawRes = rawRes;
        file = null;
    }

    /**
     * @return current {@link #name} of this Sound.
     */
    public String getName() {
        return name;
    }

    /**
     * @return current url of this sound. It's getter for {@link #file}.
     */
    @Nullable
    public String getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "Sound{"
                + "name='" + name + '\''
                + ", file='" + file + '\''
                + ", rawRes=" + rawRes
                + '}';
    }

    /**
     * @return current {@link #rawRes} of this sound.
     */
    public int getRawRes() {
        return rawRes;
    }
}
