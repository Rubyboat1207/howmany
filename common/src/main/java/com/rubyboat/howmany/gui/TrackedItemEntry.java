package com.rubyboat.howmany.gui;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class TrackedItemEntry {
    public int count;
    private final Identifier item;

    public TrackedItemEntry(int count, Identifier item) {
        this.count = count;
        this.item = item;
    }

//    public Identifier getItem() {
//        return item;
//    }

    public Item getRegisteredItem() {
        return Registries.ITEM.get(item);
    }
}
