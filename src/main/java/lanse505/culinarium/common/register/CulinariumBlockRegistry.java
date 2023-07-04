package lanse505.culinarium.common.register;

import com.google.common.collect.Sets;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.block.base.growable.CulinariumCropBlock;
import lanse505.culinarium.common.block.impl.block.ChoppingBoardBlock;
import lanse505.culinarium.common.block.impl.block.MillstoneBlock;
import lanse505.culinarium.common.block.impl.tile.ChoppingBoardTile;
import lanse505.culinarium.common.block.impl.tile.MillstoneTile;
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
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Culinarium.MODID);
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Culinarium.MODID);
  private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Culinarium.MODID);

  // Registration Objects
  public static final RegistryObject<Block> MILLSTONE = registerBlock("millstone", () -> new MillstoneBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));
  public static final RegistryObject<Item> MILLSTONE_ITEM = registerBlockItem("millstone", () -> new BlockItem(MILLSTONE.get(), DEFAULT_ITEM_BLOCK_PROPERTIES));
  public static final RegistryObject<BlockEntityType<?>> MILLSTONE_TILE = registerBlockEntity("millstone", () -> new BlockEntityType<>(MillstoneTile::new, Sets.newHashSet(MILLSTONE.get()), null));
  public static final RegistryObject<Block> RYE = registerBlock("rye", () -> new CulinariumCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT), true));
  public static final RegistryObject<Block> CHOPPING_BOARD = registerBlock("chopping_board", () -> new ChoppingBoardBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));
  public static final RegistryObject<Item> CHOPPING_BOARD_ITEM = registerBlockItem("chopping_board", () -> new BlockItem(CHOPPING_BOARD.get(), DEFAULT_ITEM_BLOCK_PROPERTIES));
  public static final RegistryObject<BlockEntityType<?>> CHOPPING_BOARD_TILE = registerBlockEntity("chopping_board", () -> new BlockEntityType<>(ChoppingBoardTile::new, Sets.newHashSet(CHOPPING_BOARD.get()), null));


  // Registration Methods
  private static RegistryObject<Block> registerBlock(String name, Supplier<Block> supplier) {
    return register(BLOCKS, name, supplier);
  }

  private static RegistryObject<Item> registerBlockItem(String name, Supplier<Item> supplier) {
    return register(ITEMS, name, supplier);
  }

  private static RegistryObject<BlockEntityType<?>> registerBlockEntity(String name, Supplier<BlockEntityType<?>> supplier) {
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
    event.register(new ResourceLocation(Culinarium.MODID, "block/millstone_top"));
    event.register(new ResourceLocation(Culinarium.MODID, "block/millstone_bottom"));
  }

}
