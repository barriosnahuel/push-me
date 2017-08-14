package com.github.barriosnahuel.vossosunboton.util.file;

import android.support.annotation.NonNull;
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

    public static void copy(@NonNull final InputStream inputStream, @NonNull final FileOutputStream fileOutputStream)
        throws IOException {

        // Transfer bytes from inputStream to fileOutputStream
        final byte[] buffer = new byte[1024];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        fileOutputStream.close();
    }
}
