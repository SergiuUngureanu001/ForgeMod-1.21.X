package net.sergiu.minecraftmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.sergiu.minecraftmod.TestMod;

public class ZarathustraModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(TestMod.MOD_ID, "zarathustra"), "main");

    private final ModelPart head;
    private final ModelPart headwear;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public ZarathustraModel(ModelPart root) {
        this.head = root.getChild("head");
        this.headwear = root.getChild("headwear");
        this.body = root.getChild("body");
        this.leftArm = root.getChild("left_arm");
        this.rightArm = root.getChild("right_arm");
        this.leftLeg = root.getChild("left_leg");
        this.rightLeg = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild("headwear",
                CubeListBuilder.create()
                        .texOffs(32, 0)
                        .addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, new CubeDeformation(0.5F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild("body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16).mirror()
                        .addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(5.0F, 2.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-5.0F, 2.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild("left_leg",
                CubeListBuilder.create().texOffs(0, 16).mirror()
                        .addBox(-1.9F, 0.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.0F)).mirror(false),
                PartPose.offset(1.9F, 12.0F, 0.0F)
        );

        partdefinition.addOrReplaceChild("right_leg",
                CubeListBuilder.create().texOffs(0, 16)
                        .addBox(-2.1F, 0.0F, -2.0F, 4, 12, 4, new CubeDeformation(0.0F)),
                PartPose.offset(-1.9F, 12.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        // Head rotation
        this.head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        this.head.xRot = headPitch * ((float)Math.PI / 180F);
        this.headwear.yRot = this.head.yRot;
        this.headwear.xRot = this.head.xRot;

        // Arms swing
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
        this.leftArm.xRot  = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;

        // Legs
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * limbSwingAmount;
        this.leftLeg.xRot  = Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack ms, VertexConsumer vb,
                               int packedLight, int packedOverlay, int packedColor) {
        head.render(ms, vb, packedLight, packedOverlay, packedColor);
        headwear.render(ms, vb, packedLight, packedOverlay, packedColor);
        body.render(ms, vb, packedLight, packedOverlay, packedColor);
        leftArm.render(ms, vb, packedLight, packedOverlay, packedColor);
        rightArm.render(ms, vb, packedLight, packedOverlay, packedColor);
        leftLeg.render(ms, vb, packedLight, packedOverlay, packedColor);
        rightLeg.render(ms, vb, packedLight, packedOverlay, packedColor);
    }


}
