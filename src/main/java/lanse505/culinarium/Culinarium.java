package lanse505.culinarium;

import lanse505.culinarium.client.CulinariumClient;
import lanse505.culinarium.common.network.CulinariumCommonProxy;
import lanse505.culinarium.common.network.CulinariumNetworkHandler;
import lanse505.culinarium.common.network.provider.CulinariumSafeSuppliers;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.register.CulinariumMenuTypeRegistry;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.common.register.creativetabs.CulinariumCreativeTabs;
import lanse505.culinarium.data.provider.blockstate.CulinariumBlockStateProvider;
import lanse505.culinarium.data.provider.lang.CulinariumProviderEnUS;
import lanse505.culinarium.data.provider.loot.CulinariumLootTableProvider;
import lanse505.culinarium.data.provider.model.CulinariumItemModelProvider;
import lanse505.culinarium.data.provider.recipe.CulinariumRecipeProvider;
import lanse505.culinarium.data.provider.tag.CulinariumBlockTagProvider;
import lanse505.culinarium.data.provider.tag.CulinariumItemTagProvider;
import lanse505.culinarium.server.CulinariumServer;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.brassgoggledcoders.shadyskies.containersyncing.ContainerSyncing;

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
//
// TODO: Reconsider the whole CreativeTab code
@Mod(Culinarium.MODID)
public class Culinarium {
  public static final String MODID = "culinarium";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
  public static CulinariumCommonProxy proxy = DistExecutor.safeRunForDist(CulinariumSafeSuppliers.getClientProxy(), CulinariumSafeSuppliers.getServerProxy());
  public static CulinariumNetworkHandler handler = new CulinariumNetworkHandler();
  private static ContainerSyncing containerSyncing;

  public Culinarium() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register listeners
    if (FMLEnvironment.dist == Dist.CLIENT) {
      CulinariumClient.handleClient();
    }

    if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
      CulinariumServer.handleServer();
    }

    // Event Subscription
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::gatherData);

    // Deferred Register
    CulinariumBlockRegistry.register(modEventBus);
    CulinariumItemRegistry.register(modEventBus);
    CulinariumCreativeTabs.register(modEventBus);
    CulinariumRecipeRegistry.register(modEventBus);
    CulinariumMenuTypeRegistry.register(modEventBus);

    containerSyncing = ContainerSyncing.setup(MODID, LOGGER);
  }

  public static ContainerSyncing getContainerSyncing() {
    return containerSyncing;
  }

  private void commonSetup(FMLCommonSetupEvent event) {
    event.enqueueWork(this::registerCompostValues);
    handler.init();
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
