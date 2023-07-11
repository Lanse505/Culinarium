package lanse505.culinarium;

import lanse505.culinarium.client.renderer.ChoppingBoardRenderer;
import lanse505.culinarium.client.renderer.MillstoneRenderer;
import lanse505.culinarium.common.block.impl.tile.ChoppingBoardTile;
import lanse505.culinarium.common.block.impl.tile.MillstoneTile;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.common.register.creativetabs.CulinariumCreativeTabs;
import lanse505.culinarium.data.provider.blockstate.CulinariumBlockStateProvider;
import lanse505.culinarium.data.provider.lang.CulinariumProviderEnUS;
import lanse505.culinarium.data.provider.loot.CulinariumLootTableProvider;
import lanse505.culinarium.data.provider.model.CulinariumItemModelProvider;
import lanse505.culinarium.data.provider.recipe.CulinariumRecipeProvider;
import lanse505.culinarium.data.provider.tag.CulinariumBlockTagProvider;
import lanse505.culinarium.data.provider.tag.CulinariumItemTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

// TODO:
//  - Following items requires textures/models:
//    - Straw,
//    - Wheat Husks,
//    - Wheat Berries,
//    - Mixed Wheat Berries,
//    - Flour,
//    - Hand-Sieve
//    - Knife
//    - Dough
//    - Chopping Block
//    - Carrot Chunks
//    - Slice o' Bread
//    - Toast
//  - Add Straw Bedding, Thatch, and Straw Bales
//  - Brewing?
//  - Berries & Jam?

@Mod(Culinarium.MODID)
public class Culinarium {
  public static final String MODID = "culinarium";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

  public Culinarium() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register listeners
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::registerBlockRenderers);
    modEventBus.addListener(this::registerCreativeTabs);
    modEventBus.addListener(this::gatherData);

    // Deferred Register
    CulinariumBlockRegistry.register(modEventBus);
    CulinariumItemRegistry.register(modEventBus);
    CulinariumCreativeTabs.CREATIVE_TABS.register(modEventBus);
    CulinariumRecipeRegistry.register(modEventBus);
  }

  private void commonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> registerCompostValues());
  }

  private void gatherData(GatherDataEvent event) {
    DataGenerator gen = event.getGenerator();
    PackOutput packOutput = gen.getPackOutput();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
    CulinariumBlockTagProvider blockTagProvider = new CulinariumBlockTagProvider(packOutput, lookupProvider, event.getExistingFileHelper());

    // Language Providers
    gen.addProvider(event.includeClient(), new CulinariumProviderEnUS(packOutput));
    // Tag Providers
    gen.addProvider(event.includeClient(), blockTagProvider);
    gen.addProvider(event.includeClient(), new CulinariumItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), event.getExistingFileHelper()));
    // BlockState Providers
    gen.addProvider(event.includeClient(), new CulinariumBlockStateProvider(packOutput, event.getExistingFileHelper()));
    // Recipe Providers
    gen.addProvider(event.includeClient(), new CulinariumRecipeProvider(packOutput));
    // Model Providers
    gen.addProvider(event.includeClient(), new CulinariumItemModelProvider(packOutput, event.getExistingFileHelper()));
    // Loot Table Providers
    gen.addProvider(event.includeClient(), new CulinariumLootTableProvider(packOutput));
  }

  @SuppressWarnings("unchecked")
  private void registerBlockRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer((BlockEntityType<? extends MillstoneTile>) CulinariumBlockRegistry.MILLSTONE_TILE.get(), MillstoneRenderer::new);
    event.registerBlockEntityRenderer((BlockEntityType<? extends ChoppingBoardTile>) CulinariumBlockRegistry.CHOPPING_BOARD_TILE.get(), ChoppingBoardRenderer::new);
  }

  private void registerCreativeTabs(BuildCreativeModeTabContentsEvent event) {
    if (event.getTab() == CulinariumCreativeTabs.MAIN.get()) {
      event.accept(CulinariumBlockRegistry.MILLSTONE_ITEM.get());
      event.accept(CulinariumBlockRegistry.CHOPPING_BOARD_ITEM.get());
      event.accept(CulinariumItemRegistry.HAND_SIEVE.get());
      event.accept(CulinariumItemRegistry.KNIFE.get());
    }
    if (event.getTab() == CulinariumCreativeTabs.PROCESSING.get()) {
      event.accept(CulinariumItemRegistry.STRAW.get());
      event.accept(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get());
      event.accept(CulinariumItemRegistry.WHEAT_HUSKS.get());
      event.accept(CulinariumItemRegistry.WHEAT_BERRIES.get());
      event.accept(CulinariumItemRegistry.FLOUR.get());
      event.accept(CulinariumItemRegistry.DOUGH.get());
      event.accept(CulinariumItemRegistry.SLICE_OF_BREAD.get());
      event.accept(CulinariumItemRegistry.TOAST.get());
      event.accept(CulinariumItemRegistry.CARROT_CHUNKS.get());
    }
    if (event.getTab() == CulinariumCreativeTabs.CROPS.get()) {
      event.accept(CulinariumItemRegistry.RYE_SEEDS.get());
      event.accept(CulinariumItemRegistry.RYE.get());
    }
  }

  private void registerCompostValues() {
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.FLOUR.get(), 0.15F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.CARROT_CHUNKS.get(), 0.22F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.RYE_SEEDS.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.WHEAT_HUSKS.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.WHEAT_BERRIES.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.SLICE_OF_BREAD.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.TOAST.get(), 0.3F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.DOUGH.get(), 0.5F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.STRAW.get(), 0.5F);
    ComposterBlock.COMPOSTABLES.put(CulinariumItemRegistry.RYE.get(), 0.65F);
  }
}
