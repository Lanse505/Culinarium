package lanse505.culinarium.common.network;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CulinariumCommonProxy {
  public Player getPlayer(Supplier<NetworkEvent.Context> context) {
    return context.get().getSender();
  }
}
