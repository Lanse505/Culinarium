package lanse505.culinarium.server.milling;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class MillingSerializer implements RecipeSerializer<MillingRecipe> {
  @Override
  public MillingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
    final ResourceLocation id = new ResourceLocation(pSerializedRecipe.get("id").getAsString());
    final Ingredient input = Ingredient.fromJson(pSerializedRecipe.get("input"));
    JsonArray array = pSerializedRecipe.get("output").getAsJsonArray();
    final ItemStack[] output = new ItemStack[array.size()];
    for (int i = 0; i < array.size(); i++) {
      output[i] = CraftingHelper.getItemStack(array.get(i).getAsJsonObject(), true);
    }
    final int duration = pSerializedRecipe.get("duration").getAsInt();
    return new MillingRecipe(id, input, output, duration);
  }

  @Override
  public @Nullable MillingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
    final ResourceLocation id = pBuffer.readResourceLocation();
    final Ingredient input = Ingredient.fromNetwork(pBuffer);
    final int size = pBuffer.readVarInt();
    final ItemStack[] output = new ItemStack[size];
    for (int i = 0; i < size; i++) {
      output[i] = pBuffer.readItem();
    }
    final int duration = pBuffer.readVarInt();
    return new MillingRecipe(id, input, output, duration);
  }

  @Override
  public void toNetwork(FriendlyByteBuf pBuffer, MillingRecipe pRecipe) {
    pBuffer.writeResourceLocation(pRecipe.getId());
    pRecipe.getInput().toNetwork(pBuffer);
    pBuffer.writeVarInt(pRecipe.getOutput().length);
    for (ItemStack stack : pRecipe.getOutput()) {
      pBuffer.writeItem(stack);
    }
    pBuffer.writeVarInt(pRecipe.getDuration());
  }
}
