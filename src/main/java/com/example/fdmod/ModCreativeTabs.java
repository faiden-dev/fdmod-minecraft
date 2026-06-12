package com.example.fdmod;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Fdmod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)

public class ModCreativeTabs {
    @SubscribeEvent

    // Add the custom item to the combat creative tab
    public static void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.SWORD_67);
        }
    }
}