package com.rubyboat.howmany.mixins;

import com.rubyboat.howmany.save.Serializer;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(method="close", at = @At(value = "HEAD"))
    public void close(CallbackInfo ci) {
        try {
            Serializer.Save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
