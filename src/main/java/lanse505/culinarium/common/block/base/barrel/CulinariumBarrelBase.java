package lanse505.culinarium.common.block.base.barrel;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.common.block.base.tile.CulinariumBarrelTileBase;
import lanse505.culinarium.common.block.base.tile.CulinariumBaseTile;
import lanse505.culinarium.common.util.DropsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public abstract class CulinariumBarrelBase<T extends CulinariumBaseTile<T>> extends CulinariumBaseTileBlock<T> {

  public static final BooleanProperty SEALED = BooleanProperty.create("sealed");

  public CulinariumBarrelBase(Properties properties) {
    super(properties);
  }

  @NotNull
  @Override
  public RotationType getRotationType() {
    return RotationType.SIX_WAY;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
    return true;
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext pContext) {
    return defaultBlockState().setValue(SEALED, false);
  }

  @Override
  public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
    return direction != null && direction.getAxis().isHorizontal();
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder.add(SEALED));
  }

  @Override
  public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
    super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    if (state.getValue(SEALED)) return;
    boolean hasSignal = Arrays.stream(Direction.values()).anyMatch(dir -> getNeighborSignal(level, pos, dir));
    if (hasSignal)
      state.setValue(SEALED, true);
    else
      state.setValue(SEALED, false);
  }

  private boolean getNeighborSignal(SignalGetter pSignalGetter, BlockPos pPos, Direction pDirection) {
    for (Direction direction : Direction.values())
      if (direction != pDirection && pSignalGetter.hasSignal(pPos.relative(direction), direction))
        return true;
    BlockPos blockpos = pPos.above();
    for (Direction direction : Direction.values())
      if (direction != Direction.DOWN && pSignalGetter.hasSignal(blockpos.relative(direction), direction))
        return true;
    return pSignalGetter.hasSignal(pPos, Direction.DOWN);
  }

  @Override
  public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
    return super.isValidSpawn(state, level, pos, type, entityType) && state.getValue(SEALED);
  }

  @Override
  public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
    return true;
  }

  @Override
  public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
    return 0;
  }

  @Override
  public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
    super.onNeighborChange(state, level, pos, neighbor);

  }

  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
    T be = getTypedBE(level, pos);
    if (be instanceof CulinariumBarrelTileBase<?>) {
      //T barrelTileT = (T) btt;
      if (stack.getTag() != null) {
        state.setValue(SEALED, stack.getTag().getBoolean("Sealed"));
      } else {
        state.setValue(SEALED, false);
      }
    }
  }

  @Override
  public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
    T be = getTypedBE(level, pos);
    if (be instanceof CulinariumBarrelTileBase<?>) {
      // Setup stack
      ItemStack stack = new ItemStack(this.asItem());
      // Setup NBT
      CompoundTag tag = new CompoundTag();
      tag.putBoolean("Sealed", state.getValue(SEALED));
      getAdditionalNBTForDrop(level, player, pos, state, blockEntity, tool, tag);
      stack.setTag(tag);
      // Setup ItemEntity & Drop
      ItemEntity dropped = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
      dropped.addDeltaMovement(DropsUtil.getDropDelta(pos, level));
      level.addFreshEntity(dropped);
    }
    super.playerDestroy(level, player, pos, state, blockEntity, tool);
  }

}
