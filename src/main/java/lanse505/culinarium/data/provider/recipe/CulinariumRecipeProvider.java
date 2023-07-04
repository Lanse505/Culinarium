package lanse505.culinarium.data.provider.recipe;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.data.provider.recipe.builder.CulinariumChoppingRecipeBuilder;
import lanse505.culinarium.data.provider.recipe.builder.CulinariumMillingRecipeBuilder;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.function.Consumer;

public class CulinariumRecipeProvider extends RecipeProvider {

  public CulinariumRecipeProvider(PackOutput pOutput) {
    super(pOutput);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
    SpecialRecipeBuilder
            .special((RecipeSerializer<? extends CraftingRecipe>) CulinariumRecipeRegistry.DOUGH_MAKING.getSerializer())
            .save(consumer, new ResourceLocation(Culinarium.MODID, "dough_making").toString());
    milling(consumer,
            "wheat_berries_to_flour",
            Ingredient.of(CulinariumItemRegistry.WHEAT_BERRIES.get()),
            5,
            new ItemStack(CulinariumItemRegistry.FLOUR.get(), 2));
    milling(consumer,
            "wheat_husks_to_flour",
            Ingredient.of(CulinariumItemRegistry.WHEAT_HUSKS.get()),
            new ItemStack(CulinariumItemRegistry.FLOUR.get(), 1));
    chopping(consumer,
            "bread_to_slices",
            Ingredient.of(Items.BREAD),
            12,
            new ItemStack(CulinariumItemRegistry.SLICE_OF_BREAD.get(), 1));
    chopping(consumer,
            "carrot_to_chunks",
            Ingredient.of(Items.CARROT),
            new ItemStack(CulinariumItemRegistry.CARROT_CHUNKS.get(), 1));
  }

  protected static void milling(Consumer<FinishedRecipe> consumer, String id, Ingredient input, ItemStack... output) {
    ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "milling/" + id);
    CulinariumMillingRecipeBuilder.milling(rl, output).input(input).save(consumer, rl);
  }

  protected static void milling(Consumer<FinishedRecipe> consumer, String id, Ingredient input, int duration, ItemStack... output) {
    ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "milling/" + id);
    CulinariumMillingRecipeBuilder.milling(rl, output).input(input).duration(duration).save(consumer, rl);
  }

  protected static void chopping(Consumer<FinishedRecipe> consumer, String id, Ingredient input, ItemStack output) {
    ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "chopping/" + id);
    CulinariumChoppingRecipeBuilder.chopping(rl, output).input(input).save(consumer, rl);
  }

  protected static void chopping(Consumer<FinishedRecipe> consumer, String id, Ingredient input, int chops, ItemStack output) {
    ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "chopping/" + id);
    CulinariumChoppingRecipeBuilder.chopping(rl, output).input(input).chops(chops).save(consumer, rl);
  }
}
