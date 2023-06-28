package lanse505.culinarium.block.base.tile;

import lanse505.culinarium.block.base.CulinariumBaseTileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class CulinariumActiveTile<T extends CulinariumActiveTile<T>> extends CulinariumBaseTile<T> implements ITickableBlockEntity<T>, MenuProvider {

  public CulinariumActiveTile(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(base, pType, pPos, pBlockState);
  }

  public void openGui(Player player) {
    if (player instanceof ServerPlayer) {
      NetworkHooks.openScreen((ServerPlayer) player, this, getBlockPos());
    }
  }

  @Override
  public Component getDisplayName() {
    return null;
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
    return null;
  }
}
