package com.example.fdmod.entity;

import java.util.UUID;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;


public class PlayerBotEntity extends PathfinderMob {
    // Constructor for the PlayerBotEntity
    public PlayerBotEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    // Define attributes for the PlayerBotEntity
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.ATTACK_DAMAGE, 4.0D);
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


    // Unique identifier for the bot's owner
    private UUID ownerUUID;

    // Set the owner of the bot using the player's UUID
    public void setOwner(ServerPlayer player) {
        this.ownerUUID = player.getUUID();
    }

    // Check if a given entity is the owner of the bot
    public boolean isOwner(LivingEntity entity) {
        return entity instanceof ServerPlayer player &&
            player.getUUID().equals(this.ownerUUID);
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
        else if (mode == BotMode.FOLLOW) {
            Player player = this.level().getNearestPlayer(this, 128);

            if (player != null) {
                this.getNavigation().moveTo(
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    1.2D
                );
            }
        }
        // Attack the nearest mob
        else if (mode == BotMode.ATTACK) {
        var nearestMob = this.level()
            .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(128))
            .stream()
            .filter(entity ->
                entity != this &&
                !(entity instanceof PlayerBotEntity) &&
                !(entity instanceof ServerPlayer player &&
                    this.ownerUUID != null &&
                    player.getUUID().equals(this.ownerUUID)
                )
            )
            .min((a, b) -> Double.compare(
                this.distanceToSqr(a),
                this.distanceToSqr(b)
            ))
            .orElse(null);

            if (nearestMob != null) {
                this.getLookControl().setLookAt(nearestMob);

                this.getNavigation().moveTo(
                    nearestMob.getX(),
                    nearestMob.getY(),
                    nearestMob.getZ(),
                    1.2D
                );

                if (this.distanceTo(nearestMob) <= 2.5F) {
                    this.doHurtTarget(nearestMob);
                    this.swing(InteractionHand.MAIN_HAND);
                }
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