package net.zuperz.aurora.Entity.client;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Animal;
import net.zuperz.aurora.Entity.custom.ChainEntity;

public class ChainModel<R extends Animal> extends HierarchicalModel<ChainEntity> {
    private final ModelPart chain;
    private final ModelPart chain1;
    private final ModelPart chain2;

    public ChainModel(ModelPart root) {
        chain = root.getChild("bone");
        chain1 = root.getChild("bone2");
        chain2 = root.getChild("bone3");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(0, 43).addBox(-7.5F, -1.5F, -7.5F, 15.0F, 3.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 12.5F, 0.5F));

        PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).addBox(-9.5F, -1.5F, -9.5F, 19.0F, 3.0F, 19.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.5F, 0.5F));

        PartDefinition bone3 = partdefinition.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 22).addBox(-9.0F, -1.5F, -9.0F, 18.0F, 3.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.5F, 0.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(ChainEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.animate(entity.idleAnimationState, ChainAnimations.CHAIN_IDLE, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        chain.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        chain1.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
        chain2.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return chain;
    }
}
