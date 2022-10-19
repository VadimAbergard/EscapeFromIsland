package com.survivalonisland.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.survivalonisland.game.entity.Entity;
import com.survivalonisland.game.entity.Player;
import com.survivalonisland.game.item.Item;

import java.util.ArrayList;
import java.util.List;

public class CustomWorld {

    private static final List<Entity> entityInWorld = new ArrayList<>();
    private static final List<Item> itemInWorld = new ArrayList<>();

    public List<Entity> getEntitu() {
        return entityInWorld;
    }

    public List<Item> getItem() {
        return itemInWorld;
    }
    public List<Entity> getEntityInWorld() {
        return entityInWorld;
    }
    public List<Item> getItemInWorld() {
        return itemInWorld;
    }


}
