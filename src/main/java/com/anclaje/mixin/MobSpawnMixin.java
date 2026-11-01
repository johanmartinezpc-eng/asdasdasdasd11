package com.anclaje.mixin;

import com.anclaje.Grapple;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Endermite y silverfish generados con huevo de aparición o con /summon
 * quedan estáticos. Los naturales (bloques infestados, perlas) no cambian.
 */
@Mixin(Mob.class)
public abstract class MobSpawnMixin {

    @Inject(method = "finalizeSpawn", at = @At("RETURN"))
    private void anclaje$freezeOnSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
                                       EntitySpawnReason reason, SpawnGroupData groupData,
                                       CallbackInfoReturnable<SpawnGroupData> cir) {
        Mob self = (Mob) (Object) this;
        if (Grapple.isFreezable(self)
                && (reason == EntitySpawnReason.SPAWN_ITEM_USE || reason == EntitySpawnReason.COMMAND)) {
            Grapple.freeze(self);
        }
    }
}
