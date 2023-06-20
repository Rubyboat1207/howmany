package com.rubyboat.howmany.gui;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class TrackedItemEntry {
    private int count;
    private final Identifier item;
    public boolean isArbitrary = false;

    public TrackedItemEntry(int count, Identifier item) {
        this.count = count;
        this.item = item;
    }

    public TrackedItemEntry(Identifier item) {
        this(0, item);
    }

    public void updateCount(Inventory inv) {
        count = inv.count(getRegisteredItem());
    }

    public int getCount() {
        return count;
    }

    public int getCount(Inventory inv) {
        updateCount(inv);
        return count;
    }

    public void setCount(int count) {
        isArbitrary = true;
        this.count = count;
    }

//    public Identifier getItem() {
//        return item;
//    }

    public Item getRegisteredItem() {
        return Registries.ITEM.get(item);
    }
}
