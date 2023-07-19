package lanse505.culinarium.common.util;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class ItemStackHandlerBuilder {

    protected NonNullList<ItemStack> stacks;

    private int slots;
    private BiFunction<Integer, ItemStack, Integer> maxSlotSize = (slot, stack) -> stack.getMaxStackSize();

    private BiPredicate<Integer, ItemStack> insertPredicate = (slot, stack) -> true;
    private BiPredicate<Integer, ItemStack> extractPredicate = (slot, stack) -> true;


    protected ItemStackHandlerBuilder() {
        this.stacks = NonNullList.create();
    }

    public ItemStackHandlerBuilder(NonNullList<ItemStack> defaultStacks) {
        this.stacks = defaultStacks;
    }

    public static ItemStackHandlerBuilder builder() {
        return new ItemStackHandlerBuilder();
    }

    public static ItemStackHandlerBuilder builder(NonNullList<ItemStack> defaultedStacks) {
        return new ItemStackHandlerBuilder(defaultedStacks);
    }

    public ItemStackHandlerBuilder slots(int slots) {
        this.slots = slots;
        return this;
    }

    public ItemStackHandlerBuilder maxSlotSize(BiFunction<Integer, ItemStack, Integer> maxSlotSize) {
        this.maxSlotSize = maxSlotSize;
        return this;
    }

    public ItemStackHandlerBuilder insertCondition(BiPredicate<Integer, ItemStack> insertPredicate) {
        this.insertPredicate = insertPredicate;
        return this;
    }

    public ItemStackHandlerBuilder extractCondition(BiPredicate<Integer, ItemStack> extractPredicate) {
        this.extractPredicate = extractPredicate;
        return this;
    }

    public ItemStackHandler build() {
        return new ItemStackHandler(slots) {

            @Override
            public int getSlotLimit(int slot) {
                return maxSlotSize.apply(slot, ItemStack.EMPTY);
            }

            @Override
            protected int getStackLimit(int slot, @NotNull ItemStack stack) {
                return maxSlotSize.apply(slot, stack);
            }

            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return insertPredicate.test(slot, stack);
            }

            @Override
            public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
                return extractPredicate.test(slot, stacks.get(slot)) ? super.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
            }
        };
    }


}
