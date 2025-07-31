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
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.brewing.BrewingRecipeRegisterEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.entity.ModEntities;
import net.sergiu.minecraftmod.entity.custom.ZarathustraEntity;
import net.sergiu.minecraftmod.item.ModItems;
import net.sergiu.minecraftmod.item.custom.HammerItem;
import net.sergiu.minecraftmod.potion.ModPotions;
import net.sergiu.minecraftmod.villager.ModVillagers;

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
        }else if (event.getEntity() instanceof ZarathustraEntity zarathustra
                && event.getSource().getDirectEntity() instanceof Player player) {
            if (!player.level().isClientSide() && !zarathustra.isBaby()) {
                // 1) give him fire resistance
                zarathustra.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 5));

                // 2) compute the two-blocks-behind vector
                Vec3 look   = player.getLookAngle().normalize();
                double dist = 4.0;
                double rawX = player.getX() - look.x * dist;
                double rawY = player.getEyeY() - 0.5;       // chest-height
                double rawZ = player.getZ() - look.z * dist;

                // 3) build his AABB at that spot
                AABB targetBB = zarathustra.getBoundingBox()
                        .move(rawX - zarathustra.getX(),
                                rawY - zarathustra.getY(),
                                rawZ - zarathustra.getZ());

                // 4) decide yaw/pitch so he faces the player
                float yaw   = player.getYRot() + 180f;
                float pitch = player.getXRot();

                Level world = player.level();
                // 5) teleport if free, otherwise scan up 1–5 blocks
                if (world.noCollision(zarathustra, targetBB)) {
                    zarathustra.moveTo(rawX, rawY, rawZ, yaw, pitch);
                } else {
                    for (int dy = 1; dy <= 5; dy++) {
                        AABB shifted = targetBB.move(0, dy, 0);
                        if (world.noCollision(zarathustra, shifted)) {
                            zarathustra.moveTo(rawX, rawY + dy, rawZ, yaw, pitch);
                            break;
                        }
                    }
                }
            }
        }



    }

    @SubscribeEvent
    public static void onBrewingRecipeRegister(BrewingRecipeRegisterEvent event) {
        PotionBrewing.Builder builder = event.getBuilder();

        builder.addMix(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION.getHolder().get());
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.FARMER) {
            var trades = event.getTrades();

            trades.get(1).add(((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 5),
                    new ItemStack(ModItems.KOHLRABI.get(), 14), 6, 4, 0.05f)));

            trades.get(1).add(((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.DIAMOND, 3),
                    new ItemStack(ModItems.HONEY_BERRIES.get(), 32), 6, 4, 0.05f)));

            trades.get(2).add(((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.BELL, 1),
                    new ItemStack(ModItems.AURORA_ASHES.get(), 32), 1, 12, 0.05f)));

        }

        if(event.getType() == ModVillagers.KAUPENGER.get()) {
            var trades = event.getTrades();

            trades.get(1).add(((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.EMERALD, 12),
                    new ItemStack(ModItems.CHISEL.get(), 1), 6, 4, 0.05f)));

            trades.get(1).add(((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.DIAMOND, 5),
                    new ItemStack(ModItems.TOMAHAWK.get(), 1), 6, 4, 0.05f)));

            trades.get(2).add(((pTrader, pRandom) -> new MerchantOffer(
                    new ItemCost(Items.BELL, 1),
                    new ItemStack(ModItems.AURORA_ASHES.get(), 32), 1, 12, 0.05f)));
        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemCost(Items.EMERALD, 12),
                new ItemStack(ModItems.RADIATION_STAFF.get(), 1), 1, 10, 0.2f
        ));

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemCost(Items.NETHERITE_INGOT, 8),
                new ItemStack(ModItems.BAR_BRAWL_MUSIC_DISC.get(), 1), 1, 10, 0.2f
        ));
    }

}
