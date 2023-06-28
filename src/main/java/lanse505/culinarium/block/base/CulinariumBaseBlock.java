package lanse505.culinarium.block.base;

import lanse505.culinarium.block.base.misc.DistanceRayTraceResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public abstract class CulinariumBaseBlock extends Block {
  private CreativeModeTab group = CreativeModeTabs.SEARCH;
  private final String name;

  public CulinariumBaseBlock(String name, Properties pProperties) {
    super(pProperties);
    this.name = name;
  }

  @Nullable
  protected static DistanceRayTraceResult rayTraceBox(BlockPos pos, Vec3 start, Vec3 end, VoxelShape shape) {
    BlockHitResult bbResult = shape.clip(start, end, pos);
    if (bbResult != null) {
      Vec3 hitVec = bbResult.getLocation();
      Direction sideHit = bbResult.getDirection();
      double dist = start.distanceTo(hitVec);
      return new DistanceRayTraceResult(hitVec, sideHit, pos, shape, dist);
    }
    return null;
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

  public Supplier<Item> getItemBlockFactory() {
    return () -> null;
    // TODO: Figure out how to fix this also CreativeModeTabs how?
    //return () -> (Item) new BlockItem(this, new Item.Properties().tab(this.group));
  }

  public List<VoxelShape> getBoundingBoxes(BlockState state, BlockGetter source, BlockPos pos) {
    return Collections.emptyList();
  }

  public boolean hasCustomBoxes(BlockState state, BlockGetter source, BlockPos pos) {
    return false;
  }

  @Nullable
  protected HitResult rayTraceBoxesClosest(Vec3 start, Vec3 end, BlockPos pos, List<VoxelShape> boxes) {
    List<DistanceRayTraceResult> results = new ArrayList<>();
    for (VoxelShape box : boxes) {
      DistanceRayTraceResult hit = rayTraceBox(pos, start, end, box);
      if (hit != null)
        results.add(hit);
    }
    HitResult closestHit = null;
    double curClosest = Double.MAX_VALUE;
    for (DistanceRayTraceResult hit : results) {
      if (curClosest > hit.getDistance()) {
        closestHit = hit;
        curClosest = hit.getDistance();
      }
    }
    return closestHit;
  }

  public CreativeModeTab getItemGroup() {
    return group;
  }

  public void setItemGroup(CreativeModeTab group) {
    this.group = group;
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
    // TODO: Add support for TE Inventory
    return stacks;
  }

  public boolean hasIndividualRenderVoxelShape() {
    return false;
  }

}
