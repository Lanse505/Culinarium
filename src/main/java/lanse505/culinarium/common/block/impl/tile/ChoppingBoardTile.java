package lanse505.culinarium.common.block.impl.tile;

import lanse505.culinarium.common.block.base.tile.CulinariumActiveTile;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.util.ItemStackHandlerBuilder;
import lanse505.culinarium.server.recipe.ChoppingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Optional;

public class ChoppingBoardTile extends CulinariumActiveTile<ChoppingBoardTile> {

  private static final HashMap<Item, Boolean> hasRecipeCache = new HashMap<>();
  public ChoppingRecipe activeRecipe = null; // Add recipe
  public int chops = 0;  private final ItemStackHandler inventory = ItemStackHandlerBuilder.builder()
          .slots(1)
          .insertCondition(this::isValid)
          .extractCondition((slot, stack) -> false)
          .build();
  @SuppressWarnings("unchecked")
  public ChoppingBoardTile(BlockPos pPos, BlockState pBlockState) {
    super(CulinariumBlockRegistry.CHOPPING_BOARD.getBlock(), CulinariumBlockRegistry.CHOPPING_BOARD.getType(), pPos, pBlockState);
  }

  public boolean isValid(int slot, @Nonnull ItemStack stack) {
    return !hasItem() && (
            hasRecipeCache.containsKey(stack.getItem()) || // Has cached value
                    this.level.getRecipeManager()                  // Otherwise check if recipe exists
                            .getRecipes()
                            .stream().filter((recipe) -> recipe instanceof ChoppingRecipe)
                            .anyMatch((chopping) -> {
                              boolean isValid = ((ChoppingRecipe) chopping).isValid(stack);
                              if (isValid) hasRecipeCache.put(stack.getItem(), true);
                              return isValid;
                            })
    );
  }

  public ItemStackHandler getInventory() {
    return inventory;
  }

  public boolean hasItem() {
    return !inventory.getStackInSlot(0).isEmpty();
  }

  @Override
  public void serverTick(Level level, BlockPos pos, BlockState state, ChoppingBoardTile blockEntity) {
    if (hasItem() && activeRecipe == null) {
      Optional<ChoppingRecipe> found = level.getRecipeManager().getRecipes().stream().filter(recipe -> recipe instanceof ChoppingRecipe)
              .filter(recipe -> ((ChoppingRecipe) recipe).getInput().test(inventory.getStackInSlot(0)))
              .map(recipe -> (ChoppingRecipe) recipe).findFirst();
      found.ifPresent(recipe -> activeRecipe = recipe);
    }
    if (!hasItem() && activeRecipe != null) {
      activeRecipe = null;
    }
  }

  public InteractionResult performChop(Player player, InteractionHand hand) {
    if (!getInventory().getStackInSlot(0).isEmpty() && activeRecipe != null && player.getItemInHand(hand).is(CulinariumItemRegistry.KNIFE.get())) {
      if (isServer()) {
        player.getInventory().placeItemBackInInventory(activeRecipe.getOutput().copy());
        player.getItemInHand(hand).hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(hand));
        spawnChoppingParticle(player, hand, player.getDirection(), getBlockPos());
        chops++;
        if (chops >= activeRecipe.getChops()) {
          chops = 0;
          activeRecipe = null;
        }
      }
      return InteractionResult.SUCCESS;
    }
    return InteractionResult.FAIL;
  }

  public void spawnChoppingParticle(Player player, InteractionHand hand, Direction facing, BlockPos pos) {

  }

  @Override
  protected void saveAdditional(CompoundTag compoundTag) {
    super.saveAdditional(compoundTag);
    if (activeRecipe != null) compoundTag.putString("activeRecipe", activeRecipe.getId().toString());
    compoundTag.put("inventory", inventory.serializeNBT());
    compoundTag.putInt("chops", chops);
  }

  @Override
  public void load(CompoundTag compoundTag) {
    super.load(compoundTag);
    if (compoundTag.contains("activeRecipe") && level != null) {
      activeRecipe = (ChoppingRecipe) level.getRecipeManager().byKey(new ResourceLocation(compoundTag.getString("activeRecipe"))).orElse(null);
    }
    if (compoundTag.contains("inventory")) inventory.deserializeNBT(compoundTag.getCompound("inventory"));
    if (compoundTag.contains("chops")) chops = compoundTag.getInt("chops");
  }

  @Override
  public CompoundTag getUpdateTag() {
    CompoundTag tag = super.getUpdateTag();
    if (activeRecipe != null) tag.putString("activeRecipe", activeRecipe.getId().toString());
    tag.put("inventory", inventory.serializeNBT());
    tag.putInt("chops", chops);
    return tag;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    super.handleUpdateTag(tag);
    if (tag.contains("activeRecipe") && level != null) {
      activeRecipe = (ChoppingRecipe) level.getRecipeManager().byKey(new ResourceLocation(tag.getString("activeRecipe"))).orElse(null);
    }
    if (tag.contains("inventory")) inventory.deserializeNBT(tag.getCompound("inventory"));
    if (tag.contains("chops")) chops = tag.getInt("chops");
  }

  @Nullable
  @Override
  public Packet<ClientGamePacketListener> getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }




}
