package lanse505.culinarium.server.milling;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.register.CulinariumRecipeRegistry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class MillingRecipe implements Recipe<Container> {

  public static final RecipeSerializer<MillingRecipe> SERIALIZER = new MillingSerializer();

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
}
