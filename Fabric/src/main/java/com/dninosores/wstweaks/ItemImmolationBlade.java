package com.dninosores.wstweaks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;

public class ItemImmolationBlade extends SwordItem {
    public ItemImmolationBlade() {
        super(ToolMaterials.DIAMOND, 12, -2f,
                new FabricItemSettings().group(ItemGroup.COMBAT));
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(150);
        super.postHit(stack, target, attacker);
        if (target instanceof AbstractSkeletonEntity) {
            target.setHealth(1);
            target.damage(DamageSource.LIGHTNING_BOLT, 150);
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, target.getWorld());
            lightning.setPosition(target.getPos());
            target.getWorld().spawnEntity(lightning);
            double i = target.getWorld().random.nextDouble() * 4.0D;
            double d = target.getWorld().random.nextDouble() * 4.0D;
            double k = target.getWorld().random.nextDouble() * 4.0D;
            target.setVelocity(2.0D - i, d, 2.0D - k);
            return true;
        }
        return true;
    }
}
