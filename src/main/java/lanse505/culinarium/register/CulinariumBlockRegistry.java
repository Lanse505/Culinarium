package lanse505.culinarium.register;

import com.google.common.collect.Sets;
import com.hrznstudio.titanium.block.tile.ActiveTile;
import com.hrznstudio.titanium.nbthandler.NBTManager;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.block.GrindstoneBlock;
import lanse505.culinarium.block.tile.GrindstoneTile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CulinariumBlockRegistry {

  // Defaults
  private static final Item.Properties DEFAULT_ITEM_BLOCK_PROPERTIES = new Item.Properties();



  // DeferredRegisters
  private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Culinarium.MODID);
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Culinarium.MODID);
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Culinarium.MODID);



  // Registration Objects
  public static final RegistryObject<Block> GRINDSTONE = registerBlock("grindstone", () -> new GrindstoneBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
  public static final RegistryObject<Item> GRINDSTONE_ITEM = registerBlockItem("grindstone", () -> new BlockItem(GRINDSTONE.get(), DEFAULT_ITEM_BLOCK_PROPERTIES));
  public static final RegistryObject<BlockEntityType<?>> GRINDSTONE_TILE = registerBlockEntity("grindstone", () -> new BlockEntityType<>(GrindstoneTile::new, Sets.newHashSet(GRINDSTONE.get()), null), GrindstoneTile.class);



  // Registration Methods
  private static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
    return register(BLOCKS, name, supplier);
  }

  private static RegistryObject<Item> registerBlockItem(String name, Supplier<Item> supplier) {
    return register(ITEMS, name, supplier);
  }

  private static <T extends ActiveTile<T>> RegistryObject<BlockEntityType<?>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> supplier) {
    return register(BLOCK_ENTITY_TYPES, name, supplier);
  }

  private static <T> RegistryObject<T> register(DeferredRegister<T> registry, String name, Supplier<T> supplier) {
    return registry.register(name, supplier);
  }

  public static void register(IEventBus bus) {
    BLOCKS.register(bus);
    ITEMS.register(bus);
    BLOCK_ENTITY_TYPES.register(bus);
    bus.addListener((CulinariumBlockRegistry::registerAdditionalBlockModels));
  }

  private static void registerAdditionalBlockModels(ModelEvent.RegisterAdditional event) {
    event.register(new ResourceLocation(Culinarium.MODID, "block/grindstone_top"));
    event.register(new ResourceLocation(Culinarium.MODID, "block/grindstone_bottom"));
  }

}
