package lanse505.culinarium.common.register;

import com.google.common.collect.Sets;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.block.base.growable.CulinariumCropBlock;
import lanse505.culinarium.common.block.impl.block.ChoppingBoardBlock;
import lanse505.culinarium.common.block.impl.block.MillstoneBlock;
import lanse505.culinarium.common.block.impl.block.barrel.BrewingBarrelBlock;
import lanse505.culinarium.common.block.impl.tile.ChoppingBoardTile;
import lanse505.culinarium.common.block.impl.tile.MillstoneTile;
import lanse505.culinarium.common.block.impl.tile.barrel.BrewingBarrelTile;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class CulinariumBlockRegistry {

    private static final Item.Properties DEFAULT_ITEM_BLOCK_PROPERTIES = new Item.Properties();

    // DeferredRegisters
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Culinarium.MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Culinarium.MODID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Culinarium.MODID);

    // Blocks
    public static final RegistryObject<CulinariumCropBlock> RYE = BLOCKS.register("rye", () -> new CulinariumCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));
    public static final RegistryObject<RotatedPillarBlock> STRAW_BALE =
            registerBlock("straw_bale",
                    () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.HAY_BLOCK)),
                    (block) -> () -> new BlockItem(block.get(), DEFAULT_ITEM_BLOCK_PROPERTIES));

    // Blocks /w BlockEntities
    public static final BlockHolderWithTile<MillstoneBlock, BlockItem, MillstoneTile> MILLSTONE =
            registerBlockWithTile("millstone",
                    () -> new MillstoneBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()),
                    CulinariumBlockRegistry::getDefaultBlockItem,
                    MillstoneTile::new,
                    CulinariumBlockRegistry::getDefaultType);
    public static final BlockHolderWithTile<ChoppingBoardBlock, BlockItem, ChoppingBoardTile> CHOPPING_BOARD =
            registerBlockWithTile("chopping_block",
                    () -> new ChoppingBoardBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()),
                    CulinariumBlockRegistry::getDefaultBlockItem,
                    ChoppingBoardTile::new,
                    CulinariumBlockRegistry::getDefaultType);
    public static final BlockHolderWithTile<BrewingBarrelBlock, BlockItem, BrewingBarrelTile> BREWING_BARREL =
            registerBlockWithTile("brewing_barrel",
                    () -> new BrewingBarrelBlock(BlockBehaviour.Properties.copy(Blocks.BARREL).noOcclusion()),
                    CulinariumBlockRegistry::getDefaultBlockItem,
                    BrewingBarrelTile::new,
                    CulinariumBlockRegistry::getDefaultType);

    // Registration Methods
    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITY_TYPES.register(bus);
        bus.addListener((CulinariumBlockRegistry::registerAdditionalBlockModels));
    }

    public static <BLOCK extends Block, BLOCKITEM extends BlockItem> RegistryObject<BLOCK> registerBlock
            (String id, Supplier<BLOCK> block, Function<RegistryObject<BLOCK>, Supplier<BLOCKITEM>> item) {
        RegistryObject<BLOCK> b = BLOCKS.register(id, block);
        ITEMS.register(id, item.apply(b));
        return b;
    }

    public static <BLOCK extends Block, BLOCKITEM extends BlockItem, T extends BlockEntity>
    BlockHolderWithTile<BLOCK, BLOCKITEM, T> registerBlockWithTile(String id,
                                                                         Supplier<BLOCK> block,
                                                                         Function<RegistryObject<BLOCK>, Supplier<BLOCKITEM>> item,
                                                                         BlockEntityType.BlockEntitySupplier<T> factory,
                                                                         BiFunction<BlockEntityType.BlockEntitySupplier<T>, RegistryObject<BLOCK>, Supplier<BlockEntityType<T>>> type)
    {
        RegistryObject<BLOCK> b = BLOCKS.register(id, block);
        RegistryObject<BLOCKITEM> i = ITEMS.register(id, item.apply(b));
        RegistryObject<BlockEntityType<T>> t = BLOCK_ENTITY_TYPES.register(id, type.apply(factory, b));
        return new BlockHolderWithTile<>(b, i, t);
    }

    private static <BLOCK extends Block> Supplier<BlockItem> getDefaultBlockItem(RegistryObject<BLOCK> block) {
        return () -> new BlockItem(block.get(), DEFAULT_ITEM_BLOCK_PROPERTIES);
    }

    private static <BLOCK extends Block, T extends BlockEntity, TYPE extends BlockEntityType<T>> Supplier<BlockEntityType<T>> getDefaultType
            (BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<BLOCK> block) {
        return () -> new BlockEntityType<>(factory, Sets.newHashSet(block.get()), null);
    }

    private static void registerAdditionalBlockModels(ModelEvent.RegisterAdditional event) {
        event.register(new ResourceLocation(Culinarium.MODID, "block/millstone_top"));
        event.register(new ResourceLocation(Culinarium.MODID, "block/millstone_bottom"));
    }

    public record BlockHolderWithTile<BLOCK extends Block, BLOCKITEM extends BlockItem, T extends BlockEntity>
            (RegistryObject<BLOCK> blockRO, RegistryObject<BLOCKITEM> itemRO, RegistryObject<BlockEntityType<T>> typeRO) implements ItemLike {

        public BLOCK getBlock() {
            return blockRO.get();
        }

        public BLOCKITEM getItem() {
            return itemRO.get();
        }

        public BlockEntityType<T> getType() {
            return typeRO.get();
        }

        @Override
        public Item asItem() {
            return getItem();
        }
    }
}
