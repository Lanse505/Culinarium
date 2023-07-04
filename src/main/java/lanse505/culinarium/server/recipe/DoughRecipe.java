package lanse505.culinarium.server.recipe;

import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class DoughRecipe extends CustomRecipe {

  public DoughRecipe(ResourceLocation pId, CraftingBookCategory pCategory) {
    super(pId, pCategory);
  }

  @Override
  public boolean matches(CraftingContainer pContainer, Level pLevel) {
    int i = 0;
    ItemStack itemstack = ItemStack.EMPTY;

    for(int j = 0; j < pContainer.getContainerSize(); ++j) {
      ItemStack itemstack1 = pContainer.getItem(j);
      if (!itemstack1.isEmpty()) {
        if (itemstack1.is(CulinariumItemRegistry.BOWL_OF_WATER.get())) {
          if (!itemstack.isEmpty()) {
            return false;
          }
          itemstack = itemstack1;
        } else if (!itemstack1.is(CulinariumItemRegistry.FLOUR.get())) {
            return false;
        }
        ++i;
      }
    }
    return !itemstack.isEmpty() && itemstack.hasTag() && i > 0;
  }

  @Override
  public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) {
    int i = 0;
    ItemStack water = ItemStack.EMPTY;

    for(int j = 0; j < pContainer.getContainerSize(); ++j) {
      ItemStack stack = pContainer.getItem(j);
      if (!stack.isEmpty()) {
        if (stack.is(CulinariumItemRegistry.BOWL_OF_WATER.get())) {
          if (!water.isEmpty()) {
            return ItemStack.EMPTY;
          }
          water = stack;
        } else if (!stack.is(CulinariumItemRegistry.FLOUR.get())) {
          return ItemStack.EMPTY;
        }
        ++i;
      }
    }

    if (!water.isEmpty() && i >= 1) {
      return new ItemStack(CulinariumItemRegistry.DOUGH.get(), i);
    } else {
      return ItemStack.EMPTY;
    }
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingContainer pContainer) {
    NonNullList<ItemStack> nonnulllist = NonNullList.withSize(pContainer.getContainerSize(), ItemStack.EMPTY);
    for(int i = 0; i < nonnulllist.size(); ++i) {
      ItemStack itemstack = pContainer.getItem(i);
      if (itemstack.is(CulinariumItemRegistry.BOWL_OF_WATER.get())) {
        nonnulllist.set(i, new ItemStack(Items.BOWL, 1));
        break;
      }
    }
    return nonnulllist;
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return pWidth >= 3 && pHeight >= 3;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return CulinariumRecipeRegistry.DOUGH_MAKING.getSerializer();
  }
}
