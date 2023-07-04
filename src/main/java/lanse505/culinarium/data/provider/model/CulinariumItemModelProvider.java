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
    singleTexture("mixed_wheat_berries", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat_seeds"));
    singleTexture("straw", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat"));
    singleTexture("hand_sieve", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/bamboo_door"));
    singleTexture("wheat_husks", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat_seeds"));
    singleTexture("wheat_berries", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/wheat_seeds"));
    singleTexture("flour", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/bone_meal"));
    singleTexture("bowl_of_water", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/bowl"));
    singleTexture("dough", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/bread"));
    singleTexture("knife", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/iron_sword"));
    singleTexture("slice_of_bread", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/bread"));
    singleTexture("toast", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/bread"));
    singleTexture("carrot_chunks", mcLoc("minecraft:item/generated"), "layer0", new ResourceLocation("minecraft", "item/carrot"));
  }
}
