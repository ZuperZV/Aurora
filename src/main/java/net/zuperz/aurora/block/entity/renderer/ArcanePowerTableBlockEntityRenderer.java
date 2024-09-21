package net.zuperz.aurora.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.items.IItemHandler;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.custom.ArcanePowerTable;
import net.zuperz.aurora.block.entity.custom.ArcanePowerTableBlockEntity;
import net.zuperz.aurora.block.entity.custom.ArcanePowerTableBlockEntity;

public class ArcanePowerTableBlockEntityRenderer implements BlockEntityRenderer<ArcanePowerTableBlockEntity> {

    private final ItemRenderer itemRenderer;

    public ArcanePowerTableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ArcanePowerTableBlockEntity blockEntity, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockPos pos = blockEntity.getBlockPos();
        if (pos == null) {
            return;
        }

        BlockState pState = level.getBlockState(pos);
        if (pState.getBlock() != ModBlocks.ARCANE_POWER_TABLE.get()) {
            return;

        }

        Lazy<IItemHandler> itemHandler = blockEntity.getItemHandler();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.7, 0.5);
        poseStack.scale(0.5f, 0.5f, 0.5f);

        for (int i = 0; i < 3; i++) {
            ItemStack stack = itemHandler.get().getStackInSlot(i);

            if (!stack.isEmpty()) {
                poseStack.pushPose();

                Direction facing = pState.getValue(ArcanePowerTable.FACING);
                switch (facing) {
                    case SOUTH, NORTH -> {
                        switch (i) {
                            case 0 -> poseStack.translate(1.0, 1.15, 0.0);
                            case 1 -> poseStack.translate(0.0, 1.2, 0.0);
                            case 2 -> poseStack.translate(-1.0, 1.15, 0.0);
                        }
                    }
                    case EAST, WEST -> {
                        switch (i) {
                            case 0 -> poseStack.translate(0.0, 1.15, 1.0);
                            case 1 -> poseStack.translate(0.0, 1.2, 0.0);
                            case 2 -> poseStack.translate(0.0, 1.15, -1.0);
                        }
                    }
                }

                poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));

                try {
                    itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(level, pos), packedOverlay, poseStack, bufferSource, level, 1);
                } catch (Exception e) {
                }

                poseStack.popPose();
            }
        }

        ItemStack outputStack = blockEntity.getOutputItems().getStackInSlot(0);
        if (!outputStack.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0, 1.6, 0.0);
            poseStack.mulPose(Axis.YP.rotationDegrees(blockEntity.getRenderingRotation()));
            try {
                itemRenderer.renderStatic(outputStack, ItemDisplayContext.FIXED, getLightLevel(level, pos), packedOverlay, poseStack, bufferSource, level, 1);
            } catch (Exception e) {
            }
            poseStack.popPose();
        }

        poseStack.popPose();
    }


    private int getLightLevel(Level level, BlockPos pos) {
        return LightTexture.FULL_BRIGHT;
    }
}