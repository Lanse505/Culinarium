package lanse505.culinarium.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.BiPredicate;

public class ItemStackHandlerUtil {

  @Nonnull
  public static ItemStackHandler createItemHandler(int slots) {
    return new ItemStackHandler(slots);
  }

  @Nonnull
  public static ItemStackHandler createItemHandler(int slots, BiPredicate<Integer, ItemStack> insertionPredicate) {
    return new ItemStackHandler(slots) {
      @Override
      public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return insertionPredicate.test(slot, stack);
      }
    };
  }

}
