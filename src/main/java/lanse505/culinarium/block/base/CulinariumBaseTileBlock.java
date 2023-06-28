package lanse505.culinarium.block.base;

import lanse505.culinarium.block.base.tile.CulinariumBaseTile;
import lanse505.culinarium.block.base.tile.ITickableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class CulinariumBaseTileBlock<T extends CulinariumBaseTile<T>> extends CulinariumBaseBlock implements EntityBlock {

  private final Class<T> beClass;

  public CulinariumBaseTileBlock(String name, Properties properties, Class<T> tileClass) {
    super(name, properties);
    this.beClass = tileClass;
  }

  public abstract BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory();

  @Override
  @SuppressWarnings("deprecation")
  public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
    getTile(worldIn, pos).ifPresent(tile -> tile.onNeighborChanged(blockIn, fromPos));
  }


  @Override
  @SuppressWarnings("deprecation")
  public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
    return getTile(worldIn, pos)
            .map(tile -> tile.onActivated(player, hand, ray.getDirection(), ray.getLocation().x, ray.getLocation().y, ray.getLocation().z))
            .orElseGet(() -> super.use(state, worldIn, pos, player, hand, ray));
  }


  public Optional<T> getTile(BlockGetter access, BlockPos pos) {
    BlockEntity be = access.getBlockEntity(pos);
    if (beClass.isInstance(be)) return Optional.of(beClass.cast(be));
    return Optional.empty();
  }

  public Class<T> getBlockEntityClass() {
    return beClass;
  }

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
  public <T extends BlockEntity> GameEventListener getListener(ServerLevel serverLevel, T be) {
    return EntityBlock.super.getListener(serverLevel, be);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return getTileEntityFactory().create(pos, state);
  }

}
