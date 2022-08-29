package fr.poulpogaz.jam.patterns;

import fr.poulpogaz.jam.Constants;
import org.joml.Vector2f;

public interface MovePattern {

    /**
     * @param t parameter t
     * @return direction of the entity after t tick
     */
    default Vector2f dir(int t) {
        return dir(t, new Vector2f());
    }

    Vector2f dir(int t, Vector2f dest);


    MovePattern FOLLOW_MAP = new MovePattern() {
        @Override
        public Vector2f dir(int t, Vector2f dest) {
            return dest.set(0, Constants.MAP_SCROLL_SPEED);
        }
    };

    MovePattern STATIC = new MovePattern() {
        @Override
        public Vector2f dir(int t, Vector2f dest) {
            return dest.set(0, 0);
        }
    };
}
