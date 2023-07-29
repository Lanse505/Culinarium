package lanse505.culinarium.common.network.proxy;

import lanse505.culinarium.common.network.CulinariumCommonProxy;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CulinariumServerProxy extends CulinariumCommonProxy {
  @Override
  public Player getPlayer(Supplier<NetworkEvent.Context> context) {
    return context.get().getSender();
  }
}
