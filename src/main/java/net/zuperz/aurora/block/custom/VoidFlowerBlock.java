package net.zuperz.aurora.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.zuperz.aurora.block.ModBlocks;

import java.util.List;

public class VoidFlowerBlock extends FlowerBlock  implements SuspiciousEffectHolder {

    public VoidFlowerBlock(SuspiciousStewEffects p_330645_, Properties p_304822_) {
        super(p_330645_, p_304822_);
    }

    public VoidFlowerBlock(Holder<MobEffect> p_316154_, float p_332744_, BlockBehaviour.Properties p_53514_) {
        this(makeEffectList(p_316154_, p_332744_), p_53514_);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos.below());

        return blockBelow.is(ModBlocks.LUMINOUS_VOID_STONE) ||
                blockBelow.is(ModBlocks.VOID_STONE.get()) ||
                blockBelow.is(ModBlocks.COBBLE_VOID_STONE.get());
    }
}