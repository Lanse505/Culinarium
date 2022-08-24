package lanse505.culinarium.block.base;

import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.block.tile.BasicTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class CulinariumBaseBlock<T extends BasicTile<T>> extends RotatableBlock<T> {

  public CulinariumBaseBlock(Properties properties, Class<T> tileClass) {
    super("", properties, tileClass);
  }

  @NotNull
  @Override
  public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext selectionContext) {
    return getShape(state, world, pos, selectionContext);
  }

}
