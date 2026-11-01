package com.anclaje.mixin;

import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Mantiene quieto al murciélago sin IA (anula empujones/knockback). */
@Mixin(Bat.class)
public abstract class BatMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void anclaje$stayStill(CallbackInfo ci) {
        Bat self = (Bat) (Object) this;
        if (self.isNoAi()) {
            self.setDeltaMovement(Vec3.ZERO);
        }
    }
}
