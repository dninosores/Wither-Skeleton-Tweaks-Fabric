package com.dninosores.wstweaks;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemImmolationBlade extends SwordItem {

    public static int FIRE_TIME = 5;
    public static void RegisterEvents() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.isSpectator()) {
                return ActionResult.PASS;
            }
            if (player.getEquippedStack(EquipmentSlot.MAINHAND).isOf(WitherSkeletonTweaks.BLAZE_BLADE) ||
                    player.getEquippedStack(EquipmentSlot.MAINHAND).isOf(WitherSkeletonTweaks.LAVA_BLADE)) {
                entity.setOnFireFor(FIRE_TIME);
            }
            return ActionResult.PASS;
        });
    }

    public ItemImmolationBlade() {
        super(new MaterialImmolationBlade(), 12, -4f,
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
        // target.setOnFireFor(150);
       // target.damage(DamageSource.mob(attacker), 12);
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
