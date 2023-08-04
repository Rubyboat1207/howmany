package com.rubyboat.howmany.forge.gui;


import com.rubyboat.howmany.CommonMain;
import com.rubyboat.howmany.gui.HowManyGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommonMain.MOD_ID, value = Dist.CLIENT)
public class ForgeOverlayBoilerplate {

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGuiOverlayEvent e) {
        PlayerEntity pl = MinecraftClient.getInstance().player;
        assert pl != null;
//        HowManyGUI.drawUI(e.getGuiGraphics(), pl.getInventory());
        var c = e.getGuiGraphics();
        HowManyGUI.drawUI(c, pl.getInventory(), c.getScaledWindowWidth() - 75, c.getScaledWindowHeight() / 2);
    }
}
