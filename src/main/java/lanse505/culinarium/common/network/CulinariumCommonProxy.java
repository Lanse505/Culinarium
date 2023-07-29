package lanse505.culinarium.common.network;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class CulinariumCommonProxy {
  public Player getPlayer(NetworkEvent.Context context) {
    return context.getSender();
  }
}
