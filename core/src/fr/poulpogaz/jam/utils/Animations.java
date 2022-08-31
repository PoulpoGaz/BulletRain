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
            textures = new TextureRegion[32];
            textures[0] = new TextureRegion(t, 0, 0, 64, 64);
            textures[1] = new TextureRegion(t, 64, 0, 64, 64);
            textures[2] = new TextureRegion(t, 128, 0, 64, 64);
            textures[3] = new TextureRegion(t, 192, 0, 64, 64);
            textures[4] = new TextureRegion(t, 256, 0, 64, 64);
            textures[5] = new TextureRegion(t, 320, 0, 64, 64);
            textures[6] = new TextureRegion(t, 384, 0, 64, 64);
            textures[7] = new TextureRegion(t, 448, 0, 64, 64);
            textures[8] = new TextureRegion(t, 512, 0, 64, 64);
            textures[9] = new TextureRegion(t, 576, 0, 64, 64);
            textures[10] = new TextureRegion(t, 640, 0, 64, 64);
            textures[11] = new TextureRegion(t, 704, 0, 64, 64);
            textures[12] = new TextureRegion(t, 768, 0, 64, 64);
            textures[13] = new TextureRegion(t, 832, 0, 64, 64);
            textures[14] = new TextureRegion(t, 896, 0, 64, 64);
            textures[15] = new TextureRegion(t, 0, 64, 64, 64);
            textures[16] = new TextureRegion(t, 64, 64, 64, 64);
            textures[17] = new TextureRegion(t, 128, 64, 64, 64);
            textures[18] = new TextureRegion(t, 192, 64, 64, 64);
            textures[19] = new TextureRegion(t, 256, 64, 64, 64);
            textures[20] = new TextureRegion(t, 320, 64, 64, 64);
            textures[21] = new TextureRegion(t, 384, 64, 64, 64);
            textures[22] = new TextureRegion(t, 448, 64, 64, 64);
            textures[23] = new TextureRegion(t, 512, 64, 64, 64);
            textures[24] = new TextureRegion(t, 576, 64, 64, 64);
            textures[25] = new TextureRegion(t, 640, 64, 64, 64);
            textures[26] = new TextureRegion(t, 704, 64, 64, 64);
            textures[27] = new TextureRegion(t, 768, 64, 64, 64);
            textures[28] = new TextureRegion(t, 832, 64, 64, 64);
            textures[29] = new TextureRegion(t, 896, 64, 64, 64);
            textures[30] = new TextureRegion(t, 0, 128, 64, 64);
            textures[31] = new TextureRegion(t, 64, 128, 64, 64);
            ANIMATIONS.put("expl_08", new Animation<>(0.050000f, textures));
            textures = new TextureRegion[9];
            textures[0] = new TextureRegion(t, 128, 128, 32, 32);
            textures[1] = new TextureRegion(t, 160, 128, 32, 32);
            textures[2] = new TextureRegion(t, 192, 128, 32, 32);
            textures[3] = new TextureRegion(t, 224, 128, 32, 32);
            textures[4] = new TextureRegion(t, 256, 128, 32, 32);
            textures[5] = new TextureRegion(t, 288, 128, 32, 32);
            textures[6] = new TextureRegion(t, 320, 128, 32, 32);
            textures[7] = new TextureRegion(t, 352, 128, 32, 32);
            textures[8] = new TextureRegion(t, 384, 128, 32, 32);
            ANIMATIONS.put("hit", new Animation<>(0.050000f, textures));
        }
    }

    public static Animation<TextureRegion> get(String name) {
        return ANIMATIONS.get(name);
    }
}

