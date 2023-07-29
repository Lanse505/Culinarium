package lanse505.culinarium.common.network;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.network.packet.ServerboundUpdateSealPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CulinariumNetworkHandler {
  private static final String PROTOCOL_VERSION = "1";

  private int index = 0;

  public static final SimpleChannel INSTANCE =
      NetworkRegistry.newSimpleChannel(
          new ResourceLocation(Culinarium.MODID, "main"),
          () -> PROTOCOL_VERSION,
          PROTOCOL_VERSION::equals,
          PROTOCOL_VERSION::equals
      );

  public static Player getPlayer(Supplier<NetworkEvent.Context> context) {
    return Culinarium.proxy.getPlayer(context);
  }

  public void init() {
    registerMessage(ServerboundUpdateSealPacket.class, ServerboundUpdateSealPacket::encode, ServerboundUpdateSealPacket::decode, ServerboundUpdateSealPacket::handle);
  }

  private <MESSAGE> void registerMessage(Class<MESSAGE> type, BiConsumer<MESSAGE, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MESSAGE> decoder, BiConsumer<MESSAGE, Supplier<NetworkEvent.Context>> consumer) {
    registerMessage(index++, type, encoder, decoder, consumer);
  }

  public <MESSAGE> void registerMessage(int id, Class<MESSAGE> type, BiConsumer<MESSAGE, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MESSAGE> decoder, BiConsumer<MESSAGE, Supplier<NetworkEvent.Context>> consumer) {
    INSTANCE.registerMessage(id, type, encoder, decoder, consumer);
  }

}
