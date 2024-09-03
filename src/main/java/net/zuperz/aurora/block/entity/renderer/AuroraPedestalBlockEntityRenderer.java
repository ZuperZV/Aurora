package net.zuperz.aurora.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.zuperz.aurora.block.entity.custom.AuroraPedestalBlockEntity;

public class AuroraPedestalBlockEntityRenderer implements BlockEntityRenderer<AuroraPedestalBlockEntity> {

    private final ItemRenderer itemRenderer;

    public AuroraPedestalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer(); // Use the provided ItemRenderer
    }

    @Override
    public void render(AuroraPedestalBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        ItemStack stack = blockEntity.getItem(0);

        if (!stack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.5, 1.15, 0.5); // Position the item in the center and above the pedestal
            poseStack.scale(0.5f, 0.5f, 0.5f); // Scale the item
            poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation())); // Rotate the item

            // Render the item with the correct light level
            itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(blockEntity.getLevel(), blockEntity.getBlockPos()), packedOverlay, poseStack, bufferSource, blockEntity.getLevel(), 1);

            poseStack.popPose();
        }
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}
