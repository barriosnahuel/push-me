package com.github.barriosnahuel.vossosunboton.feature.addbutton;

import android.content.Context;
import com.github.barriosnahuel.vossosunboton.model.Sound;
import com.github.barriosnahuel.vossosunboton.storage.Storage;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class SoundDao {

    private final Storage storage;

    private static final class Keys {
        /* default */ static String SOUNDS_NAME = "sounds.name";
        /* default */ static String SOUNDS_FILE_NAME_PREFFIX_ = "sounds.file.";
    }

    public SoundDao() {
        storage = new Storage();
    }

    /* default */ void save(final Context context, final Sound sound) {
        final Set<String> names = storage.getAll(context, Keys.SOUNDS_NAME);
        names.add(sound.getName());
        storage.save(context, Keys.SOUNDS_NAME, names);
        storage.save(context, Keys.SOUNDS_FILE_NAME_PREFFIX_ + sound.getName(), sound.getFile());
    }

    public Set<Sound> find(final Context context) {
        final Set<Sound> sounds = new HashSet<>();

        final Set<String> names = storage.getAll(context, Keys.SOUNDS_NAME);

        for (final String eachSoundName : names) {
            final String fileName = storage.get(context, Keys.SOUNDS_FILE_NAME_PREFFIX_ + eachSoundName);
            sounds.add(new Sound(eachSoundName, fileName));
        }

        return sounds;
    }
}
