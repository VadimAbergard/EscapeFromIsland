package com.survivalonisland.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.survivalonisland.game.map.CustomWorld;

import javax.xml.soap.Text;

public class Axe implements Item {

    private Vector2 location;
    private Texture texture = new Texture("sprites\\items\\axe.png");

    @Override
    public int getId() {
        return 2;
    }

    @Override
    public String getName() {
        return "Axe";
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
        return 1;
    }

    @Override
    public boolean isFood() {
        return false;
    }

    @Override
    public int getSaturation() {
        return 0;
    }

}
