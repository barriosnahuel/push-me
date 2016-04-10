package com.github.barriosnahuel.vossosunboton.feature.addbutton;

import android.content.Context;

import com.github.barriosnahuel.vossosunboton.storage.Storage;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class SoundDao {

    private Storage storage;

    private interface Keys {
        String SOUNDS_NAME = "sounds.name";
        String SOUNDS_FILE_NAME_PREFFIX_ = "sounds.file.";
    }

    public SoundDao() {
        this.storage = new Storage();
    }

    public void save(Context context, Sound sound) {
        Set<String> names = storage.getAll(context, Keys.SOUNDS_NAME);
        names.add(sound.getName());
        storage.save(context, Keys.SOUNDS_NAME, names);
        storage.save(context, Keys.SOUNDS_FILE_NAME_PREFFIX_ + sound.getName(), sound.getFile());
    }

    public Set<Sound> find(Context context) {
        Set<Sound> sounds = new HashSet<>();

        Set<String> names = storage.getAll(context, Keys.SOUNDS_NAME);

        for (String eachSoundName : names) {
            String fileName = storage.get(context, Keys.SOUNDS_FILE_NAME_PREFFIX_ + eachSoundName);
            sounds.add(new Sound(eachSoundName, fileName));
        }

        return sounds;
    }
}
