package net.zuperz.aurora.Entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.zuperz.aurora.Entity.custom.ChainEntity;
import net.zuperz.aurora.aurora;

public class ChainRenderer extends MobRenderer<ChainEntity, ChainModel<ChainEntity>> {
    private static final ResourceLocation CHAIN_LOCATION = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID,"textures/entity/chain.png");

    public ChainRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ChainModel<>(pContext.bakeLayer(ModModelLayers.CHAIN)), 0.55f);
    }

    @Override
    public ResourceLocation getTextureLocation(ChainEntity pEntity) {
        return CHAIN_LOCATION;
    }

    @Override
    public void render(ChainEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
