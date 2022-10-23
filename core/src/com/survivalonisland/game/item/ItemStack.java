package com.survivalonisland.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.survivalonisland.game.scene.GameScene;

public class ItemStack {

    private final String name;
    private final int id;
    private final Texture texture;
    private final Vector2 position;

    private final int maxCount;
    private final boolean isFood;
    private final int saturation;
    private int count;

    public ItemStack(Item item) {
        name = item.getName();
        id = item.getId();
        texture = item.getTexture();
        position = item.getLocation();
        maxCount = item.getMaxCount();
        isFood = item.isFood();
        saturation = item.getSaturation();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Texture getTexture() {
        return texture;
    }

    public Vector2 getLocation() {
        return position;
    }

    public int getMaxCount() {
        return maxCount;
    }
    public boolean isFood() {
        return isFood;
    }

    public int getSaturation() {
        return saturation;
    }

    public int getCount() {
        return count;
    }

    public int addCount(int value) {
        return count += value;
    }

    public int removeCount(int value) {
        return count -= value;
    }


}
