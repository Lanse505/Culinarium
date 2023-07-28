package lanse505.culinarium.common.register;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.container.BrewingBarrelMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CulinariumMenuTypeRegistry {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Culinarium.MODID);

    public static final RegistryObject<MenuType<BrewingBarrelMenu>> BREWING_BARREL_MENU = MENU_TYPES.register("brewing_barrel",
            () -> IForgeMenuType.create((id, inv, data) -> new BrewingBarrelMenu(id, inv.player, data.readBlockPos(), new FluidTank(8000), new ItemStackHandler(4), new FluidTank(8000))));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
