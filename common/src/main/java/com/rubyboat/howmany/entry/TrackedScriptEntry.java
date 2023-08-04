package com.rubyboat.howmany.entry;

import com.google.gson.JsonObject;
import com.rubyboat.howmany.gui.HowManyGUI;
import com.rubyboat.howmany.gui.Scripting;
import com.rubyboat.howmany.save.Serializer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class TrackedScriptEntry extends TrackedEntry {
    String script;

    public TrackedScriptEntry(String script) {
        super(null);
        this.type = "inline_script";
        this.script = script;
    }

    public TrackedScriptEntry(JsonObject jsonObject) {
        super(jsonObject);
        this.type = "inline_scripted";
        this.script = jsonObject.get("script").getAsString();
    }

    @Override
    public Identifier getTexture() {
        return HowManyGUI.TRACKED_ITEM_TEXTURE_SCRIPTED;
    }

    @Override
    public void updateCount(Inventory inv) {
        this.count = Scripting.parseScript(script, inv);
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Item getIcon() {
        return Registries.ITEM.get(Scripting.getIconIdentifier(script));
    }

    @Override
    public JsonObject toJSON() {
        var obj = super.toJSON();

        obj.addProperty("script", this.script);

        return obj;
    }
}
