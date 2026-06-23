package com.example.fdmod.command;

import com.example.fdmod.entity.BotMode;
import com.example.fdmod.entity.PlayerBotEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class BotCommand {

    // Available bot classes (groups/types)
    private static final String[] CLASSES = {
        "oguzok",
        "pony1",
        "pony2",
        "pony3"
    };

    // Available bot modes
    private static final String[] MODES = {
        "follow",
        "stop",
        "attack"
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(

            // Base command: /bMode <class> <mode>
            Commands.literal("bMode")

                // First argument: class
                .then(Commands.argument("class", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        for (String c : CLASSES) {
                            builder.suggest(c);
                        }
                        return builder.buildFuture();
                    })

                    // Second argument: mode
                    .then(Commands.argument("mode", StringArgumentType.word())
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
                            String modeStr = StringArgumentType.getString(ctx, "mode").toUpperCase();

                            BotMode mode = BotMode.valueOf(modeStr);

                            var bots = player.level().getEntitiesOfClass(
                                PlayerBotEntity.class,
                                player.getBoundingBox().inflate(64)
                            );

                            for (PlayerBotEntity bot : bots) {
                                if (className.equals(bot.getClassName())) {
                                    bot.setMode(mode);
                                }
                            }

                            return 1;
                        })
                    )
                )
        );
    }
}