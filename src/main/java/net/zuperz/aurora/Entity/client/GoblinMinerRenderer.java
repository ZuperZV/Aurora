package net.zuperz.aurora.Entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.zuperz.aurora.Entity.custom.GoblinMinerEntity;
import net.zuperz.aurora.aurora;

public class GoblinMinerRenderer extends MobRenderer<GoblinMinerEntity, GoblinMinerModel<GoblinMinerEntity>> {
    private static final ResourceLocation CHAIN_LOCATION = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,"textures/entity/chain.png");

    public GoblinMinerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new GoblinMinerModel<>(pContext.bakeLayer(ModModelLayers.GOBLIN_MINER)), 0.55f);
    }

    @Override
    public ResourceLocation getTextureLocation(GoblinMinerEntity pEntity) {
        return CHAIN_LOCATION;
    }

    @Override
    public void render(GoblinMinerEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
