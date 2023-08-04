package com.rubyboat.howmany.entry;

import com.google.gson.JsonObject;
import com.rubyboat.howmany.gui.HowManyGUI;
import com.rubyboat.howmany.save.Serializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class TrackedItemEntry extends TrackedEntry {
    private int count;
    private final Identifier item;
    public boolean isArbitrary = false;

    public TrackedItemEntry(Identifier item) {
        super(null);
        this.item = item;
        this.type = "item";
    }

    public TrackedItemEntry(JsonObject jsonObject) {
        super(jsonObject);
        this.item = new Identifier(jsonObject.get("item").getAsString());
        this.type = "item";
    }

    public void updateCount(Inventory inv) {
        count = inv.count(getIcon());
    }

    public int getCount() {
        return count;
    }

    public int getCount(Inventory inv) {
        updateCount(inv);
        return count;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Item getIcon() {
        return Registries.ITEM.get(item);
    }

    @Override
    public JsonObject toJSON() {
        var obj = super.toJSON();

        obj.addProperty("item", item.toString());

        return obj;
    }

    public void setCount(int count) {
        isArbitrary = true;
        this.count = count;
    }

    @Override
    public Identifier getTexture() {
        return HowManyGUI.TRACKED_ITEM_TEXTURE;
    }
}
