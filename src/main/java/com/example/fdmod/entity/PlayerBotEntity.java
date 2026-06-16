package com.example.fdmod.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

public class PlayerBotEntity extends PathfinderMob {
    // Constructor for the PlayerBotEntity
    public PlayerBotEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    // Define attributes for the PlayerBotEntity
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }


    // Bot mode to determine behavior
    private BotMode mode = BotMode.STOP;
    
    // Getter and setter for bot mode
    public BotMode getMode() {
        return mode;
    }
    public void setMode(BotMode mode) {
        this.mode = mode;
    }


    // Preliminary mode to track previous state
    private BotMode modePreliminary = BotMode.STOP;

    @Override
    // Tick method to update bot behavior based on mode
    public void tick() {
        super.tick();

        // Stop movement
        if (mode == BotMode.STOP) {
            this.getNavigation().stop();
            this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0D);
        }
        // Find the nearest player and move towards them
        if (mode == BotMode.FOLLOW) {
            Player player = this.level().getNearestPlayer(this, 80);

            if (player != null) {
                this.getNavigation().moveTo(
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    1.2D
                );
            }
        }

        // Broadcast a message when switching from FOLLOW to STOP mode
        if (modePreliminary == BotMode.FOLLOW && mode == BotMode.STOP) {
            // Randomly select a message to broadcast
            String[] messages = {
                "Yes, boss!",
                "Yes, chief!",
                "Yes, sir!"
            };
            String msg = messages[this.random.nextInt(messages.length)];

            // Broadcast the message to all players on the server
            if (!this.level().isClientSide() && this.level().getServer() != null) {
                this.level().getServer().getPlayerList().broadcastSystemMessage(
                    Component.literal(msg),
                    false
                );
            }
        }

        // Update preliminary mode to current mode at the end of the tick
        modePreliminary = mode;
    }
}