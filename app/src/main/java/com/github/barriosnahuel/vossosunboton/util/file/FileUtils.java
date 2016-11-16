package com.github.barriosnahuel.vossosunboton.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public final class FileUtils {

    private FileUtils() {
        // Do nothing.
    }

    public static void copy(File src, FileOutputStream fileOutputStream) throws IOException {
        InputStream in = new FileInputStream(src);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            fileOutputStream.write(buf, 0, len);
        }
        in.close();
        fileOutputStream.close();
    }
}
