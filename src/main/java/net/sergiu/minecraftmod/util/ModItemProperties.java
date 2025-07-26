package net.sergiu.minecraftmod.util;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.component.ModDataComponentTypes;
import net.sergiu.minecraftmod.item.ModItems;

public class ModItemProperties {
    public static void addCustomItemProperties() {
        ItemProperties.register(ModItems.CHISEL.get(), ResourceLocation.fromNamespaceAndPath(TestMod.MOD_ID, "used"),
                (itemStack, clientLevel, livingEntity, i) -> itemStack.get(ModDataComponentTypes.COORDINATES.get()) != null ? 1f : 0f);
    }
}
