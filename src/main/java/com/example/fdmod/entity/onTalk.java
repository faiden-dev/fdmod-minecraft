package com.example.fdmod.entity;

import net.minecraft.server.level.ServerPlayer;

public class onTalk {

    // Entry point for Python AI bridge 
    public static void sendMessage(PlayerBotEntity bot, String message, ServerPlayer player) {

        // Send data to Python AI
        PythonBridge.sendAsync(bot, message, player);
    }
}