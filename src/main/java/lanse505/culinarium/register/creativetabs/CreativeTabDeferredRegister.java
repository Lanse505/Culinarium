package lanse505.culinarium.register.creativetabs;

import lanse505.culinarium.register.core.WrappedDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class CreativeTabDeferredRegister extends WrappedDeferredRegister<CreativeModeTab> {

  private final String modid;

  protected CreativeTabDeferredRegister(String modid) {
    super(modid, Registries.CREATIVE_MODE_TAB);
    this.modid = modid;
  }

  /**
   * @apiNote We manually require the title and icon to be passed so that we ensure all tabs have one.
   */
  public CreativeTabRegistryObject registerMain(Component title, Supplier icon, UnaryOperator<CreativeModeTab.Builder> operator) {
    return register(modid, title, icon, operator);
  }

  /**
   * @apiNote We manually require the title and icon to be passed so that we ensure all tabs have one.
   */
  public CreativeTabRegistryObject register(String name, Component title, Supplier<Item> icon, UnaryOperator<CreativeModeTab.Builder> operator) {
    return register(name, () -> {
      CreativeModeTab.Builder builder = CreativeModeTab.builder()
              .title(title)
              .icon(() -> icon.get().getDefaultInstance())
              .withTabFactory(CulinariumCreativeTab::new)
              //TODO - 1.21: Check if Vanilla adds any other creative tabs and if so ensure we are after them
              .withTabsBefore(CreativeModeTabs.BUILDING_BLOCKS, CreativeModeTabs.COLORED_BLOCKS, CreativeModeTabs.NATURAL_BLOCKS, CreativeModeTabs.FUNCTIONAL_BLOCKS,
                      CreativeModeTabs.REDSTONE_BLOCKS, CreativeModeTabs.TOOLS_AND_UTILITIES, CreativeModeTabs.COMBAT, CreativeModeTabs.FOOD_AND_DRINKS,
                      CreativeModeTabs.INGREDIENTS, CreativeModeTabs.SPAWN_EGGS);
      return operator.apply(builder).build();
    }, CreativeTabRegistryObject::new);
  }

  public static class CulinariumCreativeTab extends CreativeModeTab {
    public CulinariumCreativeTab(CreativeModeTab.Builder builder) {
      super(builder);
    }
  }
}
