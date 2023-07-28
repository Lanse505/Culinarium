package lanse505.culinarium.common.block.impl.block.barrel;

import lanse505.culinarium.common.block.base.barrel.CulinariumBarrelBase;
import lanse505.culinarium.common.block.impl.tile.barrel.BrewingBarrelTile;
import lanse505.culinarium.common.container.BrewingBarrelMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class BrewingBarrelBlock extends CulinariumBarrelBase<BrewingBarrelTile> {

    public BrewingBarrelBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntityType.BlockEntitySupplier<?> getTileEntityFactory() {
        return BrewingBarrelTile::new;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BrewingBarrelTile brewing = getTypedBE(level, pos);
        if (stack.getTag() != null) {
            CompoundTag tag = stack.getTag();
            if (tag.contains("brewable")) brewing.getBrewable().readFromNBT(tag.getCompound("Brewable"));
            if (tag.contains("BlockEntityTag")) brewing.getStorage().deserializeNBT(tag.getCompound("Storage"));
            if (tag.contains("brewed")) brewing.getBrewed().readFromNBT(tag.getCompound("Brewed"));
        }
    }

    @Override
    public void getAdditionalNBTForDrop(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool, CompoundTag tag) {
        BrewingBarrelTile brewing = getTypedBE(level, pos);
        if (state.getValue(SEALED)) {
            // Brewable
            CompoundTag brewable = new CompoundTag();
            brewing.getBrewable().writeToNBT(brewable);
            tag.put("Brewable", brewable);

            // Storage
            tag.put("Storage", brewing.getStorage().serializeNBT());

            // Brewed
            CompoundTag brewed = new CompoundTag();
            brewing.getBrewed().writeToNBT(brewed);
            tag.put("Brewed", brewed);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BrewingBarrelTile brewing) {
                NetworkHooks.openScreen((ServerPlayer) player, brewing, be.getBlockPos());
                return InteractionResult.PASS;
            } else {
                throw new IllegalStateException("Our named container provider is missing!");
            }
        }
        return InteractionResult.PASS;
    }

}
