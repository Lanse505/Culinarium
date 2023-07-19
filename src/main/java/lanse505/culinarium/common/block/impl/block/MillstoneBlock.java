package lanse505.culinarium.common.block.impl.block;

import lanse505.culinarium.common.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.common.block.impl.tile.MillstoneTile;
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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class MillstoneBlock extends CulinariumBaseTileBlock<MillstoneTile> {

    private static final VoxelShape shape = Shapes.box(0.125D, 0.0D, 0.125D, 0.875D, 0.5625D, 0.875D);

    public MillstoneBlock(BlockBehaviour.Properties properties) {
        super(properties);
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
        return MillstoneTile::new;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(level.getBlockEntity(pos) instanceof MillstoneTile millstone)) return InteractionResult.PASS;
        ItemStackHandler inventory = millstone.getInventory();

        if (!player.getMainHandItem().isEmpty() && inventory.getStackInSlot(0).isEmpty()) {
            if (inventory.insertItem(0, player.getMainHandItem(), true) != player.getMainHandItem()) {
                inventory.insertItem(0, player.getMainHandItem(), false); // Insert into the inventory
                player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);     // Empty their Main-Hand.
                millstone.markForUpdate();                                           // Mark for update
                return InteractionResult.SUCCESS;
            }
        }

        boolean isGrinding = millstone.isMilling;

        if (!inventory.getStackInSlot(0).isEmpty() && player.getMainHandItem().isEmpty() && player.isCrouching()) {
            if (inventory.extractItem(0, Integer.MAX_VALUE, true) != ItemStack.EMPTY) {
                ItemStack extracted = inventory.extractItem(0, Integer.MAX_VALUE, false);
                player.getInventory().placeItemBackInInventory(extracted, true);
                if (isGrinding) millstone.isMilling = false;
                millstone.markForUpdate(); // Mark for update
                return InteractionResult.SUCCESS;
            }
        }

        BlockPos hitPos = hit.getBlockPos();

        if (!inventory.getStackInSlot(0).isEmpty() && player.getMainHandItem().isEmpty()) {
            if (level.isClientSide()) {
                millstone.spawnMillstoneParticles(player, hand, hit.getDirection(), hitPos); // If it's client-side then spawnGrindParticles.
                return InteractionResult.SUCCESS;
            } else if (millstone.activeRecipe != null) {
                millstone.duration++; // Increase the duration
                millstone.isMilling = true; // If the recipe isn't null and "isGrinding" is false. Set "isGrinding" to true.
                millstone.markForUpdate(); // Mark for update
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }

        return InteractionResult.PASS;
    }

    @Override
    public NonNullList<ItemStack> getDynamicDrops(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        BlockEntity tileentity = worldIn.getBlockEntity(pos);
        if (tileentity instanceof MillstoneTile millstone) {
            for (int i = 0; i < millstone.getInventory().getSlots(); i++) {
                stacks.add(millstone.getInventory().getStackInSlot(i));
            }
        }
        return stacks;
    }
}
