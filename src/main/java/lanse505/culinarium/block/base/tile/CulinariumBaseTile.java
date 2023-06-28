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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

public class CulinariumBaseTile<T extends CulinariumBaseTile<T>> extends BlockEntity {

  private final CulinariumBaseTileBlock<T> block;

  public CulinariumBaseTile(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(pType, pPos, pBlockState);
    this.block = base;
  }

  @ParametersAreNonnullByDefault
  public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
    return InteractionResult.PASS;
  }

  public void onNeighborChanged(Block blockIn, BlockPos fromPos) {

  }

  // BlockEntity.Read
  @Override
  public void load(CompoundTag compound) {
    NBTManager.getInstance().readTileEntity(this, compound);
    super.load(compound);
  }

  @Override
  protected void saveAdditional(CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    NBTManager.getInstance().writeTileEntity(this, compoundTag);
  }

  public void markForUpdate() {
    this.level.sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
    setChanged();
  }

  @Override
  @Nonnull
  public CompoundTag getUpdateTag() {
    CompoundTag compoundTag = new CompoundTag();
    saveAdditional(compoundTag);
    return compoundTag;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    load(pkt.getTag());
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    CompoundTag tag = new CompoundTag();
    saveAdditional(tag);
    return ClientboundBlockEntityDataPacket.create(this, blockEntity -> tag);
  }

  public void updateNeigh() {
    this.level.updateNeighborsAt(this.worldPosition, this.getBlockState().getBlock());
    this.level.sendBlockUpdated(this.worldPosition, this.level.getBlockState(worldPosition), this.level.getBlockState(worldPosition), 3);
  }

  public void syncObject(Object object){
    if (isServer()){
      CompoundTag nbt = NBTManager.getInstance().writeTileEntityObject(this, object, new CompoundTag());
      Titanium.NETWORK.sendToNearby(this.level, this.worldPosition, 64, new TileFieldNetworkMessage(this.worldPosition, nbt));
    }
  }

  public void handleSyncObject(CompoundTag nbt){
    NBTManager.getInstance().readTileEntity(this, nbt);
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
