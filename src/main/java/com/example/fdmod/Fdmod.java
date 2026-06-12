package com.example.fdmod;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Fdmod.MODID)
public class Fdmod {
    public static final String MODID = "fdmod";

    // Constructor for the mod class
    public Fdmod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.register(eventBus);
    }
}
