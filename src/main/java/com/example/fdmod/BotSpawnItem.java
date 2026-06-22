package com.example.fdmod;

import com.example.fdmod.entity.ModEntities;
import com.example.fdmod.entity.PlayerBotEntity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BotSpawnItem extends Item {
    // Constructor for the BotSpawnItem class
    public BotSpawnItem(Properties properties) {
        super(properties);
    }

    @Override
    // Override the use method to spawn a PlayerBotEntity when the item is used
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        // Check if the code is running on the server side
        if (!level.isClientSide()) {
            PlayerBotEntity bot = ModEntities.PLAYER_BOT.get().create(level);

            if (bot != null) {
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
                level.addFreshEntity(bot);
            }
        }

        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}