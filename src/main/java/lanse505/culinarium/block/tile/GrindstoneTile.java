package lanse505.culinarium.block.tile;

import com.hrznstudio.titanium.annotation.Save;
import com.hrznstudio.titanium.block.BasicTileBlock;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class GrindstoneTile extends ActiveTile<GrindstoneTile> {

  private static final TagKey<Item> GRINDABLE = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), new ResourceLocation(Culinarium.MODID, "grindable"));

  @Save
  public boolean isGrinding;

  public static final int DEFAULT_DURATION = 80;

  @Save
  public int duration;

  @Save
  public InventoryComponent<GrindstoneTile> inventory;

  private SmeltingRecipe activeRecipe = null;

  public GrindstoneTile(BlockPos pos, BlockState state) {
    super((BasicTileBlock<GrindstoneTile>) CulinariumBlockRegistry.GRINDSTONE.get(),
            CulinariumBlockRegistry.GRINDSTONE_TILE.get(), pos, state);
    this.addInventory(inventory = new InventoryComponent<GrindstoneTile>("inventory", 0, 0, 1)
            .setComponentHarness(this)
            .setInputFilter((stack, amount) -> stack.is(GRINDABLE)));
  }

  @Override
  public InteractionResult onActivated(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
    if (getLevel() != null && !isGrinding) {
      if (!inventory.getStackInSlot(0).isEmpty() && player.getMainHandItem().isEmpty() && player.isCrouching()) {
        if (isGrinding) isGrinding = false;
        player.addItem(inventory.extractItem(0, Integer.MAX_VALUE, false));
        return InteractionResult.SUCCESS;
      }

      if (!inventory.getStackInSlot(0).isEmpty() && player.getMainHandItem().isEmpty()) {
        //if (activeRecipe != null)
        isGrinding = true; // If the recipe isn't null and "isGrinding" is false. Set "isGrinding" to true.
        if (level != null && level.isClientSide()) spawnGrindParticles(player, hand, facing, hitX, hitY, hitZ); // If it's client-side then spawnGrindParticles.
        return InteractionResult.SUCCESS;
      }

      if (!player.getMainHandItem().isEmpty() && inventory.getStackInSlot(0).isEmpty()) {
        inventory.insertItem(0, player.getMainHandItem(), false); // Insert into the inventory
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY); // Empty their Main-Hand.
        markComponentDirty(); // Mark Component for Update since we changed its contents.
        return InteractionResult.SUCCESS;
      }

      // If item in hand and in grindstone inventory but they're different items
      if (!inventory.getStackInSlot(0).isEmpty() && !player.getMainHandItem().isEmpty() && !inventory.getStackInSlot(0).is(player.getMainHandItem().getItem())) {
        activeRecipe = null; // Set recipe null
        isGrinding = false;  // Set "isGrinding" to false
        ItemStack stack = inventory.getStackInSlot(0); // Get the ItemStack from the Inventory
        ItemEntity entity = new ItemEntity(getLevel(), hitX, hitY, hitZ, stack); // Create an ItemEntity
        getLevel().addFreshEntity(entity); // Spawn the ItemEntity
        inventory.setStackInSlot(0, player.getMainHandItem()); // Set the inventorys ItemStack to be the same as the one in the players Main-Hand.
        player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY); // Set the Main-Hand ItemStack to Empty.
        markComponentDirty(); // Mark Component for Update since we changed its contents.
        return InteractionResult.SUCCESS;
      }
    }
    return InteractionResult.PASS;
  }

  @Override
  public void serverTick(Level level, BlockPos pos, BlockState state, GrindstoneTile blockEntity) {
//    if (inventory.getStackInSlot(0) != ItemStack.EMPTY && activeRecipe == null) {
//      Optional<SmeltingRecipe> smelting = level.getRecipeManager().getRecipes().stream().filter(recipe -> recipe instanceof SmeltingRecipe)
//              .filter(recipe -> recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.test(inventory.getStackInSlot(0))))
//              .map(recipe -> (SmeltingRecipe) recipe).findFirst();
//      smelting.ifPresent(smeltingRecipe -> activeRecipe = smeltingRecipe);
//    }
    if (isGrinding && duration < 80) duration++;
    //if (isGrinding && (activeRecipe == null || inventory.getStackInSlot(0).isEmpty())) isGrinding = false;
    if (!isGrinding && duration != 0) duration = 0;
    this.markForUpdate();
    this.markComponentDirty();
    if (duration == GrindstoneTile.DEFAULT_DURATION) {
      finishGrindingSpin();
    }
  }

  @NotNull
  @Override
  public GrindstoneTile getSelf() {
    return this;
  }

  public void spawnGrindParticles(Player player, InteractionHand hand, Direction facing, double hitX, double hitY, double hitZ) {
    
  }

  public void finishGrindingSpin() {
    if (isGrinding && getLevel() != null) {
      if (activeRecipe != null) {
      inventory.extractItem(0, 1, false); // "Extract" one item.
      //ItemStack output = activeRecipe.getResultItem(); // Get the output from the recipe
      //ItemEntity entity = new ItemEntity(getLevel(), this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ(), output); // Create ItemEntity
      //getLevel().addFreshEntity(entity); // Spawn ItemEntity

      //if (inventory.getStackInSlot(0) == ItemStack.EMPTY) {
      //  activeRecipe = null; // Set the activeRecipe to null
      //}
    }
    this.isGrinding = false; // Set "isGrinding" to false
    this.duration = 0;
    this.markForUpdate();
    this.markComponentDirty();
    this.syncObject(this);
    }
  }

}
