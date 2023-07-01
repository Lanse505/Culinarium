package lanse505.culinarium.data.provider.tag;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.util.CulinariumTags;
import lanse505.culinarium.util.CulinariumTags.CulinariumItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class CulinariumBlockTagProvider extends BlockTagsProvider {

  public CulinariumBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, Culinarium.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider pProvider) {

  }


}
