package lanse505.culinarium.common.block.base.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Collections;
import java.util.List;

public interface ICulinariumCoreBlock {

    default List<VoxelShape> getBoundingBoxes(BlockState state, BlockGetter source, BlockPos pos) {
        return Collections.emptyList();
    }

    default boolean hasCustomBoxes(BlockState state, BlockGetter source, BlockPos pos) { return false; }

    default NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        return NonNullList.create();
    }

}
