package net.zuperz.aurora.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.custom.AlterBlockEntity;

public class AlterBlockEntityRenderer implements BlockEntityRenderer<AlterBlockEntity> {

    private final ItemRenderer itemRenderer;

    public AlterBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(AlterBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Lazy<IItemHandler> itemHandler = blockEntity.getItems();
        Level level = blockEntity.getLevel();
        BlockPos pos = blockEntity.getBlockPos();

        if (containsBeam(level, pos)) {
            poseStack.pushPose();
            poseStack.translate(0.5, 0.8, 0.5);
            poseStack.scale(0.5f, 0.5f, 0.5f);

            // Render input slots (0 to 4)
            for (int i = 0; i < 5; i++) {
                ItemStack stack = itemHandler.get().getStackInSlot(i);

                if (!stack.isEmpty()) {
                    poseStack.pushPose();

                    // Adjust positions based on the input slot
                    switch (i) {
                        case 0:
                            poseStack.translate(4.0, 3.0, 4.0);
                            break;
                        case 1:
                            poseStack.translate(-4.0, 3.0, -4.0);
                            break;
                        case 2:
                            poseStack.translate(4.0, 3.0, -4.0);
                            break;
                        case 3:
                            poseStack.translate(-4.0, 3.0, 4.0);
                            break;
                        case 4:
                            poseStack.translate(0.0, 1.5, 0.0);
                            break;
                    }

                    poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));

                    itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(level, pos), packedOverlay, poseStack, bufferSource, level, 1);

                    poseStack.popPose();
                }
            }

            // Render output slot (index 0 of outputItems)
            ItemStack outputStack = blockEntity.getOutputItems().getStackInSlot(0);
            if (!outputStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate(0.0, 2.0, 0.0); // Position for output slot

                poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));

                itemRenderer.renderStatic(outputStack, ItemDisplayContext.FIXED, getLightLevel(level, pos), packedOverlay, poseStack, bufferSource, level, 1);

                poseStack.popPose();
            }

            poseStack.popPose();
        }
    }

    private boolean containsBeam(Level level, BlockPos pos) {
        for (BlockPos beamPos : getBeamPositions(pos)) {
            if (level.getBlockState(beamPos).getBlock() == ModBlocks.BEAM.get()) {
                return true;
            }
        }
        return false;
    }

    private BlockPos[] getBeamPositions(BlockPos pos) {
        // Adjust the beam positions if the arrangement changes
        return new BlockPos[]{
                pos.offset(2, 0, -2),
                pos.offset(-2, 0, -2),
                pos.offset(2, 0, 2),
                pos.offset(-2, 0, 2),
        };
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(blockLight, skyLight);
    }
}