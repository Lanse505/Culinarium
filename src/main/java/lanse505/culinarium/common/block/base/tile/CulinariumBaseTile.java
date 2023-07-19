package lanse505.culinarium.common.block.base.tile;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CulinariumBaseTile<T extends CulinariumBaseTile<T>> extends BlockEntity {

    private final CulinariumBaseTileBlock<T> block;

    public CulinariumBaseTile(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.block = base;
    }

    public void markForUpdate() {
        this.level.sendBlockUpdated(getBlockPos(), getLevel().getBlockState(getBlockPos()), getLevel().getBlockState(getBlockPos()), 3);
        setChanged();
    }

    public boolean isClient() {
        return this.level.isClientSide;
    }

    public boolean isServer() {
        return !isClient();
    }

    public CulinariumBaseTileBlock<T> getBasicTileBlock() {
        return block;
    }

}
