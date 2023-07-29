package lanse505.culinarium.common.network.packet;

import lanse505.culinarium.common.block.base.barrel.CulinariumBarrelBase;
import lanse505.culinarium.common.network.CulinariumNetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ServerboundUpdateSealPacket(BlockPos pos, boolean newSealState) {

  public ServerboundUpdateSealPacket(FriendlyByteBuf pBuffer) {
    this(pBuffer.readBlockPos(), pBuffer.readBoolean());
  }

  public static void handle(ServerboundUpdateSealPacket packet, Supplier<NetworkEvent.Context> context) {
    Player player = CulinariumNetworkHandler.getPlayer(context);
    if (player instanceof ServerPlayer) {
      return;
    }
    context.get().enqueueWork(() -> {
      BlockState state = player.level().getBlockState(packet.pos());
      if (state.getBlock() instanceof CulinariumBarrelBase<?> barrel) {
        state.setValue(CulinariumBarrelBase.SEALED, packet.newSealState());
      }
    });
    context.get().setPacketHandled(true);
  }

  public static void encode(ServerboundUpdateSealPacket packet, FriendlyByteBuf pBuffer) {
    pBuffer.writeBlockPos(packet.pos());
    pBuffer.writeBoolean(packet.newSealState());
  }

  public static ServerboundUpdateSealPacket decode(FriendlyByteBuf pBuffer) {
    return new ServerboundUpdateSealPacket(pBuffer.readBlockPos(), pBuffer.readBoolean());
  }


}
