package fr.poulpogaz.jam.patterns;

import com.badlogic.gdx.math.Vector2;
import fr.poulpogaz.jam.Constants;

public interface MovePattern {

    /**
     * @param t parameter t
     * @return direction of the entity after t tick
     */
    default Vector2 dir(int t) {
        return dir(t, new Vector2());
    }

    Vector2 dir(int t, Vector2 dest);


    MovePattern FOLLOW_MAP = new MovePattern() {
        @Override
        public Vector2 dir(int t, Vector2 dest) {
            return dest.set(0, -Constants.MAP_SCROLL_SPEED);
        }
    };

    MovePattern STATIC = new MovePattern() {
        @Override
        public Vector2 dir(int t, Vector2 dest) {
            return dest.set(0, 0);
        }
    };
}
