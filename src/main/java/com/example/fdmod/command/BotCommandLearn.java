package com.example.fdmod.command;

import com.example.fdmod.entity.PlayerBotEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class BotCommandLearn {

    private static final String[] CLASSES = {
        "oguzok",
        "pony1",
        "pony2",
        "pony3"
    };

    private static final String[] MODES = {
        "walk",
        "stop"
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
            Commands.literal("bLearn")

                .then(Commands.argument("class", StringArgumentType.word())
                    // suggests available classes for tab completion
                    .suggests((ctx, builder) -> {
                        for (String c : CLASSES) {
                            builder.suggest(c);
                        }
                        return builder.buildFuture();
                    })

                    .then(Commands.argument("mode", StringArgumentType.word())
                        // suggests available modes for tab completion
                        .suggests((ctx, builder) -> {
                            for (String m : MODES) {
                                builder.suggest(m);
                            }
                            return builder.buildFuture();
                        })

                        .executes(ctx -> {

                            CommandSourceStack source = ctx.getSource();
                            ServerPlayer player = source.getPlayerOrException();

                            String className = StringArgumentType.getString(ctx, "class").toLowerCase();
                            String mode = StringArgumentType.getString(ctx, "mode").toLowerCase();

                            // Get all PlayerBotEntity instances within 64 blocks of the player
                            var bots = player.level().getEntitiesOfClass(
                                PlayerBotEntity.class,
                                player.getBoundingBox().inflate(64)
                            );

                            // Iterate through the bots and apply the learning mode to those matching the specified class
                            for (PlayerBotEntity bot : bots) {
                                if (className.equals(bot.getClassName().toLowerCase())) {

                                    if (mode.equals("walk")) {
                                        bot.startLearnWalk();
                                    } else if (mode.equals("stop")) {
                                        bot.stopLearnWalk();
                                    }
                                }
                            }

                            return 1;
                        })
                    )
                )
        );
    }
}