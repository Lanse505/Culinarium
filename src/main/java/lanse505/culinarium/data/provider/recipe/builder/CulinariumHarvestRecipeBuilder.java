package lanse505.culinarium.data.provider.recipe.builder;

import com.google.gson.JsonObject;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.data.provider.recipe.base.CulinariumBaseRecipeBuilder;
import lanse505.culinarium.common.util.CulinariumJsonHelper;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class CulinariumHarvestRecipeBuilder extends CulinariumBaseRecipeBuilder {

  private BlockState target;
  private BlockState result;
  private boolean isFuzzy;

  protected CulinariumHarvestRecipeBuilder(ResourceLocation id, BlockState result) {
    super(id);
    this.result = result;
  }

  public static CulinariumHarvestRecipeBuilder harvest(ResourceLocation id, BlockState result) {
    return new CulinariumHarvestRecipeBuilder(id, result);
  }

  public CulinariumHarvestRecipeBuilder target(BlockState target) {
    this.target = target;
    return this;
  }

  public CulinariumHarvestRecipeBuilder fuzzy() {
    this.isFuzzy = true;
    return this;
  }

  @Override
  protected void ensureValid(ResourceLocation pRecipeId) {
    if (this.target == null)
      throw new IllegalStateException("No target specified for recipe " + pRecipeId);
    if (this.result == null)
      throw new IllegalStateException("No result specified for recipe " + pRecipeId);
  }

  @Override
  public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
    this.ensureValid(pRecipeId);
    pFinishedRecipeConsumer.accept(new Result(pRecipeId, target, result, isFuzzy));
  }

  public static class Result extends CulinariumBaseRecipeBuilder.Result {

    private final BlockState target;
    private final BlockState result;
    private final boolean isFuzzy;

    public Result(ResourceLocation id, BlockState target, BlockState result, boolean isFuzzy) {
      super(id);
      this.target = target;
      this.result = result;
      this.isFuzzy = isFuzzy;
    }

    @Override
    public void serializeRecipeData(JsonObject pJson) {
      pJson.add("target", CulinariumJsonHelper.serializeBlockState(target));
      pJson.add("result", CulinariumJsonHelper.serializeBlockState(result));
      pJson.addProperty("isFuzzy", isFuzzy);
    }

    @Override
    public RecipeSerializer<?> getType() {
      return CulinariumRecipeRegistry.HARVEST.getSerializer();
    }
  }
}
