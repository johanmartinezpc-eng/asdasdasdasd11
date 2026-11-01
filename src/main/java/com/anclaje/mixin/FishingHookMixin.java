package com.anclaje.mixin;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Al recoger la caña con algo enganchado, en vez de jalar a la entidad
 * (mob u objeto), impulsa al jugador hacia ella. La entidad no se mueve.
 */
@Mixin(FishingHook.class)
public abstract class FishingHookMixin {

    /** Fuerza del jalón (vanilla usa 0.1 sobre la entidad). */
    private static final double STRENGTH = 0.2;
    /** Límite de velocidad añadida por cada jalón. */
    private static final double MAX_IMPULSE = 2.5;

    @Inject(method = "pullEntity", at = @At("HEAD"), cancellable = true)
    private void anclaje$pullPlayer(Entity hooked, CallbackInfo ci) {
        ci.cancel(); // anula el comportamiento original (jalar al mob)

        FishingHook self = (FishingHook) (Object) this;
        Player owner = self.getPlayerOwner();
        if (!(owner instanceof ServerPlayer player)) {
            return;
        }

        Vec3 target = hooked.position().add(0, hooked.getBbHeight() * 0.5, 0);
        Vec3 origin = player.position().add(0, player.getBbHeight() * 0.5, 0);

        Vec3 impulse = target.subtract(origin).scale(STRENGTH);
        if (impulse.length() > MAX_IMPULSE) {
            impulse = impulse.normalize().scale(MAX_IMPULSE);
        }

        player.setDeltaMovement(player.getDeltaMovement().add(impulse));
        player.resetFallDistance(); // sin daño de caída tras el impulso
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }
}
