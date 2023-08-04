package com.rubyboat.howmany.entry;

import com.google.gson.JsonObject;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public abstract class TrackedEntry {
    int count = 0;
    String type = "";

    public TrackedEntry(JsonObject jsonObject) {
        if(jsonObject == null) {
            return;
        }
        this.type = jsonObject.getAsJsonObject().get("type").getAsString();
    }

    public abstract Identifier getTexture();

    public int getCount() {
        return count;
    }
    public int getCount(Inventory inv) {
        updateCount(inv);
        return count;
    }
    public abstract void updateCount(Inventory inv);
    public abstract String getType();
    public abstract Item getIcon();
    public JsonObject toJSON() {
        var obj = new JsonObject();

        obj.addProperty("type", type);

        return obj;
    }
}
