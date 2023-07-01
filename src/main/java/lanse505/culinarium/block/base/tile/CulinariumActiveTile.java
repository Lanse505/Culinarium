package lanse505.culinarium.block.base.tile;

import lanse505.culinarium.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CulinariumActiveTile<T extends CulinariumActiveTile<T>> extends CulinariumBaseTile<T> implements ITickableBlockEntity<T>, MenuProvider, EntityBlock {

  public CulinariumActiveTile(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(base, pType, pPos, pBlockState);
  }

  public void openGui(Player player) {
    if (player instanceof ServerPlayer) {
      NetworkHooks.openScreen((ServerPlayer) player, this, getBlockPos());
    }
  }

  @NotNull
  @Override
  public  Component getDisplayName() {
    return Component.translatable("container.culinarium.grindstone");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
    return null; //TODO: Implement Menu
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return CulinariumBlockRegistry.MILLSTONE_TILE.get().create(pPos, pState);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Nullable
  @Override
  public <R extends BlockEntity> BlockEntityTicker<R> getTicker(Level pLevel, BlockState pState, BlockEntityType<R> type) {
    return (level, pos, state, blockentity) -> {
      if (blockentity instanceof ITickableBlockEntity tickable) {
        if (level.isClientSide()) {
          tickable.clientTick(level, pos, state, blockentity);
        } else {
          tickable.serverTick(level, pos, state, blockentity);
        }
      }
    };
  }
}
