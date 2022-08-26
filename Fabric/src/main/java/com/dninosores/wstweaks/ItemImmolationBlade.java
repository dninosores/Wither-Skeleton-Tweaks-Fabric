package com.dninosores.wstweaks;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemImmolationBlade extends SwordItem {
    public ItemImmolationBlade() {
        super(ToolMaterials.DIAMOND, 12, -2f,
                new FabricItemSettings().group(ItemGroup.COMBAT));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (Screen.hasShiftDown()) {
            tooltip.add(new TranslatableText("tooltip.wstweaks.immolationblade-shift1"));
            tooltip.add(new TranslatableText("tooltip.wstweaks.immolationblade-shift2"));
            tooltip.add(new TranslatableText("tooltip.wstweaks.immolationblade-shift3"));
        } else {
            tooltip.add(new TranslatableText("tooltip.wstweaks.immolationblade1"));
            tooltip.add(new TranslatableText("tooltip.wstweaks.immolationblade2"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.setOnFireFor(150);
        super.postHit(stack, target, attacker);
        if (target instanceof AbstractSkeletonEntity) {
            target.setHealth(1);
            target.damage(DamageSource.mob(attacker), 150);
            LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, target.getWorld());
            lightning.setPosition(target.getPos());
            lightning.setCosmetic(true);
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
