package fr.poulpogaz.jam;

import fr.poulpogaz.jam.utils.Animations;
import fr.poulpogaz.json.JsonException;
import fr.poulpogaz.json.JsonPrettyWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TilesetCreator {

    public static void main(String[] args) throws IOException, JsonException {
        Animation[] animations = new Animation[] {
                new Animation(Path.of("explosions/expl_08"), 2),
                new Animation(Path.of("explosions/hit"), 3)
        };

        BufferedImage out = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);

        int x = 0;
        int y = 0;
        int rowHeight = 0;
        Graphics2D g2d = out.createGraphics();
        for (Animation a : animations) {

            String name = "src/main/resources/animations/" + a.p().getFileName() + "_animation.json";

            try (BufferedWriter bw = Files.newBufferedWriter(Path.of(name))) {
                JsonPrettyWriter jpw = new JsonPrettyWriter(bw);
                jpw.beginObject();
                jpw.field("delay", a.delay());
                jpw.field("texture", "explosions.png");
                jpw.key("frames").beginArray();

                List<Path> list = child(a.p());
                list.sort(Comparator.comparingInt((path) -> extractInt(path.getFileName().toString())));

                for (Path path : list) {
                    BufferedImage img = ImageIO.read(path.toFile());

                    if (img.getWidth() + x >= out.getWidth()) {
                        x = 0;
                        y += rowHeight;
                        rowHeight = 0;
                    }

                    jpw.beginObject();
                    jpw.field("x", x);
                    jpw.setInline(JsonPrettyWriter.Inline.OBJECT);
                    jpw.field("y", y).field("w", img.getWidth()).field("h", img.getHeight());
                    jpw.setInline(JsonPrettyWriter.Inline.NONE);
                    jpw.endObject();

                    g2d.drawImage(img, x, y, null);

                    rowHeight = Math.max(rowHeight, img.getHeight());
                    x += img.getWidth();
                }


                jpw.endArray();
                jpw.endObject();
                jpw.close();
            }
        }

        g2d.dispose();

        ImageIO.write(out, "png", new File("src/main/resources/textures/explosions.png"));
    }

    private static List<Path> child(Path p ) throws IOException {
        try (DirectoryStream<Path> d = Files.newDirectoryStream(p)) {
            List<Path> out = new ArrayList<>();

            for (Path path : d) {
                out.add(path);
            }
            return out;
        }
    }

    private static int extractInt(String str) {
        String s = str.replaceAll("\\D", "");

        if (s.isEmpty()) {
            return 0;
        } else {
            return Integer.parseInt(s);
        }
    }

    private record Animation(Path p, int delay) {}
}
