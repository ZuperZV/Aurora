package net.zuperz.aurora.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.zuperz.aurora.block.ModBlocks;

public class VoidStoneBlock extends Block {


    public VoidStoneBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState p_222503_, Level p_222504_, BlockPos p_222505_, RandomSource p_222506_) {
        int i = p_222505_.getX();
        int j = p_222505_.getY();
        int k = p_222505_.getZ();

        double d0 = (double) i + p_222506_.nextDouble();
        double d1 = (double) j + 0.7;
        double d2 = (double) k + p_222506_.nextDouble();

        p_222504_.addParticle(
                ParticleTypes.ASH,
                d0, d1, d2,
                0.0, 2.05, 0.0
        );

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int l = 0; l < 1; l++) {
            blockpos$mutableblockpos.set(
                    i + Mth.nextInt(p_222506_, -10, 10),
                    j - p_222506_.nextInt(10),
                    k + Mth.nextInt(p_222506_, -10, 10)
            );

            BlockState blockstate = p_222504_.getBlockState(blockpos$mutableblockpos);
            if (!blockstate.isCollisionShapeFullBlock(p_222504_, blockpos$mutableblockpos)) {
                p_222504_.addParticle(
                        ParticleTypes.ASH,
                        (double) blockpos$mutableblockpos.getX() + p_222506_.nextDouble(),
                        (double) blockpos$mutableblockpos.getY() + p_222506_.nextDouble(),
                        (double) blockpos$mutableblockpos.getZ() + p_222506_.nextDouble(),
                        0.0, 2.05, 0.0
                );
            }
        }
    }
}
