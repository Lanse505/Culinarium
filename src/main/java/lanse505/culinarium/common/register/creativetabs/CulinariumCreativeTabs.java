package lanse505.culinarium.common.register.creativetabs;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.IEventBus;

public class CulinariumCreativeTabs {

    private static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(Culinarium.MODID);

    public static final CreativeTabRegistryObject MAIN = CREATIVE_TABS.registerMain(
            "culinarium",
            Component.translatable("itemGroup.culinarium"),
            CulinariumBlockRegistry.MILLSTONE::getItem,
            (builder) -> builder
    );

    public static final CreativeTabRegistryObject PROCESSING = CREATIVE_TABS.registerMain(
            "culinarium_processing",
            Component.translatable("itemGroup.culinarium_processing"),
            CulinariumItemRegistry.FLOUR,
            (builder) -> builder.withTabsBefore(CulinariumCreativeTabs.MAIN.key())
    );

    public static final CreativeTabRegistryObject CROPS = CREATIVE_TABS.registerMain(
            "culinarium_crops",
            Component.translatable("itemGroup.culinarium_crops"),
            CulinariumItemRegistry.RYE_SEEDS::get,
            (builder) -> builder.withTabsBefore(CulinariumCreativeTabs.PROCESSING.key())
    );

    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }

}
