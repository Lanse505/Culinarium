package lanse505.culinarium.common.block.impl.block;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.common.block.impl.tile.ChoppingBoardTile;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ChoppingBoardBlock extends CulinariumBaseTileBlock<ChoppingBoardTile> {

    private static final VoxelShape NS_shape = Shapes.box(0.0625D, 0.0D, 0.1875D, 0.9375D, 0.1875D, 0.8125D);
    private static final VoxelShape WE_shape = Shapes.box(0.1875D, 0.0D, 0.0625D, 0.8125D, 0.1875D, 0.9375D);

    public ChoppingBoardBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public RotationType getRotationType() {
        return RotationType.FOUR_WAY;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING_HORIZONTAL)) {
            case NORTH, SOUTH -> NS_shape;
            case EAST, WEST -> WE_shape;
            default -> Shapes.block();
        };
    }

    @Override
    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return ChoppingBoardTile::new;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof ChoppingBoardTile choppingBoard) {
            ItemStackHandler inventory = choppingBoard.getInventory();
            if (!player.getMainHandItem().isEmpty() && inventory.getStackInSlot(0).isEmpty()) {
                if (inventory.insertItem(0, player.getMainHandItem(), true) != player.getMainHandItem()) {
                    inventory.insertItem(0, player.getMainHandItem(), false); // Insert into the inventory
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);     // Empty their Main-Hand.
                    choppingBoard.markForUpdate();                                           // Mark for update
                    return InteractionResult.SUCCESS;
                }
            }
            if (!inventory.getStackInSlot(0).isEmpty() && player.getMainHandItem().isEmpty() && player.isCrouching()) {
                if (inventory.extractItem(0, Integer.MAX_VALUE, true) != ItemStack.EMPTY) {
                    ItemStack extracted = inventory.extractItem(0, Integer.MAX_VALUE, false);
                    player.getInventory().placeItemBackInInventory(extracted, true);
                    choppingBoard.markForUpdate(); // Mark for update
                    return InteractionResult.SUCCESS;
                }
            }
            if (player.getMainHandItem().is(CulinariumItemRegistry.KNIFE.get()) && !inventory.getStackInSlot(0).isEmpty()) {
                return choppingBoard.performChop(player, hand);
            }
        }
        return super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof ChoppingBoardTile choppingBoard) {
            for (int i = 0; i < choppingBoard.getInventory().getSlots(); i++) {
                stacks.add(choppingBoard.getInventory().getStackInSlot(i));
            }
        }
        return stacks;
    }
}
