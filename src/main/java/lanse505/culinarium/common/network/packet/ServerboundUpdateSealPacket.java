package lanse505.culinarium.common.network.packet;

import lanse505.culinarium.common.block.base.tile.CulinariumBarrelTileBase;
import lanse505.culinarium.common.network.CulinariumNetworkHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

public record ServerboundUpdateSealPacket(BlockPos pos,
                                          boolean newSealState) implements INetworkPacket<ServerboundUpdateSealPacket> {

  public ServerboundUpdateSealPacket(FriendlyByteBuf pBuffer) {
    this(pBuffer.readBlockPos(), pBuffer.readBoolean());
  }

  public static ServerboundUpdateSealPacket decode(FriendlyByteBuf buffer) {
    return new ServerboundUpdateSealPacket(buffer);
  }

  @Override
  public void handle(ServerboundUpdateSealPacket msg, NetworkEvent.Context context) {
    Player player = CulinariumNetworkHandler.getPlayer(context);
    if (player instanceof AbstractClientPlayer) {
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
  public void encode(ServerboundUpdateSealPacket msg, FriendlyByteBuf buffer) {
    buffer.writeBlockPos(msg.pos);
    buffer.writeBoolean(msg.newSealState);
  }

}
