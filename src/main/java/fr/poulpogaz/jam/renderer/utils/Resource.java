package fr.poulpogaz.jam.renderer.utils;

import org.lwjgl.BufferUtils;
import org.lwjgl.system.MemoryUtil;

import java.io.*;
import java.nio.Buffer;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

public class Resource {

    public static InputStream asStream(String path) {
        return Resource.class.getResourceAsStream("/" + path);
    }

    public static BufferedReader newBufferedReader(String path) {
        return new BufferedReader(
                new InputStreamReader(
                        Resource.class.getResourceAsStream("/" + path)));
    }

    public static String readString(String path) throws IOException {
        BufferedReader br = newBufferedReader(path);

        StringBuilder builder = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            builder.append(line).append("\n");
        }

        br.close();

        return builder.toString();
    }

    /**
     * Buffer must be freed with {@link MemoryUtil#memFree(Buffer)}
     */
    public static ByteBuffer toByteBuffer(String path, int bufferSize) throws IOException {
        return toByteBuffer(path, bufferSize, true);
    }

    /**
     * Buffer must be freed with {@link MemoryUtil#memFree(Buffer)}
     */
    public static ByteBuffer toByteBuffer(String path, int bufferSize, boolean resource) throws IOException {
        ByteBuffer buffer;
        InputStream source;

        if (resource) {
            source = Resource.asStream(path);
        } else {
            source = new BufferedInputStream(
                    Files.newInputStream(
                            Path.of(path)));
        }

        if (source == null) {
            throw new IOException("Path is incorrect: " + path);
        }

        try (source;
             ReadableByteChannel rbc = Channels.newChannel(source)) {
            buffer = MemoryUtil.memAlloc(bufferSize);

            while (true) {
                int bytes = rbc.read(buffer);
                if (bytes == -1) {
                    break;
                }

                if (buffer.remaining() == 0) {
                    buffer = MemoryUtil.memRealloc(buffer, buffer.capacity() + bufferSize);
                }
            }
        }

        return buffer.flip();
    }
}