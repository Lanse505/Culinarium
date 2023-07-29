package lanse505.culinarium.data.provider.lang;

import lanse505.culinarium.Culinarium;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public abstract class BaseCulinariumLangRegistry extends LanguageProvider {

  public BaseCulinariumLangRegistry(PackOutput output, String locale) {
    super(output, Culinarium.MODID, locale);
  }

  public abstract void addBlockTranslations();

  public abstract void addItemTranslations();

  public abstract void addEntityTranslations();

  public abstract void addEffectTranslations();

  public abstract void addMiscTranslations();

}
