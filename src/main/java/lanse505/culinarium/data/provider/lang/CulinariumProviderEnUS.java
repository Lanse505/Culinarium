package lanse505.culinarium.data.provider.lang;

import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
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
        add(CulinariumItemRegistry.RYE.get(), "Rye");
        add(CulinariumItemRegistry.RYE_SEEDS.get(), "Rye Seeds");
        add(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get(), "Mixed Wheat Berries");
        add(CulinariumItemRegistry.STRAW.get(), "Straw");
        add(CulinariumItemRegistry.HAND_SIEVE.get(), "Hand Sieve");
        add(CulinariumItemRegistry.WHEAT_HUSKS.get(), "Wheat Husks");
        add(CulinariumItemRegistry.WHEAT_BERRIES.get(), "Wheat Berries");
        add(CulinariumItemRegistry.FLOUR.get(), "Flour");
        add(CulinariumItemRegistry.BOWL_OF_WATER.get(), "Bowl of Water");
        add(CulinariumItemRegistry.DOUGH.get(), "Dough");
        add(CulinariumItemRegistry.KNIFE.get(), "Chopping Knife");
        add(CulinariumItemRegistry.SLICE_OF_BREAD.get(), "Slice o' Bread");
        add(CulinariumItemRegistry.CARROT_CHUNKS.get(), "Carrot Chunks");
    }

    @Override
    public void addEntityTranslations() {
    }

    @Override
    public void addEffectTranslations() {
    }

    @Override
    public void addMiscTranslations() {
        add("itemGroup.culinarium", "Culinarium");
        add("itemGroup.culinarium_crops", "Culinarium: Crops");
        add("itemGroup.culinarium_processing", "Culinarium: Processing");
    }
}
