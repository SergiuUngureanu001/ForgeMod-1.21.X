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
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.brewing.BrewingRecipeRegisterEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.item.custom.HammerItem;
import net.sergiu.minecraftmod.potion.ModPotions;

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
        final List<Item> HARM_ITEMS = List.of(Items.DIAMOND_SWORD,  Items.GOLDEN_SWORD, Items.IRON_SWORD, Items.WOODEN_SWORD, Items.STONE_SWORD);
        if(event.getEntity() instanceof Pig pig && event.getSource().getDirectEntity() instanceof Player player) {
            pig.addEffect(new MobEffectInstance(MobEffects.POISON, 600, 5));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 5));
            player.sendSystemMessage(Component.literal(player.getName().getString() + " JUST HIT A PIG! PIGS ARE HARAM!"));
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARMOR_EQUIP_GOLD, SoundSource.AMBIENT, 1.0f, 1.0f);
        } else if(event.getEntity() instanceof Animal animal && event.getSource().getDirectEntity() instanceof Player player) {
            if(HARM_ITEMS.contains(player.getMainHandItem().getItem())) {
                player.getMainHandItem().shrink(1);
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));

                Level serverLevel = player.level();

                serverLevel.playSound(null, player.getOnPos(), SoundEvents.ANVIL_DESTROY, SoundSource.AMBIENT, 1.0f, 1.0f);
                player.sendSystemMessage(Component.literal(player.getName().getString() + " WAS TRYING TO KILL AN ANIMAL IN A WRONG WAY!"));


            }
        } else if (event.getEntity() instanceof Villager villager && event.getSource().getDirectEntity() instanceof Player player) {
            if (!player.level().isClientSide()) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 1));

                var serverLevel = (net.minecraft.server.level.ServerLevel) player.level();

                // Create Iron Golem with correct constructor
                var golem = new net.minecraft.world.entity.animal.IronGolem(
                        net.minecraft.world.entity.EntityType.IRON_GOLEM,
                        serverLevel
                );

                // Position the golem near the player
                golem.moveTo(player.getX() + 2, player.getY(), player.getZ() + 2);

                // Make sure villagers don't attack it
                golem.setPlayerCreated(true);

                // Set player as the target
                golem.setTarget(player);

                // Add golem to the world
                serverLevel.addFreshEntity(golem);

                player.sendSystemMessage(Component.literal("You struck a villager. Justice is coming."));
            }
        }

    }

    @SubscribeEvent
    public static void onBrewingRecipeRegister(BrewingRecipeRegisterEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION.getHolder().get());
    }

}
