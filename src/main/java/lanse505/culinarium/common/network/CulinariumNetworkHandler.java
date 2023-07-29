package lanse505.culinarium.common.network;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.network.packet.INetworkPacket;
import lanse505.culinarium.common.network.packet.ServerboundUpdateSealPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Optional;
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

  public static Player getPlayer(NetworkEvent.Context context) {
    return Culinarium.proxy.getPlayer(context);
  }

  public void init() {
    //registerClientToServer(ServerboundUpdateSealPacket.class, ServerboundUpdateSealPacket::decode);
    registerMessage(ServerboundUpdateSealPacket.class, ServerboundUpdateSealPacket::decode, NetworkDirection.PLAY_TO_SERVER);
  }z


  protected <MSG extends INetworkPacket<MSG>> void registerClientToServer(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder) {
    registerMessage(type, decoder, NetworkDirection.PLAY_TO_SERVER);
  }

  protected <MSG extends INetworkPacket<MSG>> void registerServerToClient(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder) {
    registerMessage(type, decoder, NetworkDirection.PLAY_TO_CLIENT);
  }

  private <MSG extends INetworkPacket<MSG>> void registerMessage(Class<MSG> type, Function<FriendlyByteBuf, MSG> decoder, NetworkDirection networkDirection) {
    INSTANCE.registerMessage(index++, type, INetworkPacket::encode, decoder, INetworkPacket::handle, Optional.of(networkDirection));
  }

  /**
   * Send this message to the specified player.
   *
   * @param message - the message to send
   * @param player  - the player to send it to
   */
  public <MSG> void sendTo(MSG message, ServerPlayer player) {
    //Validate it is not a fake player, even though none of our code should call this with a fake player
    if (!(player instanceof FakePlayer)) {
      INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
  }

  /**
   * Send this message to everyone connected to the server.
   *
   * @param message - message to send
   */
  public <MSG> void sendToAll(MSG message) {
    INSTANCE.send(PacketDistributor.ALL.noArg(), message);
  }

  /**
   * Send this message to everyone connected to the server if the server has loaded.
   *
   * @param message - message to send
   *
   * @apiNote This is useful for reload listeners
   */
  public <MSG> void sendToAllIfLoaded(MSG message) {
    if (ServerLifecycleHooks.getCurrentServer() != null) {
      //If the server has loaded, send to all players
      sendToAll(message);
    }
  }

  /**
   * Send this message to everyone within the supplied dimension.
   *
   * @param message   - the message to send
   * @param dimension - the dimension to target
   */
  public <MSG> void sendToDimension(MSG message, ResourceKey<Level> dimension) {
    INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimension), message);
  }

  /**
   * Send this message to the server.
   *
   * @param message - the message to send
   */
  public <MSG> void sendToServer(MSG message) {
    INSTANCE.sendToServer(message);
  }

  public <MSG> void sendToAllTracking(MSG message, Entity entity) {
    INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
  }

  public <MSG> void sendToAllTrackingAndSelf(MSG message, Entity entity) {
    INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), message);
  }

  public <MSG> void sendToAllTracking(MSG message, BlockEntity tile) {
    sendToAllTracking(message, tile.getLevel(), tile.getBlockPos());
  }

  public <MSG> void sendToAllTracking(MSG message, Level world, BlockPos pos) {
    if (world instanceof ServerLevel level) {
      //If we have a ServerWorld just directly figure out the ChunkPos to not require looking up the chunk
      // This provides a decent performance boost over using the packet distributor
      level.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> sendTo(message, p));
    } else {
      //Otherwise, fallback to entities tracking the chunk if some mod did something odd and our world is not a ServerWorld
      INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()))), message);
    }
  }

}
