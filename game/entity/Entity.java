package com.survivalonisland.game.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public interface Entity {
    Vector2 getLocation();
    void spawn(Vector2 positon);
    void setSpeed(float speed);
    void removeHealth(int value);
    void addHealth(int value);
    int getHealth();
    void update(float delta);
    void kill();
    TextureRegion getFrame();
    boolean isMove();
    boolean isDead();
    boolean isHit();
    boolean isAttack();
    Body getBox();
    void dispose();
    void setBox(Body box);
}
