package com.example.fdmod.command;

import com.example.fdmod.entity.ModEntities;
import com.example.fdmod.entity.PlayerBotEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class bSpawnCommand {

    // List of available bot classes
    private static final String[] CLASSES = {
        "oguzok",
        "pony1",
        "pony2",
        "pony3"
    };

    // Registers the bSpawn command
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(

            // Base command: /bSpawn <class>
            Commands.literal("bSpawn")
                .then(

                    // Argument: class selection for the bot
                    Commands.argument("class", StringArgumentType.word())

                        // Tab completion for available classes
                        .suggests((context, builder) -> {
                            for (String c : CLASSES) {
                                builder.suggest(c);
                            }
                            return builder.buildFuture();
                        })

                        // Command execution logic
                        .executes(context -> {

                            // Get player who executed the command
                            ServerPlayer player = context.getSource().getPlayerOrException();

                            // Read selected class argument
                            String className = StringArgumentType.getString(context, "class");

                            // Create bot entity
                            PlayerBotEntity bot = ModEntities.PLAYER_BOT.get().create(player.level());

                            // Apply class (skin + behavior group)
                            bot.setSkin(className);
                            bot.setClass(className);

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