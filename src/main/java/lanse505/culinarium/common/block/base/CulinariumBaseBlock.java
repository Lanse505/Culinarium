package lanse505.culinarium.common.block.base;

import lanse505.culinarium.common.block.impl.tile.MillstoneTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public abstract class CulinariumBaseBlock extends Block {

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

  public List<VoxelShape> getBoundingBoxes(BlockState state, BlockGetter source, BlockPos pos) {
    return Collections.emptyList();
  }

  public boolean hasCustomBoxes(BlockState state, BlockGetter source, BlockPos pos) {
    return false;
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

  public NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    NonNullList<ItemStack> stacks = NonNullList.create();
    BlockEntity tileentity = worldIn.getBlockEntity(pos);
    if (tileentity instanceof MillstoneTile millstone) {
      for (int i = 0; i < millstone.getInventory().getSlots(); i++) {
        stacks.add(millstone.getInventory().getStackInSlot(i));
      }
    }
    return stacks;
  }

}
