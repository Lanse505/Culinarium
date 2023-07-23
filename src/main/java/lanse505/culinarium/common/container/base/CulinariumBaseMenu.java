package lanse505.culinarium.common.container.base;

import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class CulinariumBaseMenu extends AbstractContainerMenu {

    protected final Player player;
    protected final BlockPos pos;

    protected int SLOT_INPUT;
    protected int SLOT_INPUT_END;
    protected int SLOT_OUTPUT;
    protected int SLOT_OUTPUT_END;
    protected int SLOT_COUNT;

    protected CulinariumBaseMenu(@Nullable MenuType<?> menuType, int containerId, Player player, BlockPos pos) {
        super(menuType, containerId);
        this.player = player;
        this.pos = pos;
    }

    public int addSlotRange(Container playerInventory, int index, int x, int y, int amount, int padX) {
        return internalAddSlotRange(playerInventory, index, x, y, amount, padX, Slot::new);
    }

    public int addCustomSlotRange(Container playerInventory, int index, int x, int y, int amount, int padX, SlotCreator creator) {
        return internalAddSlotRange(playerInventory, index, x, y, amount, padX, creator);
    }

    private int internalAddSlotRange(Container playerInventory, int index, int x, int y, int amount, int padX, SlotCreator creator) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(creator.create(playerInventory, index, x, y));
            x += padX;
            index++;
        }
        return index;
    }

    public int addSlotBox(Container playerInventory, int startingIndex, int x, int y, int boxWidth, int padX, int boxHeight, int padY) {
        return internalAddSlotBox(playerInventory, startingIndex, x, y, boxWidth, padX, boxHeight, padY, Slot::new);
    }

    public int addCustomSlotBox(Container playerInventory, int startingIndex, int x, int y, int boxWidth, int padX, int boxHeight, int padY, SlotCreator creator) {
        return internalAddSlotBox(playerInventory, startingIndex, x, y, boxWidth, padX, boxHeight, padY, creator);
    }

    private int internalAddSlotBox(Container playerInventory, int startingIndex, int x, int y, int boxWidth, int padX, int boxHeight, int padY, SlotCreator creator) {
        for (int j = 0 ; j < boxHeight ; j++) {
            startingIndex = internalAddSlotRange(playerInventory, startingIndex, x, y, boxWidth, padX, creator);
            y += padY;
        }
        return startingIndex;
    }

    public void layoutPlayerInventorySlots(Container playerInventory, int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            itemstack = stack.copy();
            if (index < SLOT_COUNT) {
                if (!this.moveItemStackTo(stack, SLOT_COUNT, Inventory.INVENTORY_SIZE + SLOT_COUNT, true)) {
                    return ItemStack.EMPTY;
                }
            }
            if (!this.moveItemStackTo(stack, SLOT_INPUT, SLOT_INPUT_END, false)) {
                if (index < 27 + SLOT_COUNT) {
                    if (!this.moveItemStackTo(stack, 27 + SLOT_COUNT, 36 + SLOT_COUNT, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < Inventory.INVENTORY_SIZE + SLOT_COUNT && !this.moveItemStackTo(stack, SLOT_COUNT, 27 + SLOT_COUNT, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(player.level(), this.pos), player, CulinariumBlockRegistry.BREWING_BARREL.getBlock());
    }

    @FunctionalInterface
    public interface SlotCreator {
        Slot create(Container container, int index, int x, int y);
    }
}
