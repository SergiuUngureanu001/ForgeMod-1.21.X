package net.sergiu.minecraftmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sergiu.minecraftmod.TestMod;
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
                    .sized(1.0f, 1.5f).build("zarathustra"));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
