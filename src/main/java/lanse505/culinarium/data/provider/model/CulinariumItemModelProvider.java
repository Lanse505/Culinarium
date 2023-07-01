package lanse505.culinarium.data.provider.model;

import lanse505.culinarium.Culinarium;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CulinariumItemModelProvider extends ItemModelProvider {

  public CulinariumItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, Culinarium.MODID, existingFileHelper);
  }

  //TODO: Update the texture references to our textures once we get them
  @Override
  protected void registerModels() {
    singleTexture("rye", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat"));
    singleTexture("rye_seeds", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat_seeds"));
    singleTexture("straw", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat"));
    singleTexture("mixed_wheat_berries", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat_seeds"));
    singleTexture("wheat_husks", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat_seeds"));
    singleTexture("wheat_berries", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat_seeds"));
    singleTexture("flour", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/bone_meal"));
  }
}
