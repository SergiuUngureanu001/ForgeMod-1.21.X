package net.sergiu.minecraftmod.event;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.item.custom.HammerItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>();

    // Done with the help of https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java
    // Don't be a jerk License
    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getMainHandItem();

        if(mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) {
            BlockPos initialBlockPos = event.getPos();
            if(HARVESTED_BLOCKS.contains(initialBlockPos)) {
                return;
            }

            for(BlockPos pos : HammerItem.getBlocksToBeDestroyed(2, initialBlockPos, serverPlayer)) {
                if(pos == initialBlockPos || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        final List<Item> HARM_ITEMS = List.of(Items.DIAMOND_SWORD,  Items.GOLDEN_SWORD, Items.IRON_SWORD);
        if(event.getEntity() instanceof Pig pig && event.getSource().getDirectEntity() instanceof Player player) {
            pig.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 5));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 5));
            player.sendSystemMessage(Component.literal(player.getName().getString() + " JUST HIT A PIG! PIGS ARE HARAM!"));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_GOLD, SoundSource.AMBIENT, 1.0f, 1.0f);
        } else if(event.getEntity() instanceof Animal animal && event.getSource().getDirectEntity() instanceof Player player) {
            if(HARM_ITEMS.contains(player.getMainHandItem().getItem())) {
                // Strike lightning at the player's position
                var serverLevel = (net.minecraft.server.level.ServerLevel) player.level();
                var lightning = new net.minecraft.world.entity.LightningBolt(
                        net.minecraft.world.entity.EntityType.LIGHTNING_BOLT,
                        serverLevel
                );

                animal.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 600, 5));
                lightning.moveTo(player.getX(), player.getY(), player.getZ());
                lightning.setVisualOnly(false);
                serverLevel.addFreshEntity(lightning);

                player.sendSystemMessage(Component.literal("You've angered the gods!"));
            }
        }
    }

}
