package lanse505.culinarium.client;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.client.gui.BrewingBarrelScreen;
import lanse505.culinarium.client.renderer.ChoppingBoardRenderer;
import lanse505.culinarium.client.renderer.MillstoneRenderer;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.register.CulinariumMenuTypeRegistry;
import lanse505.culinarium.common.register.creativetabs.CulinariumCreativeTabs;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class CulinariumClient {

  public static void handleClient() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(CulinariumClient::clientSetup);
    modEventBus.addListener(CulinariumClient::registerBlockRenderers);
    modEventBus.addListener(CulinariumClient::registerCreativeTabsContent);
    modEventBus.addListener(CulinariumClient::registerAdditionalBlockModels);
  }

  public static void clientSetup(FMLClientSetupEvent event) {
    event.enqueueWork(CulinariumClient::registerScreens);
  }

  private static void registerScreens() {
    MenuScreens.register(CulinariumMenuTypeRegistry.BREWING_BARREL_MENU.get(), BrewingBarrelScreen::new);
  }

  @SuppressWarnings("unchecked")
  private static void registerBlockRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(CulinariumBlockRegistry.MILLSTONE.getType(), MillstoneRenderer::new);
    event.registerBlockEntityRenderer(CulinariumBlockRegistry.CHOPPING_BOARD.getType(), ChoppingBoardRenderer::new);
  }

  private static void registerAdditionalBlockModels(ModelEvent.RegisterAdditional event) {
    event.register(new ResourceLocation(Culinarium.MODID, "block/millstone_top"));
    event.register(new ResourceLocation(Culinarium.MODID, "block/millstone_bottom"));
  }

  private static void registerCreativeTabsContent(BuildCreativeModeTabContentsEvent event) {
    if (event.getTab() == CulinariumCreativeTabs.MAIN.get()) {
      event.accept(CulinariumBlockRegistry.MILLSTONE.getItem());
      event.accept(CulinariumBlockRegistry.CHOPPING_BOARD.getItem());
      event.accept(CulinariumItemRegistry.HAND_SIEVE.get());
      event.accept(CulinariumItemRegistry.KNIFE.get());
      event.accept(CulinariumBlockRegistry.BREWING_BARREL.getItem());
      //event.accept(CulinariumBlockRegistry.SOAKING_BARREL_ITEM.get());
      //event.accept(CulinariumBlockRegistry.STORAGE_BARREL_ITEM.get());
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
      event.accept(CulinariumBlockRegistry.STRAW_BALE.get().asItem());
      event.accept(CulinariumItemRegistry.BOWL_OF_WATER.get());
    }
    if (event.getTab() == CulinariumCreativeTabs.CROPS.get()) {
      event.accept(CulinariumItemRegistry.RYE_SEEDS.get());
      event.accept(CulinariumItemRegistry.RYE.get());
    }
  }
}
