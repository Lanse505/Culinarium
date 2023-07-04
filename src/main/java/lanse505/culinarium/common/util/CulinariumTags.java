package lanse505.culinarium.common.util;

import lanse505.culinarium.Culinarium;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Locale;

public class CulinariumTags {

  public static class CulinariumBlockTags {
    public static TagKey<Block> HARD_SURFACE = getBlockTag("hard_surface");
  }

  public static class CulinariumItemTags {
    public static TagKey<Item> MILLABLE = getItemTag("millable");
  }

  private static TagKey<Item> getItemTag(String name) {
    return TagKey.create(Registries.ITEM, new ResourceLocation(Culinarium.MODID, name.toLowerCase(Locale.ROOT)));
  }

  private static TagKey<Block> getBlockTag(String name) {
    return TagKey.create(Registries.BLOCK, new ResourceLocation(Culinarium.MODID, name.toLowerCase(Locale.ROOT)));
  }

}
