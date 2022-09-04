package fr.poulpogaz.jam.engine;

import com.badlogic.gdx.utils.Array;

public class MultiHitBox implements HitBox {

    private final Array<HitBox> boxes = new Array<>();

    @Override
    public boolean collide(HitBox b) {
        if (b instanceof MultiHitBox) {
            MultiHitBox multi = (MultiHitBox) b;

            for (int i = 0; i < boxes.size; i++) {
                for (int j = i; j < multi.boxes.size; j++) {
                    if (boxes.get(i).collide(multi.boxes.get(j))) {
                        return true;
                    }
                }
            }

        } else {
            for (int i = 0; i < boxes.size; i++) {
                if (boxes.get(i).collide(b)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void getAABB(AABB dest) {
        if (boxes.isEmpty()) {
            dest.set(0, 0, 0, 0);
        } else {
            float minX = Float.MAX_VALUE;
            float minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE;
            float maxY = -Float.MAX_VALUE;

            AABB temp = new AABB();
            for (int i = 0; i < boxes.size; i++) {
                boxes.get(i).getAABB(temp);

                minX = Math.min(minX, temp.getX());
                minY = Math.min(minY, temp.getY());

                maxX = Math.max(maxX, temp.getX() + temp.getWidth());
                maxY = Math.max(maxY, temp.getY() + temp.getHeight());
            }

            dest.set(minX, minY, maxX - minX, maxY - minY);
        }
    }

    public Array<HitBox> getBoxes() {
        return boxes;
    }
}
