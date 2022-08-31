package fr.poulpogaz.jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import fr.poulpogaz.jam.utils.Size;
import fr.poulpogaz.jam.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private final GlyphLayout layout = new GlyphLayout();

    private final List<String> labels = new ArrayList<>();
    private float gap = 10;
    private int selected = 0;
    private boolean allowLooping = true;
    private int align = Align.center;

    private Color selectedColor = new Color(1, 1, 1, 1);
    private Color notSelectedColor = new Color(0.4f, 0.4f, 0.4f, 1);

    private boolean visible = true;

    public Menu() {

    }

    public void draw(SpriteBatch batch, BitmapFont font, float x, float y, Size s) {
        if (!visible) {
            return;
        }

        float h = Utils.fontHeight(font);

        for (int i = labels.size() - 1; i >= 0; i--) {
            String str = labels.get(i);

            if (i == selected) {
                font.setColor(selectedColor);
            } else {
                font.setColor(notSelectedColor);
            }

            font.draw(batch, str, x, y, s.width, align, false);

            y += h + gap;
        }
    }

    public void getPreferredSize(BitmapFont font, Size s) {
        s.width = 0;
        s.height = 0;

        for (int i = 0; i < labels.size(); i++) {
            String str = labels.get(i);
            layout.setText(font, str);

            s.width = Math.max(layout.width, s.width);
            s.height += layout.height;

            if (i + 1 < labels.size()) {
                s.height += gap;
            }
        }
    }

    public int update() {
        if (!visible) {
            return -1;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selected--;

            if (selected < 0) {
                if (allowLooping) {
                    selected = labels.size() - 1;
                } else {
                    selected = 0;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selected++;

            if (selected >= labels.size()) {
                if (allowLooping) {
                    selected = 0;
                } else {
                    selected = labels.size() - 1;
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            return selected;
        }

        return -1;
    }

    public void addLabel(String label) {
        labels.add(label);
    }

    public List<String> getLabels() {
        return labels;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        if (selected < 0 || selected >= labels.size()) {
            throw new IndexOutOfBoundsException();
        }

        this.selected = selected;

    }

    public boolean isAllowLooping() {
        return allowLooping;
    }

    public void setAllowLooping(boolean allowLooping) {
        this.allowLooping = allowLooping;
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (visible != this.visible) {
            this.visible = visible;
            selected = 0;
        }
    }

    public int getAlign() {
        return align;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor.set(selectedColor);
    }

    public Color getNotSelectedColor() {
        return notSelectedColor;
    }

    public void setNotSelectedColor(Color notSelectedColor) {
        this.notSelectedColor.set(notSelectedColor);
    }
}
