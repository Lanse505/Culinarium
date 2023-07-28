package lanse505.culinarium.common.container;

import lanse505.culinarium.common.block.impl.tile.barrel.BrewingBarrelTile;
import lanse505.culinarium.common.container.base.BaseBarrelMenu;
import lanse505.culinarium.common.register.CulinariumMenuTypeRegistry;
import lanse505.culinarium.server.recipe.BrewingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BrewingBarrelMenu extends BaseBarrelMenu {

    private BrewingRecipe local;

    private FluidTank brewable;
    private ItemStackHandler storage;
    private FluidTank brewed;

    public BrewingBarrelMenu(int menuId, Player player, BlockPos pos, FluidTank brewable, ItemStackHandler storage, FluidTank brewed) {
        super(CulinariumMenuTypeRegistry.BREWING_BARREL_MENU.get(), menuId, player, pos);
        this.SLOT_COUNT = 4;
        this.SLOT_INPUT = 0;
        this.SLOT_INPUT_END = 3;
        layoutPlayerInventorySlots(player.getInventory(), 8, 121);
        this.brewable = brewable;
        this.storage = storage;
        addSlot(new SlotItemHandler(storage, 0, 62, 37));
        addSlot(new SlotItemHandler(storage, 1, 98, 37));
        addSlot(new SlotItemHandler(storage, 2, 62, 55));
        addSlot(new SlotItemHandler(storage, 3, 98, 55));
        this.brewed = brewed;
    }

    public IFluidTank getBrewable() {
        return brewable;
    }

    public ItemStackHandler getStorage() {
        return storage;
    }

    public IFluidTank getBrewed() {
        return brewed;
    }

    @SuppressWarnings("ConstantValue")
    public int getTicks() {
        if (player.level() != null) {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof BrewingBarrelTile brewing) {
                return brewing.getProgress();
            }
        }
        return 0;
    }

    @SuppressWarnings("ConstantValue")
    public int getMaxTicks() {
        if (shouldUpdateTicksForScreen()) {
            if (player.level() != null) {
                BlockEntity be = player.level().getBlockEntity(pos);
                if (be instanceof BrewingBarrelTile brewing && brewing.getRecipe() != null) {
                    local = brewing.getRecipe();
                    return local.getTicks();
                }
            }
        } else {
            return local.getTicks();
        }
        return 0;
    }

    @SuppressWarnings("ConstantValue")
    public boolean shouldUpdateTicksForScreen() {
        if (player.level() != null) {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof BrewingBarrelTile brewing && brewing.getRecipe() != null) {
                return local != brewing.getRecipe();
            }
        }
        return false;
    }
}
