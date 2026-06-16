package com.example.fdmod;

import com.example.fdmod.entity.ModEntities;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Fdmod.MODID)
public class Fdmod {
    public static final String MODID = "fdmod";

    // Constructor for the mod class
    public Fdmod(FMLJavaModLoadingContext context) {
        IEventBus eventBus = context.getModEventBus();
        
        // Register mod components
        ModItems.register(eventBus);
        ModEntities.register(eventBus);
    }

}
