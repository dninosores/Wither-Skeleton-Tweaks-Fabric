package com.dninosores.wstweaks;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.DamageSourcePropertiesLootCondition;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.entity.DamageSourcePredicate;
import net.minecraft.predicate.entity.EntityEquipmentPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

import static com.dninosores.wstweaks.WitherSkeletonTweaks.*;

public class ItemImmolationBlade extends SwordItem {

    public static int FIRE_TIME = 5;

    public ItemImmolationBlade() {
        super(new MaterialImmolationBlade(), 12, -3.69f,
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

        LootTableEvents.MODIFY.register((ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable.Builder tableBuilder, LootTableSource source) -> {
            LootPool.Builder skullDrop = LootPool.builder()
                    .rolls(ConstantLootNumberProvider.create(1))
                    .conditionally(DamageSourcePropertiesLootCondition.builder(DamageSourcePredicate.Builder.create().sourceEntity(
                            EntityPredicate.Builder.create().equipment(
                                            EntityEquipmentPredicate.Builder.create().mainhand(
                                                            ItemPredicate.Builder.create().items(LAVA_BLADE, BLAZE_BLADE)
                                                                    .build())
                                                    .build())
                                    .build())
                    ));

            if (WITHER_SKELETON_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.9f).build())
                        .with(ItemEntry.builder(FRAGMENT));
                tableBuilder.pool(poolBuilder);

                LootPool.Builder poolBuilder2 =
                        skullDrop.with(ItemEntry.builder(Items.WITHER_SKELETON_SKULL));
                tableBuilder.pool(poolBuilder2);
            }
            if (SKELETON_TABLE.equals(id) || STRAY_TABLE.equals(id)) {
                LootPool.Builder poolBuilder2 =
                        skullDrop.with(ItemEntry.builder(Items.SKELETON_SKULL));
                tableBuilder.pool(poolBuilder2);
            }
        });
    }

}
