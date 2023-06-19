package com.rubyboat.howmany.forge.event;

import com.rubyboat.howmany.CommonMain;
import com.rubyboat.howmany.gui.HowManyGUI;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {
    @Mod.EventBusSubscriber(modid = CommonMain.MOD_ID, value = Dist.CLIENT)
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key key) {
            HowManyGUI.shouldToggleTracked = CommonMain.TRACK_ITEM_KEYBIND.wasPressed();
        }
    }

    @Mod.EventBusSubscriber(modid = CommonMain.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister (RegisterKeyMappingsEvent event) {
            if(CommonMain.registerKeybind) {
                event.register(CommonMain.TRACK_ITEM_KEYBIND);
            }
        }
    }
}
