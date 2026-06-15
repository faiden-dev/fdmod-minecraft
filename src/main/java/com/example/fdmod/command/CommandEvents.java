package com.example.fdmod.command;

import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import com.example.fdmod.Fdmod;

@Mod.EventBusSubscriber(modid = Fdmod.MODID)

public class CommandEvents {
    @SubscribeEvent
    // Register the command to spawn a PlayerBotEntity
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        SpawnBotCommand.register(event.getDispatcher());
    }
}