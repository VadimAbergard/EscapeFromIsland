package com.survivalonisland.game.addition;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.survivalonisland.game.item.Apple;
import com.survivalonisland.game.item.Item;
import com.survivalonisland.game.item.ItemStack;
import com.survivalonisland.game.item.SlimeItem;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private int sizeInvetory;
    private final ItemStack[] slots;
    private SpriteBatch batch = new SpriteBatch();
    private OrthographicCamera camera;

    public Inventory(OrthographicCamera camera) {
        sizeInvetory = 24;
        slots = new ItemStack[sizeInvetory];
        this.camera = camera;

        slots[0] = new ItemStack(new Apple());
        slots[0].addCount(10);
    }

    public void setSize(int size) {
        sizeInvetory = size;
    }

    public int getSize() {
        return sizeInvetory;
    }

    public ItemStack getSlot(int slot) {
        return slots[slot];
    }

    public void setSlot(int slot, ItemStack item) {
        slots[slot] = item;
    }

    public void addItem(ItemStack item) {
        for(int i = 0;i < sizeInvetory;i++) {
            if (slots[i] == null) continue;
        }

        for(int i = 0;i < sizeInvetory;i++) {
            if(slots[i] == null) {
                slots[i] = item;
                slots[i].addCount(1);
                return;
            } else if(item.getId() == slots[i].getId()) {
                if(slots[i].getCount() + item.getCount() >= item.getMaxCount()) {
                    for(int l = 0;l < sizeInvetory;l++) {
                        final int ignoredSlot = i;
                        if(l == ignoredSlot) continue;

                        if(slots[l] == null)  {
                            slots[l] = item;
                            slots[l].addCount(1);
                            break;
                        } else if(item.getId() == slots[l].getId()) {
                            slots[l].addCount(1);
                            break;
                        }
                    }
                    return;
                }
                slots[i].addCount(1);
                return;
            }
        }
        System.out.println("inventory full!");
    }

    public void deleteItemInSlot(int slot) {
        slots[slot] = null;
    }

    public void debug() {
        System.out.println("");
        for(int i = 0;i < sizeInvetory;i++) {
            if(slots[i] == null) {
                System.out.println("inv debug | slot " + i + " = null");
                continue;
            }
            System.out.println("inv debug | slot " + i + ", item " + slots[i].getName() + ", count " + slots[i].getCount());
        }
        System.out.println("");
    }
}
