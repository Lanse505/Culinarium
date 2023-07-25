package lanse505.culinarium.common.block.impl.tile.barrel;

import lanse505.culinarium.common.block.base.tile.CulinariumBarrelTileBase;
import lanse505.culinarium.common.block.impl.block.barrel.BrewingBarrelBlock;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.util.FluidTankBuilder;
import lanse505.culinarium.common.util.ItemStackHandlerBuilder;
import lanse505.culinarium.server.recipe.BrewingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrewingBarrelTile extends CulinariumBarrelTileBase<BrewingBarrelTile> {

    // Capabilities
    private final FluidTank brewable;
    private final LazyOptional<FluidTank> brewableCap;
    private final ItemStackHandler storage;
    private final LazyOptional<ItemStackHandler> storageCap;
    private final FluidTank brewed;
    private final LazyOptional<FluidTank> brewedCap;

    // Variables
    private BrewingRecipe recipe = null; // TODO: Implement custom recipe here
    private int progress;
    private boolean isSealed;

    public BrewingBarrelTile(BlockPos pPos, BlockState pBlockState) {
        super(CulinariumBlockRegistry.BREWING_BARREL.getBlock(),
                CulinariumBlockRegistry.BREWING_BARREL.getType(), pPos, pBlockState);
        this.brewable = FluidTankBuilder.builder(8000).build();
        this.brewableCap = LazyOptional.of(() -> brewable);
        this.storage = ItemStackHandlerBuilder.builder().slots(4).maxSlotSize((slot, stack) -> 64).build();
        this.storageCap = LazyOptional.of(() -> storage);
        this.brewed = FluidTankBuilder.builder(8000).build();
        this.brewedCap = LazyOptional.of(() -> brewed);
        this.brewable.fill(new FluidStack(Fluids.WATER, 4000), IFluidHandler.FluidAction.EXECUTE);
        this.brewed.fill(new FluidStack(Fluids.LAVA, 4000), IFluidHandler.FluidAction.EXECUTE);
        this.isSealed = pBlockState.getValue(BrewingBarrelBlock.SEALED);
    }

    @Override
    public void clientTick(Level level, BlockPos pos, BlockState state, BrewingBarrelTile blockEntity) {
        super.clientTick(level, pos, state, blockEntity);
    }

    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state, BrewingBarrelTile blockEntity) {
        super.serverTick(level, pos, state, blockEntity);
        if (!brewable.isEmpty() && hasItemInStorage()) {
            recipe = level.getRecipeManager().getRecipes().stream()
                    .filter(BrewingRecipe.class::isInstance)
                    .map(BrewingRecipe.class::cast)
                    .filter(r -> r.isValid(brewable, storage, brewed))
                    .findFirst().orElse(null);
        }
    }

    private boolean hasItemInStorage() {
        boolean hasItems = false;
        for (int i = 0; i < storage.getSlots(); i++)
            if (!storage.getStackInSlot(i).isEmpty()) hasItems = true;
        return hasItems;
    }

    private void finishBrewing() {
        if (recipe != null) {
            brewable.drain(recipe.getBrewable(), IFluidHandler.FluidAction.EXECUTE);
            for (int i = 0; i < storage.getSlots(); i++) {
                storage.getStackInSlot(i).shrink(1);
            }
            brewed.fill(recipe.getBrewed(), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public FluidTank getBrewable() {
        return brewable;
    }

    public ItemStackHandler getStorage() {
        return storage;
    }

    public FluidTank getBrewed() {
        return brewed;
    }

    public BrewingRecipe getRecipe() {
        return recipe;
    }

    public int getProgress() {
        return progress;
    }

    public boolean isSealed() {
        return isSealed;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return storageCap.cast();
        }
        return super.getCapability(cap);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            if (side == Direction.UP) return brewableCap.cast();
            if (side == Direction.DOWN) return brewedCap.cast();
        }
        return super.getCapability(cap, side);
    }
}
