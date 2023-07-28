package lanse505.culinarium.common.menu.base;

import lanse505.culinarium.common.block.impl.block.barrel.BrewingBarrelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.Property;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

public class BaseBarrelMenu extends CulinariumBaseMenu {

    protected final Property<Boolean> sealed;

    protected BaseBarrelMenu(@Nullable MenuType<?> menuType, int containerId, Player player, BlockPos pos) {
        super(menuType, containerId, player, pos);
        this.sealed = this.propertyManager.addTrackedProperty(
                PropertyTypes.BOOLEAN.create(() -> {
                    BlockState state = player.level().getBlockState(pos);
                    if (state.getBlock() instanceof BrewingBarrelBlock) {
                        return state.getValue(BrewingBarrelBlock.SEALED);
                    }
                    return false;
                }, (value) -> {
                    BlockState state = player.level().getBlockState(pos);
                    if (state.getBlock() instanceof BrewingBarrelBlock) {
                        player.level().setBlock(pos, state.setValue(BrewingBarrelBlock.SEALED, value), Block.UPDATE_IMMEDIATE);
                    }
                })
        );
    }

    public void toggleSealed() {
        this.sealed.set(!isSealed());
    }

    public boolean isSealed() {
        return this.sealed.getOrElse(false);
    }

}
