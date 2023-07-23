package lanse505.culinarium.server.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import lanse505.culinarium.server.recipe.base.CulinariumBaseRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BrewingRecipe extends CulinariumBaseRecipe {

    private final FluidStack brewable;
    private final List<RecipeIngredient> recipeIngredients;
    private final FluidStack brewed;
    private final int ticks;

    public BrewingRecipe(ResourceLocation id, FluidStack brewable, FluidStack brewed, RecipeIngredient... recipeIngredients) {
        super(id);
        this.brewable = brewable;
        this.recipeIngredients = Arrays.asList(recipeIngredients);
        this.brewed = brewed;
        this.ticks = 100;
    }

    public BrewingRecipe(ResourceLocation id, FluidStack brewable, List<RecipeIngredient> recipeIngredients, FluidStack brewed, int ticks) {
        super(id);
        this.brewable = brewable;
        this.recipeIngredients = recipeIngredients;
        this.brewed = brewed;
        this.ticks = ticks;
    }

    public static RecipeIngredient getRecipeIngredientFromJson(JsonObject json) {
        Ingredient ingredient = CraftingHelper.getIngredient(json.getAsJsonObject("ingredient"), true);
        int count = json.get("count").getAsInt();
        return new RecipeIngredient(ingredient, count);
    }

    public static RecipeIngredient getRecipeIngredientFromNetwork(FriendlyByteBuf buffer) {
        Ingredient ingredient = CraftingHelper.getIngredient(buffer.readResourceLocation(), buffer);
        int count = buffer.readInt();
        return new RecipeIngredient(ingredient, count);
    }

    public boolean isValid(FluidTank brewable, ItemStackHandler storage, FluidTank brewed) {
        // Check if the brewable fluid matches the required fluid
        if (!brewable.getFluid().isFluidEqual(this.brewable)) return false;

        // Check if all the required ingredients are present in the storage
        for (int i = 0; i < this.recipeIngredients.size(); i++) {
            if (!this.recipeIngredients.get(i).test(storage.getStackInSlot(i))) {
                return false;
            }
        }

        // Check that any unused slots are empty (optional, as mentioned in the original method)
        for (int i = this.recipeIngredients.size(); i < 4; i++) {
            if (!storage.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }

        // Check if there is enough space in the brewed tank to hold the result
        return brewed.getFluid().getFluid() == this.brewed.getFluid() && brewed.getSpace() >= this.brewed.getAmount();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }

    public FluidStack getBrewable() {
        return brewable;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public FluidStack getBrewed() {
        return brewed;
    }

    public int getTicks() {
        return ticks;
    }

    public record RecipeIngredient(Ingredient ingredient, int count) {
        public boolean test(ItemStack stack) {
            return ingredient.test(stack) && stack.getCount() >= count;
        }

        public JsonElement serialize() {
            JsonObject ri = new JsonObject();
            ri.add("ingredient", ingredient.toJson());
            ri.addProperty("count", count);
            return ri;
        }
    }

    public static class BrewingSerializer implements RecipeSerializer<BrewingRecipe> {

        @Override
        public BrewingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            final FluidStack brewable = FluidStack.CODEC.parse(JsonOps.INSTANCE, serializedRecipe.get("brewable").getAsJsonObject()).get().orThrow();
            final List<RecipeIngredient> ri = new ArrayList<>();
            JsonArray riArray = serializedRecipe.getAsJsonArray("ingredients");
            for (int i = 0; i < riArray.size(); i++) {
                JsonObject riJson = riArray.get(i).getAsJsonObject();
                ri.add(getRecipeIngredientFromJson(riJson));
            }
            final FluidStack brewed = FluidStack.CODEC.parse(JsonOps.INSTANCE, serializedRecipe.get("brewed").getAsJsonObject()).get().orThrow();
            final int ticks = serializedRecipe.get("ticks").getAsInt();
            return new BrewingRecipe(recipeId, brewable, ri, brewed, ticks);
        }

        @Override
        public @Nullable BrewingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            final FluidStack brewable = buffer.readFluidStack();
            final List<RecipeIngredient> ingredients = new ArrayList<>();
            for (int i = 0; i < buffer.readInt(); i++) {
                ingredients.add(getRecipeIngredientFromNetwork(buffer));
            }
            final FluidStack brewed = buffer.readFluidStack();
            final int ticks = buffer.readInt();
            return new BrewingRecipe(recipeId, brewable, ingredients, brewed, ticks);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BrewingRecipe recipe) {
            buffer.writeFluidStack(recipe.brewable);
            List<RecipeIngredient> ril = recipe.recipeIngredients;
            buffer.writeInt(ril.size());
            for (RecipeIngredient ri : ril) {
                ri.ingredient.toNetwork(buffer);
                buffer.writeInt(ri.count);
            }
            buffer.writeFluidStack(recipe.brewed);
            buffer.writeInt(recipe.ticks);
        }
    }
}
