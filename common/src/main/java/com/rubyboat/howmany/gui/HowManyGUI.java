package com.rubyboat.howmany.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rubyboat.howmany.CommonMain;
import com.rubyboat.howmany.entry.TrackedEntry;
import com.rubyboat.howmany.entry.TrackedItemEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class HowManyGUI {
    public static ArrayList<TrackedEntry> trackedItems = new ArrayList<>();
    public static final Identifier TRACKED_ITEM_TEXTURE = new Identifier(CommonMain.MOD_ID, "textures/gui/tracked_item.png");
    public static final Identifier TRACKED_ITEM_TEXTURE_SCRIPTED = new Identifier(CommonMain.MOD_ID, "textures/gui/tracked_item_script.png");
    public static boolean shouldToggleTracked;

//    public static final int TOP_V = 0;
//    public static final int CENTER_V = 20;
//    public static final int BOTTOM_V = 39;
//    public static final int SINGLE_V = 59;

    public enum EntryStyle {
        TOP(0,20),
        CENTER(20, 19),
        BOTTOM(39, 20),
        SINGLE(59, 21);

        public final int v_offset;
        public final int height;
        EntryStyle(int v_offset, int height) {
            this.v_offset = v_offset;
            this.height = height;
        }
    }

    public static void drawUI(DrawContext drawContext, Inventory inv, int originX, int originY) {
        int i = 0;
        int size = 0;
        for(var item : trackedItems) {
            item.updateCount(inv);

            EntryStyle entryType = getEntryStyle(i, trackedItems.size());

            drawEntry(drawContext, item, originX, (originY - (20 * trackedItems.size()) / 2) + size, entryType, item.getTexture());
            i++;
            size += entryType.height;
        }

    }

    public static void drawEntry(DrawContext context, TrackedEntry entry, int x, int y, EntryStyle entryType) {
        drawEntry(context, entry, x, y, entryType, TRACKED_ITEM_TEXTURE);
    }

    public static void drawEntry(DrawContext context, TrackedEntry entry, int x, int y, EntryStyle entryType, Identifier texture) {
        RenderSystem.setShader(GameRenderer::getRenderTypeTextSeeThroughProgram);
        context.drawTexture(texture, x, y, 0, entryType.v_offset, 66, entryType.height, 66, 80);

        context.drawItem(entry.getIcon().getDefaultStack(), x + 2, y + 2);


        context.setShaderColor(1,1,1,1f);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("x" + entry.getCount()), x + 30, y + 6, 0xffffff, true);
    }

    public static void toggleItem(Item item) {
        Identifier identifier = Registries.ITEM.getId(item);

        if(!HowManyGUI.trackedItems.removeIf((i) ->
            Registries.ITEM.getId(i.getIcon()) == identifier &&
            i instanceof TrackedItemEntry
        )) {
            HowManyGUI.trackedItems.add(new TrackedItemEntry(identifier));
        }
    }


    public static EntryStyle getEntryStyle(int index, int count) {
        if(index == 0) {
            return count == 1 ? EntryStyle.SINGLE : EntryStyle.TOP;
        }
        if(index == count - 1) { // last in list
            return EntryStyle.BOTTOM;
        }
        return EntryStyle.CENTER;
    }
}
