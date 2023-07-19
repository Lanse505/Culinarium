package lanse505.culinarium.server.recipe;

import com.google.gson.JsonObject;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.server.recipe.base.CulinariumBaseRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class ChoppingRecipe extends CulinariumBaseRecipe {

    public Ingredient input;
    public int chops = 1;
    public ItemStack output;

    public ChoppingRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        super(id);
        this.input = input;
        this.output = output;
    }

    public ChoppingRecipe(ResourceLocation id, Ingredient input, int chops, ItemStack output) {
        super(id);
        this.input = input;
        this.chops = chops;
        this.output = output;
    }

    @Override
    public boolean isValid(ItemStack stack) {
        return input.test(stack);
    }

    public Ingredient getInput() {
        return input;
    }

    public int getChops() {
        return chops;
    }

    public ItemStack getOutput() {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CulinariumRecipeRegistry.CHOPPING.getSerializer();
    }

    @Override
    public RecipeType<?> getType() {
        return CulinariumRecipeRegistry.CHOPPING.getType();
    }

    public static class ChoppingSerializer implements RecipeSerializer<ChoppingRecipe> {

        @Override
        public ChoppingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            final ResourceLocation id = new ResourceLocation(pSerializedRecipe.get("id").getAsString());
            final Ingredient input = Ingredient.fromJson(pSerializedRecipe.get("input"));
            final ItemStack output = CraftingHelper.getItemStack(pSerializedRecipe.get("output").getAsJsonObject(), true);
            if (pSerializedRecipe.has("chops")) {
                final int chops = pSerializedRecipe.get("chops").getAsInt();
                return new ChoppingRecipe(id, input, chops, output);
            }
            return new ChoppingRecipe(id, input, output);
        }

        @Override
        public @Nullable ChoppingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            final Ingredient input = Ingredient.fromNetwork(pBuffer);
            final ItemStack output = pBuffer.readItem();
            if (pBuffer.readBoolean()) {
                final int chops = pBuffer.readInt();
                return new ChoppingRecipe(pRecipeId, input, chops, output);
            }
            return new ChoppingRecipe(pRecipeId, input, output);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ChoppingRecipe pRecipe) {
            pRecipe.getInput().toNetwork(pBuffer);
            pBuffer.writeItem(pRecipe.getOutput());
            if (pRecipe.getChops() > 1) {
                pBuffer.writeBoolean(true);
                pBuffer.writeInt(pRecipe.getChops());
            } else {
                pBuffer.writeBoolean(false);
            }
        }
    }
}
