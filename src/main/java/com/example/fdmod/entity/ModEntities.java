package com.example.fdmod.entity;

import com.example.fdmod.Fdmod;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    // Deferred register for entity types
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Fdmod.MODID);

    // Define the PlayerBot entity type
    public static final RegistryObject<EntityType<PlayerBotEntity>> PLAYER_BOT =
            ENTITY_TYPES.register("player_bot",
                    () -> EntityType.Builder
                            .of(PlayerBotEntity::new, MobCategory.CREATURE)
                            .sized(0.6F, 1.8F)
                            .build("player_bot"));

    // Method to register the entity types with the mod event bus
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}