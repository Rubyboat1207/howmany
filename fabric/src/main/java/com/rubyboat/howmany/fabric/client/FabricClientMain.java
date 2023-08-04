package com.rubyboat.howmany.fabric.client;

import com.rubyboat.howmany.CommonMain;
import com.rubyboat.howmany.gui.HowManyGUI;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

public class FabricClientMain implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CommonMain.init();
        HudRenderCallback.EVENT.register((DrawContext c, float tickDelta) -> {
            PlayerEntity pl = MinecraftClient.getInstance().player;
            assert pl != null;
            HowManyGUI.drawUI(c, pl.getInventory(), c.getScaledWindowWidth() - 75, c.getScaledWindowHeight() / 2);
        });

        if(CommonMain.registerKeybind) {
            CommonMain.RELOAD_CONFIG_KEYBIND = KeyBindingHelper.registerKeyBinding(CommonMain.RELOAD_CONFIG_KEYBIND);
            CommonMain.OPEN_EDIT_MENU_KEYBIND = KeyBindingHelper.registerKeyBinding(CommonMain.OPEN_EDIT_MENU_KEYBIND);
        }

        ClientTickEvents.END_CLIENT_TICK.register((t) -> {
            CommonMain.keybindTick();
        });
    }
}
