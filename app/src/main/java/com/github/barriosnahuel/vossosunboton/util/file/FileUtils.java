package com.github.barriosnahuel.vossosunboton.util.file;

import androidx.annotation.NonNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Nahuel Barrios, on 9/4/16.
 */
public final class FileUtils {

    private static final int INPUT_STREAM_READ_SIZE = 1024;

    private FileUtils() {
        // Do nothing.
    }

    /**
     * @param inputStream      The input stream to copy.
     * @param fileOutputStream The output stream to use.
     * @throws IOException either calling {@link InputStream#read(byte[])} or {@link FileOutputStream#write(byte[], int, int)}, or even when closing those streams.
     */
    public static void copy(@NonNull final InputStream inputStream, @NonNull final FileOutputStream fileOutputStream)
            throws IOException {

        // Transfer bytes from inputStream to fileOutputStream
        final byte[] buffer = new byte[INPUT_STREAM_READ_SIZE];
        int len;
        while ((len = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, len);
        }
        inputStream.close();
        fileOutputStream.close();
    }
}
