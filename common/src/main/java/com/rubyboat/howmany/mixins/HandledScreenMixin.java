package com.rubyboat.howmany.mixins;

import com.rubyboat.howmany.CommonMain;
import com.rubyboat.howmany.gui.HowManyGUI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    boolean wasPressed;
    @Shadow @Nullable protected Slot focusedSlot;

    @SuppressWarnings("ConstantConditions")
    @Inject(method = "tick", at = @At(value = "HEAD"))
    void tick(CallbackInfo ci) {
        var isPressed = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), CommonMain.trackBindKeycode.getCode());

        if(
            ((Object) this) instanceof CreativeInventoryScreen ||
            ((Object) this) instanceof AnvilScreen
        ) {
            return;
        }


        if(isPressed && !wasPressed) {
            wasPressed = true;
            if(this.focusedSlot != null) {
                var item = this.focusedSlot.getStack().getItem();
                if(item == Items.AIR) {
                    return;
                }
                HowManyGUI.toggleItem(item);
            }
        }else if(!isPressed) {
            wasPressed = false;
        }

    }
}
