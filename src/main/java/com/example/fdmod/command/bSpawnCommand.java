package com.example.fdmod.command;

import com.example.fdmod.entity.ModEntities;
import com.example.fdmod.entity.PlayerBotEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class bSpawnCommand {

    // List of available bot skins
    private static final String[] SKINS = {
        "oguzok",
        "pony1",
        "pony2",
        "pony3"
    };

    // Registers the bSpawn command
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(

            // Base command: /bSpawn
            Commands.literal("bSpawn")
            .then(

                // Argument: skin selection for the bot
                Commands.argument("skin", StringArgumentType.word())

                    // Tab completion for available skins
                    .suggests((context, builder) -> {
                        for (String skin : SKINS) {
                            builder.suggest(skin);
                        }
                        return builder.buildFuture();
                    })

                    // Command execution logic
                    .executes(context -> {

                        // Get player who executed the command
                        ServerPlayer player = context.getSource().getPlayerOrException();

                        // Read selected skin argument
                        String skin = StringArgumentType.getString(context, "skin");

                        // Create bot entity
                        PlayerBotEntity bot = ModEntities.PLAYER_BOT.get().create(player.level());

                        // Apply selected skin to bot
                        bot.setSkin(skin);

                        // Apply selected class to bot
                        bot.setClass(skin); 

                        // Spawn bot at player position
                        bot.moveTo(
                            player.getX(),
                            player.getY(),
                            player.getZ(),
                            player.getYRot(),
                            player.getXRot()
                        );

                        // Assign owner to bot
                        bot.setOwner(player);

                        // Add bot to world
                        player.level().addFreshEntity(bot);

                        return 1;
                    })
            )
        );
    }
}