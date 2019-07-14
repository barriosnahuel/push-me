@file:JvmName("FileUtils")

package com.github.barriosnahuel.vossosunboton.commons.file

import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

private const val INPUT_STREAM_READ_BUFFER_SIZE = 1024

/**
 * @param inputStream The input stream to copy.
 * @param fileOutputStream The output stream to use.
 * @throws IOException either calling [InputStream.read] or [FileOutputStream.write], or even when closing those streams.
 */
@Throws(IOException::class)
fun copy(inputStream: InputStream, fileOutputStream: FileOutputStream) {
    inputStream.copyTo(fileOutputStream, INPUT_STREAM_READ_BUFFER_SIZE)

    inputStream.close()
    fileOutputStream.close()
}
