package com.anclaje.mixin;

import com.anclaje.Grapple;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Mantiene quietos a los endermite/silverfish estáticos (anula empujones). */
@Mixin(Mob.class)
public abstract class MobTickMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void anclaje$stayStill(CallbackInfo ci) {
        Mob self = (Mob) (Object) this;
        if (Grapple.isStatic(self)) {
            self.setDeltaMovement(Vec3.ZERO);
        }
    }
}
