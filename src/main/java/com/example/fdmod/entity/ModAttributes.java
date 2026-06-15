package com.example.fdmod.entity;

import com.example.fdmod.Fdmod;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fdmod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModAttributes {
    @SubscribeEvent
    // Register the attributes for the PlayerBot entity
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(
            ModEntities.PLAYER_BOT.get(),
            PlayerBotEntity.createAttributes().build()
        );
    }
}