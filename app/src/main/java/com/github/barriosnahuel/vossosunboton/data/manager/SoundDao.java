package com.github.barriosnahuel.vossosunboton.data.manager;

import android.content.Context;

import com.github.barriosnahuel.vossosunboton.data.local.Storage;
import com.github.barriosnahuel.vossosunboton.data.local.defaultaudios.PackagedAudios;
import com.github.barriosnahuel.vossosunboton.model.Sound;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Currently it only handles local storage.
 *
 * @author Nahuel Barrios, on 9/4/16.
 */
public class SoundDao {

    private static final String SOUNDS_NAME = "sounds.name";
    private static final String SOUNDS_FILE_NAME_PREFFIX = "sounds.file.";
    private final Storage storage;

    /**
     * Creates a new instance of this DAO and its dependencies.
     */
    public SoundDao() {
        storage = new Storage();
    }

    /**
     * @param context The execution context.
     * @param sound   The sound to save for this user.
     */
    public void save(final Context context, final Sound sound) {
        final Set<String> names = storage.getAll(context, SOUNDS_NAME);
        names.add(sound.getName());
        storage.save(context, SOUNDS_NAME, names);
        storage.save(context, SOUNDS_FILE_NAME_PREFFIX + sound.getName(), sound.getFile());
    }

    /**
     * @param context The execution context.
     * @return The list of sounds currently stored for this user.
     */
    public List<Sound> find(final Context context) {
        final List<Sound> sounds = new ArrayList<>();

        final Set<String> names = storage.getAll(context, SOUNDS_NAME);

        for (final String eachSoundName : names) {
            final String fileName = storage.get(context, SOUNDS_FILE_NAME_PREFFIX + eachSoundName);
            sounds.add(new Sound(eachSoundName, fileName));
        }

        sounds.addAll(PackagedAudios.get(context));

        return sounds;
    }
}
