package fr.poulpogaz.jam.renderer.io;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class BasicCamera implements ICamera {

    private static final Input input = Input.getInstance();

    private static final Vector3f UP = new Vector3f(0, 1, 0);

    private Vector3f position = new Vector3f(0, 0, 3);
    private Vector3f target = new Vector3f(0, 0, -1);
    private Vector3f center = new Vector3f();
    private final Matrix4f view = new Matrix4f();

    private float speed = 0.05f;

    private boolean hasMoved = false;

    public BasicCamera() {
        calculateView();
    }

    @Override
    public void update(float delta) {
        hasMoved = false;

        Vector3f forward = target.mul(speed, new Vector3f());

        if (input.keyPressed(GLFW_KEY_W)) {
            position.add(forward);
            hasMoved = true;
        }
        if (input.keyPressed(GLFW_KEY_S)) {
            position.sub(forward);
            hasMoved = true;
        }

        if (input.keyPressed(GLFW_KEY_UP)) {
            position.add(0, speed, 0);
            hasMoved = true;
        }
        if (input.keyPressed(GLFW_KEY_DOWN)) {
            position.sub(0, speed, 0);
            hasMoved = true;
        }

        Vector3f left = UP.cross(target, new Vector3f()).normalize().mul(speed);
        if (input.keyPressed(GLFW_KEY_LEFT)) {
            position.add(left);
            hasMoved = true;
        }
        if (input.keyPressed(GLFW_KEY_RIGHT)) {
            position.sub(left);
            hasMoved = true;
        }

        if (input.keyPressed(GLFW_KEY_A)) {
            target.rotateY(speed);
            hasMoved = true;
        }
        if (input.keyPressed(GLFW_KEY_D)) {
            target.rotateY(-speed);
            hasMoved = true;
        }

        Vector3f cross = UP.cross(target, new Vector3f()).normalize();

        if (input.keyPressed(GLFW_KEY_KP_8)) {
            target.rotateAxis(-speed, cross.x, cross.y, cross.z);
            hasMoved = true;
        }
        if (input.keyPressed(GLFW_KEY_KP_2)) {
            target.rotateAxis(speed, cross.x, cross.y, cross.z);
            hasMoved = true;
        }

        if (hasMoved) {
            calculateView();
        }
    }

    private void calculateView() {
        position.add(target, center); // center = position + target
        view.setLookAt(position, center, UP);
    }

    public void reset() {
        position = new Vector3f(0, 0, 3);
        target = new Vector3f(0, 0, -1);
        center = new Vector3f();
        calculateView();
        hasMoved = true;
    }

    @Override
    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public Matrix4f view() {
        return view;
    }

    public Vector3f getUP() {
        return UP;
    }

    @Override
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        if (position != null) {
            this.position = position;

            calculateView();
        }
    }

    public Vector3f getTarget() {
        return target;
    }

    public Vector3f getCenter() {
        return center;
    }

    public void setTarget(Vector3f target) {
        if (target != null) {
            this.target = target.normalize();

            calculateView();
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}