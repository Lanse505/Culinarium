package lanse505.culinarium.data.provider.blockstate;

import com.mojang.math.Axis;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.block.base.CulinariumRotatableBlock;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.CropBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CulinariumBlockStateProvider extends BlockStateProvider {

    public CulinariumBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Culinarium.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(CulinariumBlockRegistry.RYE.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().withExistingParent(
                                String.format("block/crop/rye_stage%d", state.getValue(CropBlock.AGE)),
                                new ResourceLocation("minecraft", "block/crop"))
                        .renderType("cutout")
                        .texture("crop", modLoc(String.format("block/crop/rye_stage%d", state.getValue(CropBlock.AGE))))
                ).build());
        axisBlock(CulinariumBlockRegistry.STRAW_BALE.get(), new ResourceLocation("minecraft", "block/hay_block_side"), new ResourceLocation("minecraft", "block/hay_block_top"));
    }

}
