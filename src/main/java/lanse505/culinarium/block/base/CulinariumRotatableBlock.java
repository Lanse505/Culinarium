package lanse505.culinarium.block.base;

import lanse505.culinarium.block.base.tile.CulinariumBaseTile;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class CulinariumRotatableBlock<T extends CulinariumBaseTile<T>> extends CulinariumBaseTileBlock<T> {
  public static final DirectionProperty FACING_ALL = DirectionProperty.create("facing", Direction.values());
  public static final DirectionProperty FACING_HORIZONTAL = DirectionProperty.create("subfacing", Direction.Plane.HORIZONTAL);

  public CulinariumRotatableBlock(Properties properties) {
    super(properties);
  }

  @Nonnull
  public RotationType getRotationType() {
    return RotationType.FOUR_WAY;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context) {
    return getRotationType().getHandler().getStateForPlacement(this, context);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_206840_1_) {
    super.createBlockStateDefinition(p_206840_1_);
    if (this.getRotationType().getProperties() != null) p_206840_1_.add(this.getRotationType().getProperties());
  }

  @Override
  public BlockState rotate(BlockState state, Rotation rotation) {
    if (getRotationType().getProperties().length > 0){
      return state.setValue(getRotationType().getProperties()[0], rotation.rotate(state.getValue(getRotationType().getProperties()[0])));
    }
    return super.rotate(state, rotation);
  }

  @Override
  public BlockState mirror(BlockState state, Mirror mirrorIn) {
    if (getRotationType().getProperties().length > 0){
      return state.rotate(mirrorIn.getRotation(state.getValue(getRotationType().getProperties()[0])));
    }
    return super.mirror(state, mirrorIn);
  }

  public enum RotationType {
    NONE((block, context) -> block.defaultBlockState()),
    FOUR_WAY(((block, context) -> block.defaultBlockState().setValue(FACING_HORIZONTAL, context.getHorizontalDirection().getOpposite())), FACING_HORIZONTAL),
    SIX_WAY((block, context) -> block.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection().getOpposite()), FACING_ALL),
    TWENTY_FOUR_WAY((block, context) -> {
      //TODO: Sub facing
      return block.defaultBlockState().setValue(FACING_ALL, context.getNearestLookingDirection());
    }, FACING_ALL, FACING_HORIZONTAL);

    private final RotationHandler handler;
    private final DirectionProperty[] properties;

    RotationType(RotationHandler handler, DirectionProperty... properties) {
      this.handler = handler;
      this.properties = properties;
    }

    public RotationHandler getHandler() {
      return handler;
    }

    public DirectionProperty[] getProperties() {
      return properties;
    }
  }

  public interface RotationHandler {
    BlockState getStateForPlacement(CulinariumRotatableBlock block, BlockPlaceContext context);
  }
}
