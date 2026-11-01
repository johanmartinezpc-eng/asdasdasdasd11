package com.anclaje.mixin;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Los murciélagos colocados con huevo de aparición nacen estáticos
 * (sin IA, sin gravedad, sin despawn). Los naturales de las cuevas no cambian.
 */
@Mixin(Mob.class)
public abstract class MobSpawnMixin {

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void anclaje$staticBat(ServerLevelAccessor level, DifficultyInstance difficulty,
                                   EntitySpawnReason reason, SpawnGroupData groupData,
                                   CallbackInfoReturnable<SpawnGroupData> cir) {
        if ((Object) this instanceof Bat bat && reason == EntitySpawnReason.SPAWN_ITEM_USE) {
            bat.setNoAi(true);               // se guarda en NBT (NoAI)
            bat.setNoGravity(true);
            bat.setResting(false);
            bat.setPersistenceRequired();    // no desaparece
        }
    }
}
