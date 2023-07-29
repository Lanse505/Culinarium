package lanse505.culinarium.common.network.provider;

import lanse505.culinarium.common.network.CulinariumCommonProxy;
import lanse505.culinarium.common.network.proxy.CulinariumServerProxy;
import net.minecraftforge.fml.DistExecutor;

public class CulinariumServerProvider implements DistExecutor.SafeSupplier<CulinariumCommonProxy> {
  @Override
  public CulinariumCommonProxy get() {
    return new CulinariumServerProxy();
  }
}
