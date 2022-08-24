package lanse505.culinarium.block;

import lanse505.culinarium.block.base.CulinariumBaseBlock;
import lanse505.culinarium.block.tile.GrindstoneTile;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class GrindstoneBlock extends CulinariumBaseBlock<GrindstoneTile> {

  private static final VoxelShape shape = Shapes.box(0.125D, 0.0D, 0.125D, 0.875D, 0.5625D, 0.875D);

  public GrindstoneBlock(Properties properties) {
    super(properties, GrindstoneTile.class);
  }

  @NotNull
  @Override
  public RotationType getRotationType() {
    return RotationType.FOUR_WAY;
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return shape;
  }

  @Override
  public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
    return true;
  }

  @Override
  public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
    return GrindstoneTile::new;
  }

  @Override
  public Supplier<Item> getItemBlockFactory() {
    return CulinariumBlockRegistry.GRINDSTONE_ITEM;
  }
}
