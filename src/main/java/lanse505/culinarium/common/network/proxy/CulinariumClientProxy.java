package lanse505.culinarium.common.network.proxy;

import lanse505.culinarium.common.network.CulinariumCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CulinariumClientProxy extends CulinariumCommonProxy {
  @Override
  public Player getPlayer(Supplier<NetworkEvent.Context> context) {
    if (context.get().getDirection() == NetworkDirection.PLAY_TO_SERVER || context.get().getDirection() == NetworkDirection.LOGIN_TO_SERVER) {
      return context.get().getSender();
    }
    return Minecraft.getInstance().player;
  }
}
