package net.sergiu.minecraftmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.sergiu.minecraftmod.TestMod;
import net.sergiu.minecraftmod.entity.client.TriceratopsModel;
import net.sergiu.minecraftmod.entity.client.ZarathustraModel;
import net.sergiu.minecraftmod.entity.custom.TriceratopsEntity;
import net.sergiu.minecraftmod.entity.custom.ZarathustraEntity;

public class ZarathustraRenderer extends MobRenderer<ZarathustraEntity, ZarathustraModel<ZarathustraEntity>> {
    public ZarathustraRenderer(EntityRendererProvider.Context pContext) {
        super(pContext,
                new ZarathustraModel<>(pContext.bakeLayer(ZarathustraModel.LAYER_LOCATION)),
                0.85f);
    }


    @Override
    public ResourceLocation getTextureLocation(ZarathustraEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(TestMod.MOD_ID, "textures/entity/zarathustra/zarathustra0.png");
    }

    @Override
    public void render(ZarathustraEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            pPoseStack.scale(1f, 1f, 1f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}