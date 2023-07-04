package lanse505.culinarium.data.provider.tag;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.util.CulinariumTags.CulinariumItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CulinariumItemTagProvider extends ItemTagsProvider {
  public CulinariumItemTagProvider(
          PackOutput output,
          CompletableFuture<HolderLookup.Provider> holderProvider,
          CompletableFuture<TagLookup<Block>> blockLookup,
          @Nullable ExistingFileHelper existingFileHelper)
  {
    super(output, holderProvider, blockLookup, Culinarium.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider pProvider) {
    tag(CulinariumItemTags.MILLABLE)
            .add(CulinariumItemRegistry.WHEAT_BERRIES.get())
            .add(CulinariumItemRegistry.WHEAT_HUSKS.get());
  }
}
