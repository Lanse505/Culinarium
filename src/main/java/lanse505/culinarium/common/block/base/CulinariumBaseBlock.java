package lanse505.culinarium.common.block.base;

import lanse505.culinarium.common.block.base.core.ICulinariumCoreBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

public abstract class CulinariumBaseBlock extends Block implements ICulinariumCoreBlock {

    public CulinariumBaseBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    @Nonnull
    @SuppressWarnings("deprecation")
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext) {
        if (hasCustomBoxes(state, world, pos)) {
            VoxelShape shape = Shapes.empty();
            for (VoxelShape shape1 : getBoundingBoxes(state, world, pos)) {
                shape = Shapes.join(shape, shape1, BooleanOp.OR);
            }
            return shape;
        }
        return super.getCollisionShape(state, world, pos, selectionContext);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            Containers.dropContents(worldIn, pos, getDynamicDrops(state, worldIn, pos, newState, isMoving));
            worldIn.updateNeighbourForOutputSignal(pos, this);
        }
        super.onRemove(state, worldIn, pos, newState, isMoving);
    }

}
