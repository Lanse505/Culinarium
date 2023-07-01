package lanse505.culinarium.block.base.tile;

import lanse505.culinarium.block.base.CulinariumBaseTileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiPredicate;

public class CulinariumBaseTile<T extends CulinariumBaseTile<T>> extends BlockEntity {

  private final CulinariumBaseTileBlock<T> block;

  public CulinariumBaseTile(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
    this.block = base;
  }

  public void markForUpdate() {
    this.level.sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
    setChanged();
  }

  public boolean isClient() {
    return this.level.isClientSide;
  }

  public boolean isServer() {
    return !isClient();
  }

  public CulinariumBaseTileBlock<T> getBasicTileBlock() {
    return block;
  }

}
