package fr.poulpogaz.jam.renderer.io;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public interface ICamera {

    void update(float delta);

    boolean hasMoved();

    Matrix4f view();

    Vector3f getPosition();
}