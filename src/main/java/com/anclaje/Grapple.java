package com.anclaje;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public final class Grapple {
    private Grapple() {}

    /** Fuerza del jalón (vanilla usa 0.1 sobre la entidad). */
    public static final double STRENGTH = 0.2;
    /** Límite de velocidad añadida por cada jalón. */
    public static final double MAX_IMPULSE = 2.5;
    /** Desgaste de la caña por cada jalón al endermite (el gancho se queda pegado). */
    public static final int ANCHOR_DURABILITY_COST = 1;

    /** Endermite y silverfish son los mobs que pueden quedar estáticos. */
    public static boolean isFreezable(Entity e) {
        EntityType<?> t = e.getType();
        return t == EntityType.ENDERMITE || t == EntityType.SILVERFISH;
    }

    /** Endermite/silverfish sin IA = estático (huevo, /summon, o NoAI:1b). */
    public static boolean isStatic(Entity e) {
        return e instanceof Mob mob && isFreezable(e) && mob.isNoAi();
    }

    /** Congela al mob: sin IA, sin gravedad y sin despawn. */
    public static void freeze(Mob mob) {
        mob.setNoAi(true);
        mob.setNoGravity(true);
        mob.setPersistenceRequired();
    }

    /** Impulsa al jugador hacia la entidad enganchada (la entidad no se mueve). */
    public static void pullPlayer(ServerPlayer player, Entity target) {
        Vec3 to = target.position().add(0, target.getBbHeight() * 0.5, 0);
        Vec3 from = player.position().add(0, player.getBbHeight() * 0.5, 0);

        Vec3 impulse = to.subtract(from).scale(STRENGTH);
        if (impulse.length() > MAX_IMPULSE) {
            impulse = impulse.normalize().scale(MAX_IMPULSE);
        }

        player.setDeltaMovement(player.getDeltaMovement().add(impulse));
        player.resetFallDistance(); // sin daño de caída tras el impulso
        player.connection.send(new ClientboundSetEntityMotionPacket(player));
    }
}
