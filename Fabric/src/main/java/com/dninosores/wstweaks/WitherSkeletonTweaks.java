package com.dninosores.wstweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WitherSkeletonTweaks implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String NAMESPACE = "wstweaks";
    public static final String FRAGMENT_ID = "fragment";


    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    public static final Item FRAGMENT = new Item(new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item LAVA_BLADE = new ItemImmolationBlade();
    public static final Item BLAZE_BLADE = new ItemImmolationBlade();

    public static final Identifier WITHER_SKELETON_TABLE =
            new Identifier("minecraft", "entities/wither_skeleton");
    public static final Identifier SKELETON_TABLE =
            new Identifier("minecraft", "entities/skeleton");
    public static final Identifier STRAY_TABLE =
            new Identifier("minecraft", "entities/stray");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Initializing Wither Skeleton Tweaks (Fabric)");

        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, FRAGMENT_ID), FRAGMENT);
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "lava_blade"), LAVA_BLADE);
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "blaze_blade"), BLAZE_BLADE);

        ItemImmolationBlade.RegisterEvents();

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
