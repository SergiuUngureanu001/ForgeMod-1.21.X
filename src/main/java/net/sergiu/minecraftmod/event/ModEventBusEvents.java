package net.sergiu.minecraftmod.event;

import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.entity.ModEntities;
import net.sergiu.minecraftmod.entity.client.TriceratopsModel;
import net.sergiu.minecraftmod.entity.client.ZarathustraModel;
import net.sergiu.minecraftmod.entity.custom.TriceratopsEntity;
import net.sergiu.minecraftmod.entity.custom.ZarathustraEntity;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TriceratopsModel.LAYER_LOCATION, TriceratopsModel::createBodyLayer);
        event.registerLayerDefinition(ZarathustraModel.LAYER_LOCATION, ZarathustraModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.TRICERATOPS.get(), TriceratopsEntity.createAttributes().build());
        event.put(ModEntities.ZARATHUSTRA.get(), ZarathustraEntity.createAttributes().build());
    }
}
