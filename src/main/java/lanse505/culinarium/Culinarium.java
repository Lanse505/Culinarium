package lanse505.culinarium;

import lanse505.culinarium.client.MillstoneRenderer;
import lanse505.culinarium.block.impl.tile.MillstoneTile;
import lanse505.culinarium.data.provider.blockstate.CulinariumBlockStateProvider;
import lanse505.culinarium.data.provider.model.CulinariumItemModelProvider;
import lanse505.culinarium.data.provider.recipe.CulinariumMillingRecipeProvider;
import lanse505.culinarium.data.provider.tag.CulinariumBlockTagProvider;
import lanse505.culinarium.data.provider.tag.CulinariumItemTagProvider;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import lanse505.culinarium.register.CulinariumItemRegistry;
import lanse505.culinarium.register.CulinariumRecipeRegistry;
import lanse505.culinarium.register.creativetabs.CulinariumCreativeTabs;
import lanse505.culinarium.data.provider.lang.CulinariumProviderEnUS;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
//  - Foo
//  - Bar
//  - Baz

@Mod(Culinarium.MODID)
public class Culinarium {
  public static final String MODID = "culinarium";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

  public Culinarium() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register listeners
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::clientSetup);
    modEventBus.addListener(this::registerBlockRenderers);
    modEventBus.addListener(this::registerCreativeTabs);
    modEventBus.addListener(this::gatherData);

    // Deferred Register
    CulinariumBlockRegistry.register(modEventBus);
    CulinariumItemRegistry.register(modEventBus);
    CulinariumCreativeTabs.CREATIVE_TABS.register(modEventBus);
    CulinariumRecipeRegistry.register(modEventBus);
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
    gen.addProvider(event.includeClient(), new CulinariumMillingRecipeProvider(packOutput));
    // Model Providers
    gen.addProvider(event.includeClient(), new CulinariumItemModelProvider(packOutput, event.getExistingFileHelper()));
  }

  private void commonSetup(final FMLCommonSetupEvent event) {

  }

  private void clientSetup(final FMLClientSetupEvent event) {

  }

  private void registerBlockRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer((BlockEntityType<? extends MillstoneTile>) CulinariumBlockRegistry.MILLSTONE_TILE.get(), MillstoneRenderer::new);
  }

  private void registerCreativeTabs(BuildCreativeModeTabContentsEvent event) {
    if (event.getTab() == CulinariumCreativeTabs.MAIN.get()) {
      event.accept(CulinariumBlockRegistry.MILLSTONE_ITEM.get());
      event.accept(CulinariumItemRegistry.STRAW.get());
      event.accept(CulinariumItemRegistry.MIXED_WHEAT_BERRIES.get());
      event.accept(CulinariumItemRegistry.WHEAT_HUSKS.get());
      event.accept(CulinariumItemRegistry.WHEAT_BERRIES.get());
      event.accept(CulinariumItemRegistry.FLOUR.get());
      event.accept(CulinariumItemRegistry.RYE.get());
      event.accept(CulinariumItemRegistry.RYE_SEEDS.get());
    }
  }
}
