package lanse505.culinarium.common.block.base.tile;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CulinariumBarrelTileBase<T extends CulinariumBarrelTileBase<T>> extends CulinariumActiveTile<T> {

    public static final int DEFAULT_TANK_CAPACITY = 8000;

    public CulinariumBarrelTileBase(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(base, pType, pPos, pBlockState);
    }

}
