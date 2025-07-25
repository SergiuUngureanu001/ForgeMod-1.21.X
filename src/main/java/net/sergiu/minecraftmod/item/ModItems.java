package net.sergiu.minecraftmod.item;

import net.minecraft.world.item.*;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.item.custom.ChiselItem;
import net.sergiu.minecraftmod.item.custom.FuelItem;
import net.sergiu.minecraftmod.item.custom.GoldifierItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MOD_ID);

    public static final RegistryObject<Item> ALEXANDRITE = ITEMS.register("alexandrite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> RAW_ALEXANDRITE = ITEMS.register("raw_alexandrite", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> ZARATHUSTRA = ITEMS.register("zarathustra", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GAUNUS = ITEMS.register("gaunus", () -> new GoldifierItem(new Item.Properties().food(ModFoodProperties.GAUNUS).durability(32)));
    public static final RegistryObject<Item> CHISEL = ITEMS.register("chisel", () -> new ChiselItem(new Item.Properties().durability(32)));
    public static final RegistryObject<Item> KOHLRABI = ITEMS.register("kohlrabi", () -> new Item(new Item.Properties().food(ModFoodProperties.KOHLRABI)));
    public static final RegistryObject<Item> AURORA_ASHES = ITEMS.register("aurora_ashes", () -> new FuelItem(new Item.Properties(), 1200));

    ///  ALEXANDRITE TOOLS
    public static final RegistryObject<Item> ALEXANDRITE_SWORD = ITEMS.register("alexandrite_sword", () -> new SwordItem(ModToolTiers.ALEXANDRITE, new Item.Properties()
            .attributes(SwordItem.createAttributes(ModToolTiers.ALEXANDRITE, 3, -2.4f))));
    public static final RegistryObject<Item> ALEXANDRITE_PICKAXE = ITEMS.register("alexandrite_pickaxe", () -> new PickaxeItem(ModToolTiers.ALEXANDRITE, new Item.Properties()
            .attributes(PickaxeItem.createAttributes(ModToolTiers.ALEXANDRITE, 1, -2.8f))));
    public static final RegistryObject<Item> ALEXANDRITE_SHOVEL = ITEMS.register("alexandrite_shovel", () -> new ShovelItem(ModToolTiers.ALEXANDRITE, new Item.Properties()
            .attributes(ShovelItem.createAttributes(ModToolTiers.ALEXANDRITE, 1.5f, -3.0f))));
    public static final RegistryObject<Item> ALEXANDRITE_AXE = ITEMS.register("alexandrite_axe", () -> new AxeItem(ModToolTiers.ALEXANDRITE, new Item.Properties()
            .attributes(AxeItem.createAttributes(ModToolTiers.ALEXANDRITE, 6, -3.2f))));
    public static final RegistryObject<Item> ALEXANDRITE_HOE = ITEMS.register("alexandrite_hoe", () -> new HoeItem(ModToolTiers.ALEXANDRITE, new Item.Properties()
            .attributes(HoeItem.createAttributes(ModToolTiers.ALEXANDRITE, 0, -3.0f))));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
