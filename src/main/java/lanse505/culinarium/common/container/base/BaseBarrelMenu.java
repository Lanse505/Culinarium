package lanse505.culinarium.common.container.base;

import lanse505.culinarium.common.block.impl.tile.barrel.BrewingBarrelTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public class BaseBarrelMenu extends CulinariumBaseMenu {

    protected BaseBarrelMenu(@Nullable MenuType<?> menuType, int containerId, Player player, BlockPos pos) {
        super(menuType, containerId, player, pos);
    }

    public boolean isSealed() {
        if (player.level() != null) {
            BlockEntity be = player.level().getBlockEntity(pos);
            if (be instanceof BrewingBarrelTile brewing) {
                return brewing.isSealed();
            }
        }
        return false;
    }

}
