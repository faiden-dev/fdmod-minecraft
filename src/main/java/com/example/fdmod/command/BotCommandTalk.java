package com.example.fdmod.command;
import com.example.fdmod.entity.onTalk;

import com.example.fdmod.entity.PlayerBotEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class BotCommandTalk {

    // Available bot classes (groups/types)
    private static final String[] CLASSES = {
        "oguzok",
        "pony1",
        "pony2",
        "pony3"
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(

            // This command allows sending a message to a specific bot class
            Commands.literal("bTalk")

                // select bot group
                .then(Commands.argument("class", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        // Chat suggestions
                        for (String c : CLASSES) {
                            builder.suggest(c);
                        }

                        return builder.buildFuture();
                    })

                    // player message
                    .then(Commands.argument("message", StringArgumentType.greedyString())
                        .executes(ctx -> {

                            // Command source
                            CommandSourceStack source = ctx.getSource();

                            // Get the player who executed the command
                            ServerPlayer player = source.getPlayerOrException();

                            // Read bot class
                            String className = StringArgumentType.getString(ctx, "class").toLowerCase();

                            // Full message text from player
                            String message = StringArgumentType.getString(ctx, "message");

                            // Find all bots near the player
                            var bots = player.level().getEntitiesOfClass(
                                PlayerBotEntity.class,
                                player.getBoundingBox().inflate(64)
                            );

                            // Iterate through all found bots
                            for (PlayerBotEntity bot : bots) {

                                // Check if bot belongs to selected class
                                if (className.equals(bot.getClassName())) {

                                    // Send message to bot
                                    onTalk.sendMessage(bot, message, player);
                                }
                            }

                            // Command executed successfully
                            return 1;
                        })
                    )
                )

        );
    }
    
}