package lanse505.culinarium.data.provider.recipe.base;

import com.google.gson.JsonObject;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

public abstract class CulinariumBaseRecipeBuilder implements RecipeBuilder {

  private final ResourceLocation id;

  protected CulinariumBaseRecipeBuilder(ResourceLocation id) {
    this.id = id;
  }

  @Override
  public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
    return null;
  }

  @Override
  public RecipeBuilder group(@Nullable String pGroupName) {
    return null;
  }

  @Override
  public Item getResult() {
    return null;
  }

  abstract protected void ensureValid(ResourceLocation pRecipeId);

  public ResourceLocation getId() {
    return id;
  }

  public abstract static class Result implements FinishedRecipe {

    private final ResourceLocation id;

    public Result(ResourceLocation id) {
      this.id = id;
    }

    @Override
    public ResourceLocation getId() {
      return id;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
      return new JsonObject();
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
      return new ResourceLocation("recipes/root");
    }
  }
}
