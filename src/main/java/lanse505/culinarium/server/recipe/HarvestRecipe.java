package lanse505.culinarium.server.recipe;

import com.google.gson.JsonObject;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.server.helper.CulinariumJsonHelper;
import lanse505.culinarium.server.helper.CulinariumNetworkHelper;
import lanse505.culinarium.server.recipe.base.CulinariumBaseRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public class HarvestRecipe extends CulinariumBaseRecipe {

  private final BlockState target;
  private final BlockState result;
  private final boolean isFuzzy;

  public HarvestRecipe(ResourceLocation id, BlockState target, BlockState result, boolean isFuzzy) {
    super(id);
    this.target = target;
    this.result = result;
    this.isFuzzy = isFuzzy;
  }

  public boolean isValid(BlockState state) {
    if (target.getBlock() != state.getBlock())
      return false;
    for (Property<?> prop : target.getProperties())
      if (state.hasProperty(prop) && target.getValue(prop) != state.getValue(prop))
        return false;
    if (!isFuzzy)
      for (Property<?> prop : state.getProperties())
        if (!target.hasProperty(prop))
          return false;
    return true;
  }

  public void harvest(Level level, BlockState target, BlockPos pos) {
    Block.dropResources(target, level, pos);
    level.setBlock(pos, result, 3);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CulinariumRecipeRegistry.HARVEST.serializer().get();
  }

  @Override
  public RecipeType<?> getType() {
    return CulinariumRecipeRegistry.HARVEST.type().get();
  }

  public static class HarvestSerializer implements RecipeSerializer<HarvestRecipe> {
    @Override
    public HarvestRecipe fromJson(ResourceLocation pRecipeId, JsonObject json) {
      BlockState target = CulinariumJsonHelper.deserializeBlockState(json.getAsJsonObject("target"));
      BlockState result = CulinariumJsonHelper.deserializeBlockState(json.getAsJsonObject("result"));
      boolean isFuzzy = json.get("isFuzzy").getAsBoolean();
      return new HarvestRecipe(pRecipeId, target, result, isFuzzy);
    }

    @Override
    public @Nullable HarvestRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      BlockState target = CulinariumNetworkHelper.readBlockState(pBuffer);
      BlockState result = CulinariumNetworkHelper.readBlockState(pBuffer);
      boolean isFuzzy = pBuffer.readBoolean();
      return new HarvestRecipe(pRecipeId, target, result, isFuzzy);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, HarvestRecipe pRecipe) {
      pBuffer.writeNbt(NbtUtils.writeBlockState(pRecipe.target));
      pBuffer.writeNbt(NbtUtils.writeBlockState(pRecipe.result));
      pBuffer.writeBoolean(pRecipe.isFuzzy);
    }
  }
}
