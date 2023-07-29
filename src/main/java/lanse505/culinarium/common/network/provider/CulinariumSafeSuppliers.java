package lanse505.culinarium.common.network.provider;

import lanse505.culinarium.common.network.CulinariumCommonProxy;
import net.minecraftforge.fml.DistExecutor;

import java.util.function.Supplier;

public class CulinariumSafeSuppliers {

  public static Supplier<DistExecutor.SafeSupplier<CulinariumCommonProxy>> getClientProxy() {
    return CulinariumClientProvider::new;
  }

  public static Supplier<DistExecutor.SafeSupplier<CulinariumCommonProxy>> getServerProxy() {
    return CulinariumServerProvider::new;
  }

}
