package com.github.barriosnahuel.vossosunboton.feature.addbutton;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class Sound {

    private final String name;
    private final String file;

    public Sound(final String name, final String file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "Sound{" +
            "name='" + name + '\'' +
            ", file='" + file + '\'' +
            '}';
    }
}
