package lanse505.culinarium.common.block.impl.tile.barrel;

import lanse505.culinarium.common.block.base.tile.CulinariumBarrelTileBase;
import lanse505.culinarium.common.menu.BrewingBarrelMenu;
import lanse505.culinarium.common.recipe.BrewingRecipe;
import lanse505.culinarium.common.recipe.BrewingRecipe.RecipeIngredient;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.util.FluidTankBuilder;
import lanse505.culinarium.common.util.ItemStackHandlerBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
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
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;

public class BrewingBarrelTile extends CulinariumBarrelTileBase<BrewingBarrelTile> implements MenuProvider {

  private final LazyOptional<FluidTank> brewableCap;
  private final ItemStackHandler storage;
  private final LazyOptional<ItemStackHandler> storageCap;
  private final LazyOptional<FluidTank> brewedCap;
  // Capabilities
  private FluidTank brewable;
  private FluidTank brewed;
  // Variables
  private BrewingRecipe recipe = null;
  private int progress;

  public BrewingBarrelTile(BlockPos pPos, BlockState pBlockState) {
    super(CulinariumBlockRegistry.BREWING_BARREL.getBlock(),
            CulinariumBlockRegistry.BREWING_BARREL.getType(), pPos, pBlockState);
    this.brewable = FluidTankBuilder.builder(DEFAULT_TANK_CAPACITY).build();
    this.brewableCap = LazyOptional.of(() -> brewable);
    this.storage = ItemStackHandlerBuilder.builder().slots(4).maxSlotSize((slot, stack) -> 64).build();
    this.storageCap = LazyOptional.of(() -> storage);
    this.brewed = FluidTankBuilder.builder(DEFAULT_TANK_CAPACITY).build();
    this.brewedCap = LazyOptional.of(() -> brewed);
    this.brewable.fill(new FluidStack(Fluids.WATER, 4000), IFluidHandler.FluidAction.EXECUTE);
    this.brewed.fill(new FluidStack(Fluids.LAVA, 4000), IFluidHandler.FluidAction.EXECUTE);
  }

  @Override
  public void clientTick(Level level, BlockPos pos, BlockState state, BrewingBarrelTile blockEntity) {
    super.clientTick(level, pos, state, blockEntity);
    markForUpdate();
  }

  @Override
  public void serverTick(Level level, BlockPos pos, BlockState state, BrewingBarrelTile blockEntity) {
    super.serverTick(level, pos, state, blockEntity);
    if (!brewable.isEmpty() && hasItemInStorage()) {
      this.recipe = level.getRecipeManager().getRecipes().stream()
              .filter(BrewingRecipe.class::isInstance)
              .map(BrewingRecipe.class::cast)
              .filter(r -> r.isValid(brewable, storage, brewed))
              .findFirst().orElse(null);
    }
    if (recipe != null) {
      if (progress < 100) {
        progress++;
      } else {
        finishBrewing();
        progress = 0;
      }
    } else {
      progress = 0;
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
        RecipeIngredient ingredient = recipe.getRecipeIngredients().get(i);
        storage.getStackInSlot(i).shrink(ingredient.count());
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


  @Override
  public Component getDisplayName() {
    return Component.translatable("block.culinarium.brewing_barrel");
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new BrewingBarrelMenu(
            containerId, player, this.getBlockPos(),
            getBrewable(),
            getStorage(),
            getBrewed(),
            () -> this.recipe != null ? new ProgressView(this.progress, this.recipe.getTicks()) : ProgressView.NULL
    );
  }

  @Override
  public void writeTileNBT(CompoundTag tag) {
    tag.put("brewable", brewable.writeToNBT(new CompoundTag()));
    tag.put("storage", storage.serializeNBT());
    tag.put("brewed", brewed.writeToNBT(new CompoundTag()));
    tag.putInt("progress", progress);
    if (recipe != null) tag.putString("recipe", recipe.getId().toString());
  }

  @Override
  public void readTileNBT(CompoundTag tag) {
    this.brewable = brewable.readFromNBT(tag.getCompound("brewable"));
    this.storage.deserializeNBT(tag.getCompound("storage"));
    this.brewed = brewed.readFromNBT(tag.getCompound("brewed"));
    if (tag.contains("recipe") && level != null) {
      this.recipe = (BrewingRecipe) level.getRecipeManager().byKey(new ResourceLocation(tag.getString("recipe"))).orElse(null);
    }
  }

}
