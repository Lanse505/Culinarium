package lanse505.culinarium.data.provider.lang;

import lanse505.culinarium.register.CulinariumBlockRegistry;
import lanse505.culinarium.register.CulinariumItemRegistry;
import net.minecraft.data.PackOutput;

public class CulinariumProviderEnUS extends BaseCulinariumLangRegistry {

  public CulinariumProviderEnUS(PackOutput output) {
    super(output, "en_us");
  }

  @Override
  protected void addTranslations() {
    addBlockTranslations();
    addItemTranslations();
    addEntityTranslations();
    addEffectTranslations();
    addMiscTranslations();
  }


  @Override
  public void addBlockTranslations() {
    add(CulinariumBlockRegistry.MILLSTONE.get(), "Millstone");
    add(CulinariumBlockRegistry.RYE.get(), "Rye");
  }

  @Override
  public void addItemTranslations() {
    add("item.culinarium.grindstone", "Millstone");
    add("item.culinarium.rye", "Rye");
    add(CulinariumItemRegistry.RYE_SEEDS.get(), "Rye Seeds");
    add(CulinariumItemRegistry.STRAW.get(), "Straw");
    add(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get(), "Mixed Wheat Berries");
    add(CulinariumItemRegistry.WHEAT_HUSKS.get(), "Wheat Husks");
    add(CulinariumItemRegistry.WHEAT_BERRIES.get(), "Wheat Berries");
    add(CulinariumItemRegistry.FLOUR.get(), "Flour");
  }

  @Override
  public void addEntityTranslations() {}

  @Override
  public void addEffectTranslations() {}

  @Override
  public void addMiscTranslations() {
    add("itemGroup.culinarium", "Culinarium");
  }
}
