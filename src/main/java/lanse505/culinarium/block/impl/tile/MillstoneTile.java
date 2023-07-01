package lanse505.culinarium.block.impl.tile;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.block.base.CulinariumBaseTileBlock;
import lanse505.culinarium.block.base.tile.CulinariumActiveTile;
import lanse505.culinarium.block.menu.handler.AdaptedItemHandler;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import lanse505.culinarium.server.milling.MillingRecipe;
import lanse505.culinarium.util.CulinariumTags;
import lanse505.culinarium.util.ItemStackHandlerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class MillstoneTile extends CulinariumActiveTile<MillstoneTile> {

  private final ItemStackHandler inventory = ItemStackHandlerUtil.createItemHandler(1, (slot, stack) -> stack.is(CulinariumTags.CulinariumItemTags.MILLABLE));
  private final LazyOptional<IItemHandler> inventoryHandler = LazyOptional.of(() -> new AdaptedItemHandler(inventory));


  public boolean isMilling;

  public static final int DEFAULT_DURATION = 80;

  public int duration;

  public MillingRecipe activeRecipe = null;

  @SuppressWarnings({"unchecked", "rawtypes"})
  public MillstoneTile(BlockPos pos, BlockState state) {
    super((CulinariumBaseTileBlock) CulinariumBlockRegistry.MILLSTONE.get(), CulinariumBlockRegistry.MILLSTONE_TILE.get(), pos, state);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    inventoryHandler.invalidate();
  }

  public ItemStackHandler getInventory() {
    return inventory;
  }

  @Override
  protected void saveAdditional(CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    if (activeRecipe != null) compoundTag.putString("activeRecipe", activeRecipe.getId().toString());
    compoundTag.put("inventory", inventory.serializeNBT());
    compoundTag.putBoolean("isGrinding", isMilling);
    compoundTag.put("duration", IntTag.valueOf(duration));
  }

  @Override
  public void load(CompoundTag compound) {
    super.load(compound);
    if (compound.contains("activeRecipe") && level != null) {
      activeRecipe = (MillingRecipe) level.getRecipeManager().byKey(new ResourceLocation(compound.getString("activeRecipe"))).orElse(null);
    }
    if (compound.contains("inventory")) inventory.deserializeNBT(compound.getCompound("inventory"));
    if (compound.contains("isGrinding")) isMilling = compound.getBoolean("isGrinding");
    if (compound.contains("duration")) duration = compound.getInt("duration");
  }

  @NotNull
  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    if (activeRecipe != null) tag.putString("activeRecipe", activeRecipe.getId().toString());
    tag.put("inventory", inventory.serializeNBT());
    tag.putBoolean("isGrinding", isMilling);
    tag.put("duration", IntTag.valueOf(duration));
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    super.handleUpdateTag(tag);
    if (tag.contains("activeRecipe") && level != null) {
      activeRecipe = (MillingRecipe) level.getRecipeManager().byKey(new ResourceLocation(tag.getString("activeRecipe"))).orElse(null);
    }
    if (tag.contains("inventory")) inventory.deserializeNBT(tag.getCompound("inventory"));
    if (tag.contains("isGrinding")) isMilling = tag.getBoolean("isGrinding");
    if (tag.contains("duration")) duration = tag.getInt("duration");
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryHandler.cast();
    }
    return super.getCapability(cap);
  }

  //TODO: Reimplement the server tick
  @Override
  public void serverTick(Level level, BlockPos pos, BlockState state, MillstoneTile blockEntity) {
    if (level.getGameTime() % 60 == 0) {
      Culinarium.LOGGER.info("Is Milling: " + isMilling);
      Culinarium.LOGGER.info("Active Recipe: " + (activeRecipe != null ? activeRecipe.getId().toString() : "null"));
      Culinarium.LOGGER.info("Duration: " + duration);
    }
    if (!inventory.getStackInSlot(0).isEmpty() && activeRecipe == null) {
      Optional<MillingRecipe> found = level.getRecipeManager().getRecipes().stream().filter(recipe -> recipe instanceof MillingRecipe)
              .filter(recipe -> ((MillingRecipe) recipe).getInput().test(inventory.getStackInSlot(0)))
              .map(recipe -> (MillingRecipe) recipe).findFirst();
      found.ifPresent(recipe -> activeRecipe = recipe);
      markForUpdate();
    }
    if (activeRecipe != null && inventory.getStackInSlot(0).isEmpty()) {
      isMilling = false;
      activeRecipe = null;
      duration = 0;
      markForUpdate();
    }
    if (isMilling) {
      isMilling = false;
      markForUpdate();
    }
    if (duration == MillstoneTile.DEFAULT_DURATION) {
      finishMilling();
    }
  }

  @Override
  public void clientTick(Level level, BlockPos pos, BlockState state, MillstoneTile blockEntity) {
    if (level.getGameTime() % 60 == 0) {
      Culinarium.LOGGER.info("Is Milling: " + isMilling);
      Culinarium.LOGGER.info("Active Recipe: " + (activeRecipe != null ? activeRecipe.getId().toString() : "null"));
      Culinarium.LOGGER.info("Duration: " + duration);
    }
  }

  //TODO: Implement Grinding Particles
  public void spawnMillstoneParticles(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
    
  }

  //TODO: Reimplement the finishGrindingSpin method
  public void finishMilling() {
    if (isMilling && getLevel() != null) {
      if (activeRecipe != null) {
      inventory.extractItem(0, 1, false); // "Extract" one item.
      ItemStack output = activeRecipe.getResultItem(getLevel().registryAccess()); // Get the output from the recipe
      ItemEntity entity = new ItemEntity(getLevel(), this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), output); // Create ItemEntity
      getLevel().addFreshEntity(entity); // Spawn ItemEntity

      if (inventory.getStackInSlot(0) == ItemStack.EMPTY) {
        activeRecipe = null; // Set the activeRecipe to null
      }
    }
    this.isMilling = false; // Set "isGrinding" to false
    this.duration = 0;
    this.markForUpdate();
    }
  }
}
