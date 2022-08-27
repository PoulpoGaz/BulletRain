package fr.poulpogaz.jam;

import java.nio.file.Path;

public class Cache {

    public static Path ROOT = Path.of(System.getProperty("java.io.tmpdir") + "/Java");

    public static void setRoot() {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            Cache.setRoot(System.getenv("APPDATA"), "/jrace/");
        } else {
            Cache.setRoot(System.getProperty("user.home"), "/.config/jrace/");
        }
    }

    private static void setRoot(String root, String... more) {
        ROOT = Path.of(root, more);
    }

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