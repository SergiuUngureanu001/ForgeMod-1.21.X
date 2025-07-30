package net.sergiu.minecraftmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.entity.custom.ChairEntity;
import net.sergiu.minecraftmod.entity.custom.TomahawkProjectileEntity;
import net.sergiu.minecraftmod.entity.custom.TriceratopsEntity;
import net.sergiu.minecraftmod.entity.custom.ZarathustraEntity;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TestMod.MOD_ID);

    public static final RegistryObject<EntityType<TriceratopsEntity>> TRICERATOPS =
            ENTITY_TYPES.register("triceratops", () -> EntityType.Builder.of(TriceratopsEntity::new, MobCategory.CREATURE)
                    .sized(1.5f, 1.5f).build("triceratops"));

    public static final RegistryObject<EntityType<ZarathustraEntity>> ZARATHUSTRA =
            ENTITY_TYPES.register("zarathustra", () -> EntityType.Builder.of(ZarathustraEntity::new, MobCategory.CREATURE)
                    .sized(1.0f, 2.0f).build("zarathustra"));

    public static final RegistryObject<EntityType<TomahawkProjectileEntity>> TOMAHAWK =
            ENTITY_TYPES.register("tomahawk", () -> EntityType.Builder.<TomahawkProjectileEntity>of(TomahawkProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 1.15f).build("tomahawk"));

    public static final RegistryObject<EntityType<ChairEntity>> CHAIR =
            ENTITY_TYPES.register("chair_entity", () -> EntityType.Builder.of(ChairEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("chair_entity"));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
