package lanse505.culinarium.common.network.proxy;

import lanse505.culinarium.common.network.CulinariumCommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CulinariumClientProxy extends CulinariumCommonProxy {
  @Override
  public Player getPlayer(NetworkEvent.Context context) {
    if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER || context.getDirection() == NetworkDirection.LOGIN_TO_SERVER) {
      return context.getSender();
    }
    return Minecraft.getInstance().player;
  }
}
