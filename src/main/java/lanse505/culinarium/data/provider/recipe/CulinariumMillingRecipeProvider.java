package lanse505.culinarium.data.provider.recipe;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.data.provider.recipe.builder.CulinariumMillingRecipeBuilder;
import lanse505.culinarium.register.CulinariumItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Consumer;

public class CulinariumMillingRecipeProvider extends RecipeProvider {



  public CulinariumMillingRecipeProvider(PackOutput pOutput) {
    super(pOutput);
  }

  @Override
  protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
    milling(pWriter,
            new ResourceLocation(Culinarium.MODID, "wheat_berries_to_flour"),
            Ingredient.of(CulinariumItemRegistry.WHEAT_BERRIES.get()),
            40,
            new ItemStack(CulinariumItemRegistry.FLOUR.get(), 2));
    milling(pWriter,
            new ResourceLocation(Culinarium.MODID, "wheat_husks_to_flour"),
            Ingredient.of(CulinariumItemRegistry.WHEAT_HUSKS.get()),
            new ItemStack(CulinariumItemRegistry.FLOUR.get(), 1));
  }

  protected static void milling(Consumer<FinishedRecipe> consumer, ResourceLocation id, Ingredient input, ItemStack... output) {
    CulinariumMillingRecipeBuilder.milling(id, output).input(input).save(consumer, id);
  }

  protected static void milling(Consumer<FinishedRecipe> consumer, ResourceLocation id, Ingredient input, int duration, ItemStack... output) {
    CulinariumMillingRecipeBuilder.milling(id, output).input(input).duration(duration).save(consumer, id);
  }
}
