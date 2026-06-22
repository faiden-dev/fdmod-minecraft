package com.example.fdmod.command;

import com.example.fdmod.entity.BotMode;
import com.example.fdmod.entity.PlayerBotEntity;
import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.level.Level;

public class BotCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(

            // Register the "bot" command
            Commands.literal("bMode")

                // Make the bot follow the player
                .then(Commands.literal("follow")
                    .executes(ctx -> {
                        CommandSourceStack source = ctx.getSource();
                        Level level = source.getLevel();

                        var bots = level.getEntitiesOfClass(PlayerBotEntity.class,
                                source.getPlayerOrException().getBoundingBox().inflate(64));

                        for (PlayerBotEntity bot : bots) {
                            bot.setMode(BotMode.FOLLOW);
                        }

                        return 1;
                    })
                )

                // Stop the bot from following
                .then(Commands.literal("stop")
                    .executes(ctx -> {
                        CommandSourceStack source = ctx.getSource();
                        Level level = source.getLevel();

                        var bots = level.getEntitiesOfClass(PlayerBotEntity.class,
                                source.getPlayerOrException().getBoundingBox().inflate(64));

                        for (PlayerBotEntity bot : bots) {
                            bot.setMode(BotMode.STOP);
                        }

                        return 1;
                    })
                )

                // Stop the bot from following
                .then(Commands.literal("attack")
                    .executes(ctx -> {
                        CommandSourceStack source = ctx.getSource();
                        Level level = source.getLevel();

                        var bots = level.getEntitiesOfClass(PlayerBotEntity.class,
                                source.getPlayerOrException().getBoundingBox().inflate(64));

                        for (PlayerBotEntity bot : bots) {
                            bot.setMode(BotMode.ATTACK);
                        }

                        return 1;
                    })
                )

        );
    }
}