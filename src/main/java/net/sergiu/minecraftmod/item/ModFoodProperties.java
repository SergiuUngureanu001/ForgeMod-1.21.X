package net.sergiu.minecraftmod.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.sergiu.minecraftmod.effect.ModEffects;

public class ModFoodProperties {
    public static final FoodProperties KOHLRABI = new FoodProperties.Builder().nutrition(3).saturationModifier(0.25f)
            .effect(new MobEffectInstance(MobEffects.INVISIBILITY, 400), 0.20f).build();

    public static final FoodProperties GAUNUS = new FoodProperties.Builder().nutrition(10).saturationModifier(0.25f)
            .effect(new MobEffectInstance(MobEffects.INVISIBILITY, 2500), 1.0f)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2500, 4), 1.0f)
            .effect(new MobEffectInstance(MobEffects.REGENERATION, 2500, 4), 1.0f)
            .effect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2500, 4), 1.0f)
            .effect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 2500, 4), 1.0f)
            .effect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 2500, 5), 1.0f)
            .effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2500, 5), 1.0f)
            .effect(new MobEffectInstance(MobEffects.JUMP, 2500), 1.0f)
            .effect(new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE, 2500), 1.0f)
            .effect(new MobEffectInstance(MobEffects.LUCK, 2500), 1.0f)
            .effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 2500, 6), 1.0f)
            .effect(new MobEffectInstance(MobEffects.SLOW_FALLING, 2500), 1.0f)
            .effect(new MobEffectInstance(MobEffects.WATER_BREATHING, 2500), 1.0f)
            .effect(new MobEffectInstance(ModEffects.SLIMEY_EFFECT.getHolder().get(), 2500), 1.0f)
            .build();
}