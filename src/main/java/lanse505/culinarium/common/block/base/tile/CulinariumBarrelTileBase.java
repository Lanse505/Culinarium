package lanse505.culinarium.common.block.base.tile;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.common.block.impl.block.barrel.BrewingBarrelBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class CulinariumBarrelTileBase<T extends CulinariumBarrelTileBase<T>> extends CulinariumActiveTile<T> {

  public static final int DEFAULT_TANK_CAPACITY = 8000;

  public CulinariumBarrelTileBase(CulinariumBaseTileBlock<T> base, BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
    super(base, pType, pPos, pBlockState);
  }

  public void toggleSeal(BlockPos pos, boolean newSealState) {
    if (this.getLevel() != null && this.getLevel() instanceof ServerLevel) {
      Level level = this.getLevel();
      level.setBlock(pos, level.getBlockState(pos).setValue(BrewingBarrelBlock.SEALED, newSealState), Block.UPDATE_IMMEDIATE);
    }
  }

}
