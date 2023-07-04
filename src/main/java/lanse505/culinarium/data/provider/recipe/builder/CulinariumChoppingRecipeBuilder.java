package lanse505.culinarium.data.provider.recipe.builder;

import com.google.gson.JsonObject;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.data.provider.recipe.base.CulinariumBaseRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CulinariumChoppingRecipeBuilder extends CulinariumBaseRecipeBuilder {

  private Ingredient input;
  private int chops;
  private ItemStack output;

  protected CulinariumChoppingRecipeBuilder(ResourceLocation id, ItemStack output) {
    super(id);
    this.output = output;
  }

  public static CulinariumChoppingRecipeBuilder chopping(ResourceLocation id, ItemStack output) {
    return new CulinariumChoppingRecipeBuilder(id, output);
  }

  public CulinariumChoppingRecipeBuilder input(Ingredient input) {
    this.input = input;
    return this;
  }

  public CulinariumChoppingRecipeBuilder chops(int chops) {
    this.chops = chops;
    return this;
  }

  @Override
  protected void ensureValid(ResourceLocation pRecipeId) {
    if (input == null) {
      throw new IllegalStateException("No input specified for recipe " + pRecipeId);
    }
    if (output == null) {
      throw new IllegalStateException("No output specified for recipe " + pRecipeId);
    }
  }

  @Override
  public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
    this.ensureValid(pRecipeId);
    pFinishedRecipeConsumer.accept(new Result(pRecipeId, input, chops, output));
  }

  public static class Result extends CulinariumBaseRecipeBuilder.Result {

    private final Ingredient input;
    private final int chops;
    private final ItemStack output;

    public Result(ResourceLocation id, Ingredient input, int chops, ItemStack output) {
      super(id);
      this.input = input;
      this.chops = chops;
      this.output = output;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
      pJson.addProperty("id", this.getId().toString());
      pJson.add("input", this.input.toJson());
      pJson.addProperty("chops", this.chops);
      JsonObject json = new JsonObject();
      json.addProperty("item", ForgeRegistries.ITEMS.getKey(output.getItem()).toString());
      json.addProperty("count", output.getCount());
      if (output.hasTag()) json.addProperty("nbt", output.getTag().toString());
      pJson.add("output", json);
    }

    @Override
    public RecipeSerializer<?> getType() {
      return CulinariumRecipeRegistry.CHOPPING.getSerializer();
    }
  }
}
