package com.rubyboat.howmany.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.inventory.Inventory;
import net.minecraft.text.Text;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

import java.util.Collections;
import java.util.HashMap;

import static com.rubyboat.howmany.gui.HowManyGUI.trackedItems;

public class EditScreen extends Screen {

    int selectedEntryIndex = -1;
    Vector2i[] entryPosition;
    int selectedDistance = 5;

    InputUtil.Key upArrowKey = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_UP);
    InputUtil.Key downArrowKey = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_DOWN);
    InputUtil.Key deleteKey = InputUtil.Type.KEYSYM.createFromCode(GLFW.GLFW_KEY_DELETE);

    HashMap<InputUtil.Key, Boolean> keyPressMap = new HashMap<>();


    public EditScreen() {
        super(Text.of("edit screen"));

        keyPressMap.put(upArrowKey, false);
        keyPressMap.put(downArrowKey, false);
        keyPressMap.put(deleteKey, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Inventory inv;
        if(MinecraftClient.getInstance().player != null) {
            inv = MinecraftClient.getInstance().player.getInventory();
        }else {
            inv = new EmptyInventory();
        }

//        context.drawText(this.textRenderer, mouseX + ", " + mouseY,mouseX, mouseY + 20, 0xffffff, true);


        int i = 0;
        int size = 0;
        entryPosition = new Vector2i[trackedItems.size()];
        for(var item : trackedItems) {
            item.updateCount(inv);

            HowManyGUI.EntryStyle style = HowManyGUI.getEntryStyle(i, trackedItems.size());

            if(selectedEntryIndex == i) {
                style = HowManyGUI.EntryStyle.SINGLE;
            }

            entryPosition[i] = new Vector2i(
                (context.getScaledWindowWidth() / 2) - 33,
                (context.getScaledWindowHeight() / 2 - (20 * trackedItems.size()) / 2) + size
            );
            if(selectedEntryIndex != -1) {
                if(i < selectedEntryIndex) {
                    entryPosition[i].y -= selectedDistance;
                }else if(i > selectedEntryIndex) {
                    entryPosition[i].y += selectedDistance;
                }
            }

            HowManyGUI.drawEntry(context, item, entryPosition[i].x, entryPosition[i].y, style, item.getTexture());
            i++;
            size += style.height;
        }

        var handle = MinecraftClient.getInstance().getWindow().getHandle();
        HashMap<InputUtil.Key, Boolean> set = (HashMap<InputUtil.Key, Boolean>) keyPressMap.clone();

        keyPressMap.replaceAll((k, v) -> InputUtil.isKeyPressed(handle, k.getCode()));

        if(wasPressed(upArrowKey, set) && selectedEntryIndex - 1 >= 0) {
            Collections.swap(trackedItems, selectedEntryIndex, selectedEntryIndex - 1);
            selectedEntryIndex--;
        }else if(wasPressed(downArrowKey, set) && selectedEntryIndex + 1 < trackedItems.size()) {
            Collections.swap(trackedItems, selectedEntryIndex, selectedEntryIndex + 1);
            selectedEntryIndex++;
        }else if(wasPressed(deleteKey, set)) {
            trackedItems.remove(selectedEntryIndex);
            selectedEntryIndex = -1;
        }

        super.render(context, mouseX, mouseY, delta);
    }

    public boolean wasPressed(InputUtil.Key key, HashMap<InputUtil.Key, Boolean> oldSet, HashMap<InputUtil.Key, Boolean> newSet) {
        return (!oldSet.get(key) && newSet.get(key));
    }

    public boolean wasPressed(InputUtil.Key key, HashMap<InputUtil.Key, Boolean> oldSet) {
        return wasPressed(key, oldSet, keyPressMap);
    }

    public int getEntryIndexAt(double x, double y) {
        final int entryWidth = 66;

        for (int i = 0; i < entryPosition.length; i++) {
            var pos = entryPosition[i];

            if(x < pos.x || x > pos.x + entryWidth) {
                continue;
            }

            HowManyGUI.EntryStyle style = HowManyGUI.getEntryStyle(i, entryPosition.length);

            if(selectedEntryIndex == i) {
                style = HowManyGUI.EntryStyle.SINGLE;
            }

            if(y < pos.y || y > pos.y + style.height) {
                continue;
            }

            return i;
        }

        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        selectedEntryIndex = getEntryIndexAt(mouseX, mouseY);

        return true;
    }
}
