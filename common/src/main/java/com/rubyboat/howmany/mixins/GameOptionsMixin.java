package com.rubyboat.howmany.mixins;

import com.rubyboat.howmany.CommonMain;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(method = "setKeyCode", at = @At(value = "HEAD"))
    public void setKeycode(KeyBinding key, InputUtil.Key code, CallbackInfo ci) {
        if(key == CommonMain.RELOAD_CONFIG_KEYBIND) {
//            CommonMain.trackBindKeycode = code;
        }
    }
}
