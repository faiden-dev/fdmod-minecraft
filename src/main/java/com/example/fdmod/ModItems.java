package com.example.fdmod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    // Deferred register for items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Fdmod.MODID);

    // Create a new sword item
    public static final RegistryObject<Item> SWORD_67 = ITEMS.register("sword_67",
        () -> new SwordItem(
            Tiers.IRON,
            new Item.Properties().attributes(SwordItem.createAttributes(Tiers.IRON, 500, -0.2f))
        )
    );

    // Bot spawn item
    public static final RegistryObject<Item> BOT_SPAWN = ITEMS.register("bot_spawn",
        () -> new BotSpawnItem(new Item.Properties())
    );

    // Register the items with the event bus
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}