package net.zuperz.aurora.Entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Monster;
import net.zuperz.aurora.Entity.custom.ChainEntity;
import net.zuperz.aurora.Entity.custom.VoidWalkerEntity;

public class VoidWalkerModel<T extends Monster> extends HierarchicalModel<T> {
    private final ModelPart VoidWalker;
    private final ModelPart head;

    public VoidWalkerModel(ModelPart root) {
        this.VoidWalker = root.getChild("VoidWalker");
        this.head = VoidWalker.getChild("R_leg").getChild("Mave").getChild("Head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition VoidWalker = partdefinition.addOrReplaceChild("VoidWalker", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition R_leg = VoidWalker.addOrReplaceChild("R_leg", CubeListBuilder.create(), PartPose.offset(-1.75F, -17.0F, 0.0F));

        PartDefinition Mave = R_leg.addOrReplaceChild("Mave", CubeListBuilder.create().texOffs(0, 0).addBox(-4.625F, -9.5F, -2.0F, 10.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 27).addBox(0.375F, -5.5F, -2.0F, 5.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(36, 18).addBox(-4.625F, -5.5F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(24, 39).addBox(-2.375F, -0.5F, -1.75F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.625F, -4.0F, -0.5F));

        PartDefinition R_arm = Mave.addOrReplaceChild("R_arm", CubeListBuilder.create().texOffs(32, 44).addBox(1.5F, -10.75F, 0.25F, 5.0F, 18.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(36, 0).addBox(0.5F, -5.75F, -1.75F, 3.0F, 15.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(4.875F, -4.75F, 0.25F));

        PartDefinition L_arm = Mave.addOrReplaceChild("L_arm", CubeListBuilder.create().texOffs(12, 21).addBox(-3.0F, -5.0F, -2.0F, 3.0F, 17.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.625F, -3.5F, 0.5F));

        PartDefinition Head = Mave.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(-0.625F, -7.0F, 0.25F));

        PartDefinition Jar = Head.addOrReplaceChild("Jar", CubeListBuilder.create().texOffs(42, 31).addBox(-4.0F, -4.0F, -3.0F, 6.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -1.5F, 0.25F));

        PartDefinition Eyes = Head.addOrReplaceChild("Eyes", CubeListBuilder.create().texOffs(0, 24).addBox(-3.5F, -7.75F, -3.5F, 5.0F, 5.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 8).addBox(-4.0F, -8.0F, -4.0F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F))
                .texOffs(0, 44).addBox(-10.0F, -17.0F, -1.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.5F, 1.25F));

        PartDefinition L_leg = R_leg.addOrReplaceChild("L_leg", CubeListBuilder.create().texOffs(42, 39).addBox(-1.0F, -2.5F, -2.0F, 3.0F, 19.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 0.5F, 0.0F));

        PartDefinition R_leg2 = R_leg.addOrReplaceChild("R_leg2", CubeListBuilder.create().texOffs(0, 21).addBox(-2.0F, -3.0F, -2.0F, 3.0F, 20.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        VoidWalkerEntity voidWalkerEntity = (VoidWalkerEntity) entity;

        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(VoidWalkerAnimations.WALK, limbSwing, limbSwingAmount, 1f, 7f);
        this.animate(voidWalkerEntity.idleAnimationState, VoidWalkerAnimations.IDLE, ageInTicks, 1f);
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        VoidWalker.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return VoidWalker;
    }
}