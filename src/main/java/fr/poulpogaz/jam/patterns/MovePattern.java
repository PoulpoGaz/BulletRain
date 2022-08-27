package fr.poulpogaz.jam.patterns;

import org.joml.Vector2f;

public interface MovePattern {

    /**
     * @param t parameter t
     * @return direction of the entity after t
     */
    Vector2f dir(float t);
}
