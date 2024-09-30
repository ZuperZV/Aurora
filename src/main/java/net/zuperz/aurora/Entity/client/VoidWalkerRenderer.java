package net.zuperz.aurora.Entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EnderEyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.phys.Vec3;
import net.zuperz.aurora.Entity.custom.ChainEntity;
import net.zuperz.aurora.Entity.custom.VoidWalkerEntity;
import net.zuperz.aurora.aurora;

public class VoidWalkerRenderer extends MobRenderer<VoidWalkerEntity, VoidWalkerModel<VoidWalkerEntity>> {
    private static final ResourceLocation VOID_WALKER_LOCATION = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,"textures/entity/void_walker/void_walker.png");

    public VoidWalkerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new VoidWalkerModel<>(pContext.bakeLayer(ModModelLayers.VOID_WALKER)), 0.5f);
        this.addLayer(new VoidWalkerEyesLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(VoidWalkerEntity pEntity) {
        return VOID_WALKER_LOCATION;
    }

    @Override
    public void render(VoidWalkerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
