package fr.poulpogaz.jam.tilesetcreator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TilesetCreator {

    public static void main(String[] args) throws IOException {
        Animation[] animations = new Animation[] {
                new Animation(Path.of("explosions/expl_08"), 0.05f),
                new Animation(Path.of("explosions/hit"), 0.05f)
        };

        BufferedImage out = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_ARGB);

        try (BufferedWriter bw = Files.newBufferedWriter(Path.of("core/src/fr/poulpogaz/jam/utils/Animations.java"))) {
            PrintWriter pw = new PrintWriter(bw);
            printHeader(pw);

            int x = 0;
            int y = 0;
            int rowHeight = 0;
            Graphics2D g2d = out.createGraphics();
            for (Animation a : animations) {
                List<Path> list = child(a.p());
                list.sort(Comparator.comparingInt((path) -> extractInt(path.getFileName().toString())));

                String name = a.p().getFileName().toString();

                pw.print("            textures = new TextureRegion[%d];%n".formatted(list.size()));
                for (int i = 0; i < list.size(); i++) {
                    Path path = list.get(i);
                    BufferedImage img = ImageIO.read(path.toFile());

                    if (img.getWidth() + x >= out.getWidth()) {
                        x = 0;
                        y += rowHeight;
                        rowHeight = 0;
                    }

                    pw.printf("            textures[%d] = new TextureRegion(t, %d, %d, %d, %d);%n", i, x, y, img.getWidth(), img.getHeight());
                    g2d.drawImage(img, x, y, null);

                    rowHeight = Math.max(rowHeight, img.getHeight());
                    x += img.getWidth();
                }

                pw.printf("            ANIMATIONS.put(\"%s\", new Animation<>(%ff, textures));%n", name, a.delay);
            }

            printFooter(pw);

            g2d.dispose();
        }

        ImageIO.write(out, "png", new File("assets/textures/explosions.png"));
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


    private static void printHeader(PrintWriter pw) {
        pw.print("""
                // auto generated
                package fr.poulpogaz.jam.utils;
                                
                import com.badlogic.gdx.graphics.Texture;
                import com.badlogic.gdx.graphics.g2d.Animation;
                import com.badlogic.gdx.graphics.g2d.TextureRegion;
                import fr.poulpogaz.jam.Jam;
                                
                import java.util.HashMap;
                import java.util.Map;
                                
                public class Animations {
                                
                    private static final Map<String, Animation<TextureRegion>> ANIMATIONS = new HashMap<>();
                                
                    public static void loadAnimations() {
                        if (ANIMATIONS.isEmpty()) {
                            Texture t = Jam.getTexture("explosions.png");
                        
                            TextureRegion[] textures;
                """);
    }

    private static void printFooter(PrintWriter pw) {
        pw.print("""
                        }
                    }
                                
                    public static Animation<TextureRegion> get(String name) {
                        return ANIMATIONS.get(name);
                    }
                }
                                
                """);
    }

    private record Animation(Path p, float delay) {
    }
}