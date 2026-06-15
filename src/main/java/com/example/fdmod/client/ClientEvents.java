package com.example.fdmod.client;

import com.example.fdmod.Fdmod;
import com.example.fdmod.entity.ModEntities;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// Client-side event subscriber for the mod
@Mod.EventBusSubscriber(
    modid = Fdmod.MODID,
    bus = Mod.EventBusSubscriber.Bus.MOD,
    value = Dist.CLIENT
)

public class ClientEvents {
    @SubscribeEvent
    // Register the renderer for the PlayerBot entity
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(
            ModEntities.PLAYER_BOT.get(),
            PlayerBotRenderer::new
        );
    }
}