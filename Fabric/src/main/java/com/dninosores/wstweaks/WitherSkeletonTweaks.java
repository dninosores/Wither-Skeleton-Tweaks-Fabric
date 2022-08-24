package com.dninosores.wstweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.loot.v2.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.system.CallbackI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WitherSkeletonTweaks implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String NAMESPACE = "wstweaks";
    public static final String FRAGMENT_ID = "fragment";
    public static final String BLAZE_BLADE_ID = "blaze_blade";
    public static final String LAVA_BLADE_ID = "lava_blade";



    public static final Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);
    public static final Item FRAGMENT = new Item(new FabricItemSettings().group(ItemGroup.MISC));

    public static final Identifier WITHER_SKELETON_TABLE =
            new Identifier("minecraft", "entities/wither_skeleton");

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Initializing Wither Skeleton Tweaks (Fabric)");

        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, FRAGMENT_ID), FRAGMENT);
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "lava_blade"), new ItemImmolationBlade());
        Registry.register(Registry.ITEM, new Identifier(NAMESPACE, "blaze_blade"), new ItemImmolationBlade());

        LootTableEvents.MODIFY.register((ResourceManager resourceManager, LootManager lootManager, Identifier id, LootTable.Builder tableBuilder, LootTableSource source) -> {
            if (WITHER_SKELETON_TABLE.equals(id)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.9f).build())
                        .with(ItemEntry.builder(FRAGMENT));
                tableBuilder.pool(poolBuilder);
            }
        });
    }
}
