package com.dninosores.wstweaks.mixins;

import com.dninosores.wstweaks.WitherSkeletonTweaks;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.dninosores.wstweaks.ItemImmolationBlade.FIRE_TIME;

@Mixin(LivingEntity.class)
public abstract class ItemImmolationDamageMixin extends Entity {

    @Shadow public abstract void setHealth(float health);

    public ItemImmolationDamageMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(at = @At("HEAD"), method = "damage")
    private void init(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() != null) {
            if (source.getAttacker() instanceof LivingEntity attacker && !attacker.isSpectator()) {
                if (attacker.getEquippedStack(EquipmentSlot.MAINHAND).isOf(WitherSkeletonTweaks.BLAZE_BLADE) ||
                        attacker.getEquippedStack(EquipmentSlot.MAINHAND).isOf(WitherSkeletonTweaks.LAVA_BLADE)) {

                    this.setOnFireFor(FIRE_TIME);

                    if (((LivingEntity) (Object) this) instanceof AbstractSkeletonEntity skeleton) {
                        this.setHealth(1);
                        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, skeleton.getWorld());
                        lightning.setPosition(skeleton.getPos());
                        lightning.setCosmetic(true);
                        skeleton.getWorld().spawnEntity(lightning);
                        double i = skeleton.getWorld().random.nextDouble() * 4.0D;
                        double d = skeleton.getWorld().random.nextDouble() * 4.0D;
                        double k = skeleton.getWorld().random.nextDouble() * 4.0D;
                        skeleton.setVelocity(2.0D - i, d, 2.0D - k);
                    }
                }
            }
        }
    }
}