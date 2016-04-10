package com.github.barriosnahuel.vossosunboton.feature.addbutton;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public class Sound {

    private String name;
    private String file;

    public Sound(String name, String file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public String getFile() {
        return file;
    }
}
