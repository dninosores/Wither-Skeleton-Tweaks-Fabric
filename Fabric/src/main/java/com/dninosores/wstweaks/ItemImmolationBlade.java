package com.dninosores.wstweaks;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemImmolationBlade extends SwordItem {

    public static int FIRE_TIME = 5;

    public ItemImmolationBlade() {
        super(new MaterialImmolationBlade(), 12, -2f,
                new FabricItemSettings().group(ItemGroup.COMBAT).rarity(Rarity.EPIC));
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

    public static void RegisterEvents() {
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (player.isSpectator()) {
                return ActionResult.PASS;
            }

            if (player.getEquippedStack(EquipmentSlot.MAINHAND).isOf(WitherSkeletonTweaks.BLAZE_BLADE) ||
                    player.getEquippedStack(EquipmentSlot.MAINHAND).isOf(WitherSkeletonTweaks.LAVA_BLADE)) {

                entity.setOnFireFor(FIRE_TIME);

                if (entity instanceof AbstractSkeletonEntity skeleton) {
                    skeleton.setHealth(1);
                    skeleton.damage(DamageSource.mob(player), 150);
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

            return ActionResult.PASS;
        });
    }

}
