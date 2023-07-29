package lanse505.culinarium.common.network.provider;

import lanse505.culinarium.common.network.CulinariumCommonProxy;
import lanse505.culinarium.common.network.proxy.CulinariumClientProxy;
import net.minecraftforge.fml.DistExecutor;

public class CulinariumClientProvider implements DistExecutor.SafeSupplier<CulinariumCommonProxy> {
  @Override
  public CulinariumCommonProxy get() {
    return new CulinariumClientProxy();
  }
}
