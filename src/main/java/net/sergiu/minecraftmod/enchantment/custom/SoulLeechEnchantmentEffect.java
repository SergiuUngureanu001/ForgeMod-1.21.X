package net.sergiu.minecraftmod.enchantment.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;

public record SoulLeechEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<SoulLeechEnchantmentEffect> CODEC = MapCodec.unit(SoulLeechEnchantmentEffect::new);

    @Override
    public void apply(ServerLevel pLevel, int pEnchantmentLevel, EnchantedItemInUse pItem, Entity pEntity, Vec3 pOrigin) {
        if(pItem.owner() instanceof LivingEntity attacker && pEntity instanceof LivingEntity target) {

            float percent = switch (pEnchantmentLevel) {
                case 1 -> 0.05f;
                case 2 -> 0.08f;
                case 3 -> 0.12f;
                default -> 0f;
            };

            float healAmount = target.getMaxHealth() * percent;

            attacker.heal(healAmount);
            pLevel.playSound(attacker, attacker.getOnPos(), SoundEvents.EVOKER_CAST_SPELL,  SoundSource.PLAYERS, 1.0f, 1.0f);
        }

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }
}
