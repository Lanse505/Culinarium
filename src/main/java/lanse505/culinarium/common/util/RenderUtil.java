package lanse505.culinarium.common.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

public class RenderUtil {

    public static void renderBEModelWithTesselatedAO(BlockEntityRendererProvider.Context context,
                                                     BlockEntity blockEntity,
                                                     BakedModel model, BlockState state,
                                                     PoseStack stack, VertexConsumer buffer, boolean checkSides,
                                                     ModelData data, RenderType renderType) {
        context.getBlockRenderDispatcher()
                .getModelRenderer()
                .tesselateWithAO(
                        blockEntity.getLevel(),
                        model,
                        state,
                        blockEntity.getBlockPos(),
                        stack,
                        buffer,
                        checkSides,
                        RandomSource.create(),
                        blockEntity.getBlockPos().asLong(),
                        OverlayTexture.NO_OVERLAY,
                        data,
                        renderType
                );
    }

}
