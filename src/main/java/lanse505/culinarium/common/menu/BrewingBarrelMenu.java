package lanse505.culinarium.common.menu;

import lanse505.culinarium.common.menu.base.BaseBarrelMenu;
import lanse505.culinarium.common.register.CulinariumMenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.TankView;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.IPropertyManaged;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.Property;
import xyz.brassgoggledcoders.shadyskies.containersyncing.property.PropertyTypes;

import java.util.function.Supplier;

public class BrewingBarrelMenu extends BaseBarrelMenu implements IPropertyManaged {

  private final Property<TankView> brewable;
  private final Property<TankView> brewed;
  private final Property<ProgressView> progress;

  public BrewingBarrelMenu(int menuId, Player player, BlockPos pos, FluidTank brewable, ItemStackHandler storage, FluidTank brewed, Supplier<ProgressView> progress) {
    super(CulinariumMenuTypeRegistry.BREWING_BARREL_MENU.get(), menuId, player, pos);
    this.SLOT_COUNT = 4;
    this.SLOT_INPUT = 0;
    this.SLOT_INPUT_END = 3;
    layoutPlayerInventorySlots(player.getInventory(), 8, 121);
    this.brewable = this.propertyManager.addTrackedProperty(PropertyTypes.TANK_VIEW.create(
            () -> TankView.fromFluidTank(brewable),
            (value) -> brewable.setFluid(value.fluidStack())
    ));
    this.brewed = this.propertyManager.addTrackedProperty(PropertyTypes.TANK_VIEW.create(
            () -> TankView.fromFluidTank(brewed),
            (value) -> brewed.setFluid(value.fluidStack())
    ));
    this.progress = this.propertyManager.addTrackedProperty(PropertyTypes.PROGRESS_VIEW.create(progress));
    addSlot(new SlotItemHandler(storage, 0, 62, 37));
    addSlot(new SlotItemHandler(storage, 1, 98, 37));
    addSlot(new SlotItemHandler(storage, 2, 62, 55));
    addSlot(new SlotItemHandler(storage, 3, 98, 55));
  }

  public Property<TankView> getBrewable() {
    return this.brewable;
  }

  public Property<TankView> getBrewed() {
    return this.brewed;
  }

  public Property<ProgressView> getProgress() {
    return progress;
  }

  @Override
  public void broadcastChanges() {
    super.broadcastChanges();
    if (player instanceof ServerPlayer serverPlayer) {
      this.propertyManager.sendChanges(serverPlayer, false);
    }
  }

  @Override
  public void broadcastFullState() {
    super.broadcastFullState();
    if (player instanceof ServerPlayer serverPlayer) {
      this.propertyManager.sendChanges(serverPlayer, true);
    }
  }
}
