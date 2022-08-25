package lanse505.culinarium;

import com.mojang.logging.LogUtils;
import lanse505.culinarium.block.client.GrindstoneRenderer;
import lanse505.culinarium.block.tile.GrindstoneTile;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import lanse505.culinarium.register.CulinariumItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Culinarium.MODID)
public class Culinarium {
  public static final String MODID = "culinarium";
  public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
  public static final CreativeModeTab CULINARIUM_TAB = new CreativeModeTab(MODID) {
    @Override
    public ItemStack makeIcon() {
      return new ItemStack(CulinariumItemRegistry.FLOUR.get());
    }
  };

  public Culinarium() {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

    // Register listeners
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::clientSetup);
    modEventBus.addListener(this::registerBlockRenderers);

    // Deferred Register
    CulinariumBlockRegistry.register(modEventBus);
    CulinariumItemRegistry.register(modEventBus);
  }

  private void commonSetup(final FMLCommonSetupEvent event) {

  }

  private void clientSetup(final FMLClientSetupEvent event) {

  }

  private void registerBlockRenderers(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer((BlockEntityType<? extends GrindstoneTile>) CulinariumBlockRegistry.GRINDSTONE_TILE.get(), GrindstoneRenderer::new);
  }
}
