package com.survivalonisland.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.survivalonisland.game.map.CustomWorld;

public class Apple implements Item {

    private Vector2 location;
    private Texture texture = new Texture("sprites\\items\\apple.png");

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getName() {
        return "Apple";
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public Vector2 getLocation() {
        return location;
    }

    @Override
    public void spawn(Vector2 positon) {
        this.location = positon;
        new CustomWorld().getItem().add(this);
    }

    @Override
    public int getMaxCount() {
        return 16;
    }

    @Override
    public boolean isFood() {
        return true;
    }

    @Override
    public int getSaturation() {
        return 10;
    }

}
