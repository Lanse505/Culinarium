package lanse505.culinarium.common.network.packet;

import lanse505.culinarium.common.block.base.barrel.CulinariumBarrelBase;
import lanse505.culinarium.common.block.base.tile.CulinariumBarrelTileBase;
import lanse505.culinarium.common.network.CulinariumNetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ServerboundUpdateSealPacket(BlockPos pos, boolean newSealState) implements INetworkPacket<ServerboundUpdateSealPacket> {

  public ServerboundUpdateSealPacket(FriendlyByteBuf pBuffer) {
    this(pBuffer.readBlockPos(), pBuffer.readBoolean());
  }

  @Override
  public void handle(NetworkEvent.Context context) {
    Player player = CulinariumNetworkHandler.getPlayer(context);
    if (player instanceof ServerPlayer) {
      return;
    }
    context.enqueueWork(() -> {
      BlockEntity be = player.level().getBlockEntity(pos);
      if (be instanceof CulinariumBarrelTileBase<?> barrel) {
        barrel.toggleSeal(pos, newSealState);
      }
    });
    context.setPacketHandled(true);
  }

  @Override
  public void encode(FriendlyByteBuf buffer) {
      buffer.writeBlockPos(pos);
      buffer.writeBoolean(newSealState);
  }

  public static ServerboundUpdateSealPacket decode(FriendlyByteBuf buffer) {
    return new ServerboundUpdateSealPacket(buffer);
  }

}
