package lanse505.culinarium.register.creativetabs;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;

public class CulinariumCreativeTabs {

  public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Culinarium.MODID);

  public static final CreativeTabRegistryObject MAIN = CREATIVE_TABS.registerMain(
          Component.translatable("itemGroup.culinarium"),
          CulinariumBlockRegistry.MILLSTONE_ITEM,
          CreativeModeTab.Builder::withSearchBar
  );




}
