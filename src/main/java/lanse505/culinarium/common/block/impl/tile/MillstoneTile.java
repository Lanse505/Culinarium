package lanse505.culinarium.common.block.impl.tile;

import lanse505.culinarium.common.block.base.tile.CulinariumActiveTile;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.util.ItemStackHandlerBuilder;
import lanse505.culinarium.common.recipe.MillingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;

public class MillstoneTile extends CulinariumActiveTile<MillstoneTile> {

  private static final HashMap<Item, Boolean> hasRecipeCache = new HashMap<>();

  private final ItemStackHandler inventory = ItemStackHandlerBuilder.builder()
          .slots(1)
          .insertCondition(this::hasValidRecipe)
          .build();
  private final LazyOptional<IItemHandler> inventoryHandler = LazyOptional.of(() -> inventory);

  public boolean isMilling;

  public int duration;

  public MillingRecipe activeRecipe = null;

  @SuppressWarnings({"unchecked", "rawtypes"})
  public MillstoneTile(BlockPos pos, BlockState state) {
    super(CulinariumBlockRegistry.MILLSTONE.getBlock(), CulinariumBlockRegistry.MILLSTONE.getType(), pos, state);
  }

  private boolean hasValidRecipe(int slot, @Nonnull ItemStack stack) {
    if (hasRecipeCache.containsKey(stack.getItem())) {
      return hasRecipeCache.get(stack.getItem());
    } else {
      boolean isValid = level.getRecipeManager().getRecipes().stream()
              .filter(recipe -> recipe instanceof MillingRecipe)
              .anyMatch(recipe -> {
                boolean recipeIsValid = ((MillingRecipe) recipe).isValid(stack);
                if (recipeIsValid) {
                  hasRecipeCache.put(stack.getItem(), true);
                }
                return recipeIsValid;
              });
      hasRecipeCache.put(stack.getItem(), isValid);
      return isValid;
    }
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

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      return inventoryHandler.cast();
    }
    return super.getCapability(cap);
  }

  @Override
  public void serverTick(Level level, BlockPos pos, BlockState state, MillstoneTile blockEntity) {
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
    if (activeRecipe != null && duration >= activeRecipe.getDuration()) {
      finishMilling();
    }
  }

  @Override
  public void clientTick(Level level, BlockPos pos, BlockState state, MillstoneTile blockEntity) {
    if (isMilling) {
      spawnMillstoneParticles(
              level.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false),
              InteractionHand.MAIN_HAND,
              state.getValue(BlockStateProperties.FACING),
              pos
      );
    }
  }

  //TODO: Implement Grinding Particles
  public void spawnMillstoneParticles(Player player, InteractionHand hand, Direction facing, BlockPos pos) {

  }

  public void finishMilling() {
    if (getLevel() != null) {
      if (activeRecipe != null) {
        inventory.extractItem(0, 1, false); // "Extract" one item.
        for (ItemStack output : activeRecipe.getOutput()) {
          ItemEntity entity = new ItemEntity(getLevel(), this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), output.copy()); // Create ItemEntity
          getLevel().addFreshEntity(entity); // Spawn ItemEntity
        }

        if (inventory.getStackInSlot(0) == ItemStack.EMPTY) {
          activeRecipe = null; // Set the activeRecipe to null
        }
      }
      this.isMilling = false; // Set "isMilling" to false
      this.duration = 0;
      this.markForUpdate();
    }
  }
}
