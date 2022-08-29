package fr.poulpogaz.jam;

import java.nio.file.Path;

public class Cache {

    public static Path ROOT = Path.of("jam-cache");

    public static Path of(String path) {
        return resolve(Path.of(path));
    }

    public static Path of(String path, String more) {
        return resolve(Path.of(path, more));
    }

    public static Path resolve(Path path) {
        return ROOT.resolve(path);
    }
}