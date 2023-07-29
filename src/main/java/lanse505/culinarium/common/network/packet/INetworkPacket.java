package lanse505.culinarium.common.network.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public interface INetworkPacket<PACKET extends INetworkPacket<PACKET>> {
  static <PACKET extends INetworkPacket<PACKET>> void handle(PACKET message, Supplier<NetworkEvent.Context> ctx) {
    if (message != null) {
      // Message should never be null unless something went horribly wrong decoding.
      // In which case we don't want to try enqueuing handling it, or set the packet as handled
      NetworkEvent.Context context = ctx.get();
      context.enqueueWork(() -> message.handle(message, context));
      context.setPacketHandled(true);
    }
  }

  static <PACKET extends INetworkPacket<PACKET>> void encode(PACKET message, Supplier<FriendlyByteBuf> buffer) {
    if (message != null) {
      FriendlyByteBuf buf = buffer.get();
      message.encode(message, buf);
    }
  }

  void handle(PACKET msg, NetworkEvent.Context context);

  void encode(PACKET msg, FriendlyByteBuf buffer);

}
