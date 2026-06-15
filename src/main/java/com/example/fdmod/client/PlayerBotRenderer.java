package com.example.fdmod.client;

import com.example.fdmod.entity.PlayerBotEntity;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class PlayerBotRenderer extends HumanoidMobRenderer<PlayerBotEntity, PlayerModel<PlayerBotEntity>> {
    // Define the texture location for the PlayerBotEntity
    private static final ResourceLocation TEXTURE =
        ResourceLocation.fromNamespaceAndPath(
            "fdmod",
            "textures/skins/bot.png"
        );

    // Constructor for the PlayerBotRenderer
    public PlayerBotRenderer(EntityRendererProvider.Context context) {
        super(
            context,
            new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false),
            0.5F
        );
    }

    @Override
    // Return the texture location for the PlayerBotEntity
    public ResourceLocation getTextureLocation(PlayerBotEntity entity) {
        return TEXTURE;
    }
}