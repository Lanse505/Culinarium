package lanse505.culinarium.common.block.base;

import lanse505.culinarium.common.block.base.tile.CulinariumBaseTile;
import lanse505.culinarium.common.block.base.tile.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class CulinariumBaseTileBlock<T extends CulinariumBaseTile<T>> extends CulinariumBaseBlock implements EntityBlock {

  public CulinariumBaseTileBlock(Properties properties) {
    super(properties);
  }

  public abstract BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory();
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Nullable
  @Override
  public <R extends BlockEntity> BlockEntityTicker<R> getTicker(Level level, BlockState state, BlockEntityType<R> blockEntityType) {
    return (lvl, pos, blockState, blockEntity) -> {
      if (blockEntity instanceof ITickableBlockEntity) {
        if (lvl.isClientSide()) {
          ((ITickableBlockEntity) blockEntity).clientTick(lvl, pos, blockState, blockEntity);
        } else {
          ((ITickableBlockEntity) blockEntity).serverTick(lvl, pos, blockState, blockEntity);
        }
      }
    };
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return getTileEntityFactory().create(pos, state);
  }

}
