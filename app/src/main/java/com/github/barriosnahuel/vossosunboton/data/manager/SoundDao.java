package com.github.barriosnahuel.vossosunboton.data.manager;

import android.content.Context;
import com.github.barriosnahuel.vossosunboton.data.local.Storage;
import com.github.barriosnahuel.vossosunboton.data.local.defaultaudios.PackagedAudios;
import com.github.barriosnahuel.vossosunboton.data.model.Sound;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class SoundDao {

    private final Storage storage;

        private static final String SOUNDS_NAME = "sounds.name";
        private static final String SOUNDS_FILE_NAME_PREFFIX_ = "sounds.file.";

    public SoundDao() {
        storage = new Storage();
    }

    public void save(final Context context, final Sound sound) {
        final Set<String> names = storage.getAll(context, SOUNDS_NAME);
        names.add(sound.getName());
        storage.save(context, SOUNDS_NAME, names);
        storage.save(context, SOUNDS_FILE_NAME_PREFFIX_ + sound.getName(), sound.getFile());
    }

    public List<Sound> find(final Context context) {
        final List<Sound> sounds = new ArrayList<>();

        final Set<String> names = storage.getAll(context, SOUNDS_NAME);

        for (final String eachSoundName : names) {
            final String fileName = storage.get(context, SOUNDS_FILE_NAME_PREFFIX_ + eachSoundName);
            sounds.add(new Sound(eachSoundName, fileName));
        }

        sounds.addAll(PackagedAudios.get(context));

        return sounds;
    }
}
