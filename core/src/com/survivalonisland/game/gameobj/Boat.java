package com.survivalonisland.game.gameobj;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.survivalonisland.game.item.SlimeItem;

public final class Boat {

    private final Texture texture;
    private final Texture purposeSlimeTexture;
    private static int purpose = 0;
    private Vector2 position;

    public Boat(Vector2 position) {
        texture = new Texture("sprites\\gameObjects\\boat.png");
        purposeSlimeTexture = new SlimeItem().getTexture();
        this.position = position;
    }

    public Vector2 getPosition() {
        return this.position;
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getPurposeSlimeTexture() {
        return purposeSlimeTexture;
    }

    public int getAmountPurpose() {
        return purpose;
    }

    public void addAmountPurpose(int value) {
        purpose += value;
    }
}
