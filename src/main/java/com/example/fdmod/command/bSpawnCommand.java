package com.example.fdmod.command;

import com.example.fdmod.entity.ModEntities;
import com.example.fdmod.entity.PlayerBotEntity;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class bSpawnCommand {
    // Registercommand to spawn a PlayerBotEntity
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("bSpawn")

                .executes(context -> {
                    // Get the player who executed the command
                    ServerPlayer player = context.getSource().getPlayerOrException();

                    // Create a new instance of the PlayerBotEntity
                    PlayerBotEntity bot = ModEntities.PLAYER_BOT.get().create(player.level());

                    // Set the bot's position and rotation to match the player's
                    bot.moveTo(
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        player.getYRot(),
                        player.getXRot()
                    );

                    // Set the bot's owner to the player
                    bot.setOwner(player);
    
                    // Add the bot entity to the world
                    player.level().addFreshEntity(bot);

                    return 1;
                })

        );
    }
}