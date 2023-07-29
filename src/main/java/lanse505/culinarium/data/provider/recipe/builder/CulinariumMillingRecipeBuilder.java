package lanse505.culinarium.data.provider.recipe.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.data.provider.recipe.base.CulinariumBaseRecipeBuilder;
import lanse505.culinarium.common.recipe.MillingRecipe;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CulinariumMillingRecipeBuilder extends CulinariumBaseRecipeBuilder {

  private final ItemStack[] output;
  private Ingredient input;
  private int duration = 10;

  private CulinariumMillingRecipeBuilder(ResourceLocation id, ItemStack[] output) {
    super(id);
    this.output = output;
  }

  public static CulinariumMillingRecipeBuilder milling(ResourceLocation id, ItemStack... output) {
    return new CulinariumMillingRecipeBuilder(id, output);
  }

  public CulinariumMillingRecipeBuilder input(Ingredient input) {
    this.input = input;
    return this;
  }

  public CulinariumMillingRecipeBuilder duration(int duration) {
    this.duration = duration;
    return this;
  }

  public MillingRecipe build() {
    return new MillingRecipe(getId(), input, output, duration);
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

  @Override
  public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
    this.ensureValid(pRecipeId);
    pFinishedRecipeConsumer.accept(new CulinariumMillingRecipeBuilder.Result(pRecipeId, this.input, this.output, this.duration));
  }

  protected void ensureValid(ResourceLocation pRecipeId) {
    if (this.input == null) {
      throw new IllegalStateException("No input specified for recipe " + pRecipeId);
    }
    if (output == null) {
      throw new IllegalStateException("No output specified for recipe " + pRecipeId);
    }
  }

  public static class Result extends CulinariumBaseRecipeBuilder.Result {

    private final Ingredient input;
    private final ItemStack[] output;
    private final int duration;

    public Result(ResourceLocation id, Ingredient input, ItemStack[] output, int duration) {
      super(id);
      this.input = input;
      this.output = output;
      this.duration = duration;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
      pJson.addProperty("id", getId().toString());
      pJson.add("input", this.input.toJson());
      JsonArray array = new JsonArray();
      for (ItemStack stack : this.output) {
        JsonObject json = new JsonObject();
        json.addProperty("item", ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
        json.addProperty("count", stack.getCount());
        if (stack.hasTag()) json.addProperty("nbt", stack.getTag().toString());
        array.add(json);
      }
      pJson.add("output", array);
      pJson.addProperty("duration", this.duration);
    }

    @Override
    public RecipeSerializer<?> getType() {
      return CulinariumRecipeRegistry.MILLING.getSerializer();
    }
  }
}
