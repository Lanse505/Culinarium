package lanse505.culinarium.common.container;

import lanse505.culinarium.common.block.impl.tile.barrel.BrewingBarrelTile;
import lanse505.culinarium.common.container.base.CulinariumBaseMenu;
import lanse505.culinarium.common.register.CulinariumMenuTypeRegistry;
import lanse505.culinarium.server.recipe.BrewingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.items.SlotItemHandler;

public class BrewingBarrelMenu extends CulinariumBaseMenu {

    private BrewingRecipe local;

    public BrewingBarrelMenu(int menuId, Player player, BlockPos pos) {
        super(CulinariumMenuTypeRegistry.BREWING_BARREL_MENU.get(), menuId, player, pos);
        this.SLOT_COUNT = 4;
        this.SLOT_INPUT = 0;
        this.SLOT_INPUT_END = 3;
        layoutPlayerInventorySlots(player.getInventory(), 8, 95);
        if (player.level().getBlockEntity(pos) instanceof BrewingBarrelTile brewing) {
            addSlot(new SlotItemHandler(brewing.getStorage(), 0, 62, 11));
            addSlot(new SlotItemHandler(brewing.getStorage(), 1, 98, 11));
            addSlot(new SlotItemHandler(brewing.getStorage(), 2, 62, 29));
            addSlot(new SlotItemHandler(brewing.getStorage(), 3, 98, 29));
        }
    }

    @SuppressWarnings("ConstantValue")
    public IFluidTank getBrewable() {
        if (player.level() != null) {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof BrewingBarrelTile brewing) {
                return brewing.getBrewable();
            }
        }
        return null;
    }

    @SuppressWarnings("ConstantValue")
    public IFluidTank getBrewed() {
        if (player.level() != null) {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof BrewingBarrelTile brewing) {
                return brewing.getBrewed();
            }
        }
        return null;
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
