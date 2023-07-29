package lanse505.culinarium.common.recipe.base;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public abstract class CulinariumBaseRecipe implements Recipe<Container> {

  private final ResourceLocation id;

  public CulinariumBaseRecipe(ResourceLocation id) {
    this.id = id;
  }

  public boolean isValid(ItemStack stack) {
    return false;
  }

  @Override
  public boolean matches(Container pContainer, Level pLevel) {
    return false;
  }

  @Override
  public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return false;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
    return ItemStack.EMPTY;
  }

  @Override
  public ResourceLocation getId() {
    return this.id;
  }

}
