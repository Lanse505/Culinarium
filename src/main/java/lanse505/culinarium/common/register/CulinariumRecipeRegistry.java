package lanse505.culinarium.common.register;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.server.recipe.ChoppingRecipe;
import lanse505.culinarium.server.recipe.MillingRecipe;
import lanse505.culinarium.server.recipe.DoughRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CulinariumRecipeRegistry {
  public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, Culinarium.MODID);
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Culinarium.MODID);

  // Recipes
  public static final RecipeHolder MILLING = registerRecipe("milling", new MillingRecipe.MillingSerializer());
  public static final RecipeHolder CHOPPING = registerRecipe("chopping", new ChoppingRecipe.ChoppingSerializer());

  // Special Recipes
  public static final RecipeHolder DOUGH_MAKING = registerRecipe("dough_making", new SimpleCraftingRecipeSerializer<>(DoughRecipe::new));

  public static RecipeHolder registerRecipe(String typeName, RecipeSerializer<?> recipeSerializer) {
    final RegistryObject<RecipeType<?>> type = RECIPE_TYPES.register(typeName, () -> RecipeType.simple(new ResourceLocation(Culinarium.MODID, typeName)));
    final RegistryObject<RecipeSerializer<?>> serializer = RECIPE_SERIALIZERS.register(typeName, () -> recipeSerializer);
    return new RecipeHolder(type, serializer);
  }

  public static void register(IEventBus bus) {
    RECIPE_TYPES.register(bus);
    RECIPE_SERIALIZERS.register(bus);
  }

  public static class RecipeHolder {

    private final RegistryObject<RecipeType<?>> type;
    private final RegistryObject<RecipeSerializer<?>> serializer;

    private RecipeHolder(RegistryObject<RecipeType<?>> type, RegistryObject<RecipeSerializer<?>> serializer) {
      this.type = type;
      this.serializer = serializer;
    }

    public RecipeType<?> getType() {
      return type.get();
    }

    public RecipeSerializer<?> getSerializer() {
      return serializer.get();
    }
  }

}
