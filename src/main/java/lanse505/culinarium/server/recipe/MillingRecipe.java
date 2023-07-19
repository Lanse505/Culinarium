package lanse505.culinarium.server.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nullable;

public class MillingRecipe implements Recipe<Container> {

    private final ResourceLocation id;
    private final Ingredient input;
    private final ItemStack[] output;
    private final int duration;

    public MillingRecipe(ResourceLocation id, Ingredient input, ItemStack[] output) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.duration = 80;
    }

    public MillingRecipe(ResourceLocation id, Ingredient input, ItemStack[] output, int duration) {
        this.id = id;
        this.input = input;
        this.output = output;
        this.duration = duration;
    }

    public boolean matches(ItemStack input) {
        return this.input.test(input);
    }

    public Ingredient getInput() {
        return input;
    }

    public ItemStack[] getOutput() {
        return output;
    }

    public int getDuration() {
        return duration;
    }

    public boolean isValid(ItemStack stack) {
        return input.test(stack);
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CulinariumRecipeRegistry.MILLING.getSerializer();
    }

    @Override
    public RecipeType<?> getType() {
        return CulinariumRecipeRegistry.MILLING.getType();
    }

    public static class MillingSerializer implements RecipeSerializer<MillingRecipe> {
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
}
