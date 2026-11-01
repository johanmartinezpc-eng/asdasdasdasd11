package com.anclaje.mixin;

import com.anclaje.Grapple;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {

    /**
     * Con cualquier entidad enganchada (mob u objeto) al recoger la caña:
     * en vez de jalar a la entidad, impulsa al jugador hacia ella.
     * Como vanilla, el gancho se suelta después (silverfish incluido).
     */
    @Inject(method = "pullEntity", at = @At("HEAD"), cancellable = true)
    private void anclaje$pullPlayer(Entity hooked, CallbackInfo ci) {
        ci.cancel(); // anula el comportamiento original (jalar al mob)
        FishingHook self = (FishingHook) (Object) this;
        if (self.getPlayerOwner() instanceof ServerPlayer player) {
            Grapple.pullPlayer(player, hooked);
        }
    }

    /**
     * Endermite = ancla: el impulso se aplica pero el gancho NO se suelta,
     * así puedes jalarte varias veces. Con Shift + clic derecho te impulsas
     * y se suelta (comportamiento normal).
     */
    @Inject(method = "retrieve", at = @At("HEAD"), cancellable = true)
    private void anclaje$keepHookOnEndermite(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        FishingHook self = (FishingHook) (Object) this;
        Player owner = self.getPlayerOwner();
        Entity hooked = self.getHookedIn();

        if (owner instanceof ServerPlayer player
                && hooked != null
                && hooked.getType() == EntityType.ENDERMITE
                && !player.isShiftKeyDown()) {
            Grapple.pullPlayer(player, hooked);
            cir.setReturnValue(Grapple.ANCHOR_DURABILITY_COST);
        }
    }
}
