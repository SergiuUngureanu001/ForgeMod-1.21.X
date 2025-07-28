package net.sergiu.minecraftmod.worldgen.tree;

import net.minecraft.world.level.block.grower.TreeGrower;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.worldgen.ModConfiguredFeatures;

import java.util.Optional;

public class ModTreeGrowers {
    public static final TreeGrower WALNUT = new TreeGrower(TestMod.MOD_ID + ":walnut",
            Optional.empty(), Optional.of(ModConfiguredFeatures.WALNUT_KEY), Optional.empty());

    public static final TreeGrower PICKLE = new TreeGrower(TestMod.MOD_ID + ":pickle",
            Optional.empty(), Optional.of(ModConfiguredFeatures.PICKLE_KEY), Optional.empty());
}
