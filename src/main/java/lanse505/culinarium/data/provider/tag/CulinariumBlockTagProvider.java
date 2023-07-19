package lanse505.culinarium.data.provider.tag;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.util.CulinariumTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CulinariumBlockTagProvider extends BlockTagsProvider {

    public CulinariumBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Culinarium.MODID, existingFileHelper);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(CulinariumTags.CulinariumBlockTags.HARD_SURFACE)
                .addTags(Tags.Blocks.STONE, Tags.Blocks.ORES)
                .addTags(BlockTags.LOGS, BlockTags.PLANKS);
    }


}
