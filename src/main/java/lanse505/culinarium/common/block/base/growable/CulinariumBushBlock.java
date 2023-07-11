package lanse505.culinarium.common.block.base.growable;

import lanse505.culinarium.common.block.base.CulinariumBaseBlock;
import lanse505.culinarium.common.block.base.core.ICulinariumCoreBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraftforge.common.IPlantable;

public class CulinariumBushBlock extends BushBlock implements IPlantable, ICulinariumCoreBlock {

  public CulinariumBushBlock(Properties pProperties) {
    super(pProperties);
  }

}
