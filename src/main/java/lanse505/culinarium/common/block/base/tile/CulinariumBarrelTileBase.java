package lanse505.culinarium.common.block.base.tile;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.common.block.impl.block.barrel.BrewingBarrelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class CulinariumBarrelTileBase<T extends CulinariumBarrelTileBase<T>> extends CulinariumActiveTile<T> {

  public static final int DEFAULT_TANK_CAPACITY = 8000;

  public CulinariumBarrelTileBase(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(base, pType, pPos, pBlockState);
  }

  public void toggleSeal(BlockPos pos, boolean newSealState) {
    if (this.getLevel() != null && this.getLevel() instanceof ServerLevel) {
      Level level = this.getLevel();
      level.setBlock(pos, level.getBlockState(pos).setValue(BrewingBarrelBlock.SEALED, newSealState), Block.UPDATE_IMMEDIATE);
    }
  }

  public abstract void writeTileNBT(CompoundTag tag);

  public abstract void readTileNBT(CompoundTag tag);

  @Override
  protected void saveAdditional(CompoundTag pTag) {
    super.saveAdditional(pTag);
    writeTileNBT(pTag);
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    writeTileNBT(tag);
    return tag;
  }

  @Override
  public void load(CompoundTag pTag) {
    super.load(pTag);
    readTileNBT(pTag);
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    super.handleUpdateTag(tag);
    readTileNBT(tag);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    if (pkt.getTag() != null) {
      this.handleUpdateTag(pkt.getTag());
    }
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
