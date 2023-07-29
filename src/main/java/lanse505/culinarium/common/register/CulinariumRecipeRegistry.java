package lanse505.culinarium.common.register;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.server.recipe.ChoppingRecipe;
import lanse505.culinarium.server.recipe.DoughRecipe;
import lanse505.culinarium.server.recipe.HarvestRecipe;
import lanse505.culinarium.server.recipe.MillingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
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
  public static final RecipeHolder<MillingRecipe> MILLING = registerRecipe("milling", new MillingRecipe.MillingSerializer());
  public static final RecipeHolder<ChoppingRecipe> CHOPPING = registerRecipe("chopping", new ChoppingRecipe.ChoppingSerializer());

  // Harvest
  public static final RecipeHolder<HarvestRecipe> HARVEST = registerRecipe("harvest", new HarvestRecipe.HarvestSerializer());

  // Special Recipes
  public static final RecipeHolder<DoughRecipe> DOUGH_MAKING = registerRecipe("dough_making", new SimpleCraftingRecipeSerializer<>(DoughRecipe::new));

  public static <T extends Recipe<?>> RecipeHolder<T> registerRecipe(String typeName, RecipeSerializer<T> recipeSerializer) {
    final RegistryObject<RecipeType<T>> type = RECIPE_TYPES.register(typeName, () -> RecipeType.simple(new ResourceLocation(Culinarium.MODID, typeName)));
    final RegistryObject<RecipeSerializer<T>> serializer = RECIPE_SERIALIZERS.register(typeName, () -> recipeSerializer);
    return new RecipeHolder<>(type, serializer);
  }

  public static void register(IEventBus bus) {
    RECIPE_TYPES.register(bus);
    RECIPE_SERIALIZERS.register(bus);
  }

  public record RecipeHolder<T extends Recipe<?>>(RegistryObject<RecipeType<T>> type,
                                                  RegistryObject<RecipeSerializer<T>> serializer) {
    public RecipeType<T> getType() {
      return type.get();
    }

    public RecipeSerializer<T> getSerializer() {
      return serializer.get();
    }
  }

}
