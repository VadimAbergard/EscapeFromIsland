package com.survivalonisland.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public interface Item {
    int getId();
    String getName();
    Texture getTexture();
    Vector2 getLocation();
    void spawn(Vector2 positon);
    int getMaxCount();
    boolean isFood();
    int getSaturation();
}
