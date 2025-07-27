package net.sergiu.minecraftmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.enchantment.ModEnchantmentEffects;
import net.sergiu.minecraftmod.enchantment.ModEnchantments;
import net.sergiu.minecraftmod.trim.ModTrimMaterials;
import net.sergiu.minecraftmod.trim.ModTrimPatterns;
import net.sergiu.minecraftmod.worldgen.ModBiomeModifiers;
import net.sergiu.minecraftmod.worldgen.ModConfiguredFeatures;
import net.sergiu.minecraftmod.worldgen.ModPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackEntries extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.TRIM_MATERIAL, ModTrimMaterials::bootstrap)
            .add(Registries.TRIM_PATTERN, ModTrimPatterns::bootstrap)
            .add(Registries.ENCHANTMENT, ModEnchantments::bootstrap)

            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER ,Set.of(TestMod.MOD_ID));
    }
}
