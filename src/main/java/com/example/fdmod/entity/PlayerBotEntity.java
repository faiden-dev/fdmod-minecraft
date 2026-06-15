package com.example.fdmod.entity;

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
}