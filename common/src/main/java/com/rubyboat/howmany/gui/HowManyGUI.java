package com.rubyboat.howmany.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rubyboat.howmany.CommonMain;
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
    public static ArrayList<Identifier> trackedItems = new ArrayList<>();
    public static ArrayList<String> scripts = new ArrayList<>();
    public static final Identifier TRACKED_ITEM_TEXTURE = new Identifier(CommonMain.MOD_ID, "textures/gui/tracked_item.png");
    public static final Identifier TRACKED_ITEM_TEXTURE_SCRIPTED = new Identifier(CommonMain.MOD_ID, "textures/gui/tracked_item_script.png");
    public static boolean shouldToggleTracked;

    public static final int TOP_V = 0;
    public static final int CENTER_V = 20;
    public static final int BOTTOM_V = 39;
    public static final int SINGLE_V = 59;

    public static void drawUI(DrawContext drawContext, Inventory inv) {
        int originX = drawContext.getScaledWindowWidth() - 75;
        int originY = drawContext.getScaledWindowHeight() / 2 - (20 * trackedItems.size()) / 2;

        int i = 0;
        int size = 0;
        for(var item : trackedItems) {
            TrackedItemEntry itemEntry = new TrackedItemEntry(item);
            itemEntry.updateCount(inv);

            int v = getV(i, trackedItems.size() + scripts.size());

            drawEntry(drawContext, itemEntry, originX, originY + size, v);
            i++;
            size += getHeight(v);
        }
        for(var script : scripts) {
            TrackedItemEntry itemEntry = Scripting.parseScript(script, inv);

            int v = getV(i, trackedItems.size() + scripts.size());

            drawEntry(drawContext, itemEntry, originX, originY + size, v, TRACKED_ITEM_TEXTURE_SCRIPTED);
            i++;
            size += getHeight(v);
        }

    }

    public static void drawEntry(DrawContext context, TrackedItemEntry entry, int x, int y, int entryType) {
        drawEntry(context, entry, x, y, entryType, TRACKED_ITEM_TEXTURE);
    }

    public static void drawEntry(DrawContext context, TrackedItemEntry entry, int x, int y, int entryType, Identifier texture) {
        RenderSystem.setShader(GameRenderer::getRenderTypeTextSeeThroughProgram);
        context.drawTexture(texture, x, y, 0, entryType, 66, getHeight(entryType), 66, 80);

        context.drawItem(entry.getRegisteredItem().getDefaultStack(), x + 2, y + 2);


        context.setShaderColor(1,1,1,1f);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("x" + entry.getCount()), x + 30, y + 6, 0xffffff, true);
    }

    public static void toggleItem(Item item) {
        Identifier identifier = Registries.ITEM.getId(item);

        if(HowManyGUI.trackedItems.contains(identifier)) {
            HowManyGUI.trackedItems.remove(identifier);
        }else {
            HowManyGUI.trackedItems.add(identifier);
        }
    }




    public static int getV(int index, int count) {
        if(index == 0) {
            return count == 1 ? SINGLE_V : TOP_V;
        }
        if(index == count - 1) { // last in list
            return BOTTOM_V;
        }
        return CENTER_V;
    }

    public static int getHeight(int entryType) {
        assert(entryType == SINGLE_V || entryType == TOP_V || entryType == BOTTOM_V || entryType == CENTER_V);

        if(entryType == SINGLE_V) {
            return 21;
        }else if(entryType == TOP_V || entryType == BOTTOM_V) {
            return 20;
        }else {
            return 19;
        }
    }
}
