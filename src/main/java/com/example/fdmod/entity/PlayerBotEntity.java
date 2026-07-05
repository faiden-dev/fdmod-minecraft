package com.example.fdmod.entity;

import java.util.UUID;
import com.example.fdmod.learn.walk.startLearnWalk;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.OpenDoorGoal;

public class PlayerBotEntity extends PathfinderMob {

    public startLearnWalk learnWalkInstance;
    public boolean isLearningWalk = false;

    // Constructor for the PlayerBotEntity
    public PlayerBotEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        
        // Add goals for the bot's behavior
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, false));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.getNavigation().setCanFloat(true);
        
        // Set pathfinding malus for different block types to influence navigation behavior
        this.setPathfindingMalus(PathType.LAVA, -1.0F);
        this.setPathfindingMalus(PathType.DANGER_FIRE, 20.0F);
        this.setPathfindingMalus(PathType.DAMAGE_CAUTIOUS, 1.0F);
    }

    // Override the tick method to handle learning walk mode
    public void runLearnWalk() {
        this.getNavigation().stop();
        this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0D);
    }

    // Define attributes for the PlayerBotEntity
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 20.0D)
            .add(Attributes.MOVEMENT_SPEED, 0.25D)
            .add(Attributes.ATTACK_DAMAGE, 4.0D)
            .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    // Define synchronized data for the PlayerBotEntity, including the skin property
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN, "oguzok"); 
        builder.define(BOT_CLASS, "oguzok"); 
    }

    // Define a synchronized data accessor for the skin property of the PlayerBotEntity
    private static final EntityDataAccessor<String> SKIN =
        SynchedEntityData.defineId(PlayerBotEntity.class, EntityDataSerializers.STRING);

    // Getter and setter for skin property
    public void setSkin(String skin) {
        this.entityData.set(SKIN, skin);
    }

    // Getter for skin property
    public String getSkin() {
        return this.entityData.get(SKIN);
    }

    // Define a synchronized data accessor for the class property of the PlayerBotEntity
    private static final EntityDataAccessor<String> BOT_CLASS =
        SynchedEntityData.defineId(PlayerBotEntity.class, EntityDataSerializers.STRING);

    // Getter and setter for class property
    public void setClass(String className) {
        this.entityData.set(BOT_CLASS, className);
    }

    // Getter for class property
    public String getClassName() {
        return this.entityData.get(BOT_CLASS);
    }

    @Override
    // Save additional data to the NBT tag for persistence
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putString("Skin", getSkin());
        tag.putString("BotClass", getClassName());
    }

    @Override
    // Read additional save data from the NBT tag to restore the bot's state
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Skin")) {
            setSkin(tag.getString("Skin"));
        }

        if (tag.contains("BotClass")) {
            setClass(tag.getString("BotClass"));
        }
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
    public void setOwner(Player player) {
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

        if (this.isLearningWalk) {
            return;
        }

        // Stop movement
        if (mode == BotMode.STOP) {
            this.getNavigation().stop();
            this.getMoveControl().setWantedPosition(this.getX(), this.getY(), this.getZ(), 0.0D);
            this.navigationDelay = 0;
        }
        // Find the nearest player and move towards them
        else if (mode == BotMode.FOLLOW) {
            Player player = this.level().getNearestPlayer(this, 128);

            if (player != null) {
                moveToTarget(
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
            .getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(64))
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

                moveToTarget(
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
                "Ok",
                "Done",
                "Stopped"
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

    
    // Delay counter for navigation updates
    private int navigationDelay = 0;

    // Function to move the bot towards a target position with a specified speed
    private void moveToTarget(double x, double y, double z, double speed) {
        if (--this.navigationDelay <= 0) {
            this.navigationDelay = 10; 

            net.minecraft.world.level.pathfinder.Path path = this.getNavigation().createPath(x, y, z, 0);
            if (path != null) {
                this.getNavigation().moveTo(path, speed);
            }
        }
    }


    
    // Start learning walk mode
    public void startLearnWalk() {
        // Kill old scheduler if exists
        if (this.learnWalkInstance != null && this.learnWalkInstance.runner != null) {
            this.learnWalkInstance.runner.forceKill();
        }
        
        this.isLearningWalk = true;
        this.learnWalkInstance = new startLearnWalk(this);
    }

    // Stop learning walk mode
    public void stopLearnWalk() {
        // Kill active scheduler
        if (this.learnWalkInstance != null && this.learnWalkInstance.runner != null) {
            this.learnWalkInstance.runner.forceKill();
        }
        
        this.isLearningWalk = false;
        this.learnWalkInstance = null;
        this.setMode(BotMode.STOP);
    }
}