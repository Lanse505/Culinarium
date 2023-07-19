package lanse505.culinarium.common.block.base.tile;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class CulinariumActiveTile<T extends CulinariumActiveTile<T>> extends CulinariumBaseTile<T> implements ITickableBlockEntity<T>, EntityBlock {

    public CulinariumActiveTile(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(base, pType, pPos, pBlockState);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return CulinariumBlockRegistry.MILLSTONE_TILE.get().create(pPos, pState);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Nullable
    @Override
    public <R extends BlockEntity> BlockEntityTicker<R> getTicker(Level pLevel, BlockState pState, BlockEntityType<R> type) {
        return (level, pos, state, blockentity) -> {
            if (blockentity instanceof ITickableBlockEntity tickable) {
                if (level.isClientSide()) {
                    tickable.clientTick(level, pos, state, blockentity);
                } else {
                    tickable.serverTick(level, pos, state, blockentity);
                }
            }
        };
    }
}
