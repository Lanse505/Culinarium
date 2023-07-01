package lanse505.culinarium.register;

import lanse505.culinarium.Culinarium;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CulinariumItemRegistry {

  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Culinarium.MODID);

  // Flour-Production
  public static final RegistryObject<Item> RYE = ITEMS.register("rye", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> RYE_SEEDS = ITEMS.register("rye_seeds", () -> new ItemNameBlockItem(CulinariumBlockRegistry.RYE.get(), new Item.Properties()));
  public static final RegistryObject<Item> MIXED_WHEAT_BERRIES = ITEMS.register("mixed_wheat_berries", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> STRAW = ITEMS.register("straw", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> WHEAT_HUSKS = ITEMS.register("wheat_husks", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> WHEAT_BERRIES = ITEMS.register("wheat_berries", () -> new Item(new Item.Properties()));
  public static final RegistryObject<Item> FLOUR = ITEMS.register("flour", () -> new Item(new Item.Properties()));

  public static void register(IEventBus bus) {
    ITEMS.register(bus);
  }

}
