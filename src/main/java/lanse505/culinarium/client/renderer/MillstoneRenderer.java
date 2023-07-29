package lanse505.culinarium.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.block.base.CulinariumRotatableBlock;
import lanse505.culinarium.common.block.impl.tile.MillstoneTile;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.model.data.ModelData;

public class MillstoneRenderer implements BlockEntityRenderer<MillstoneTile> {

  private final BlockEntityRendererProvider.Context context;
  private final BakedModel model;

  public MillstoneRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
    this.model = context.getBlockRenderDispatcher().getBlockModelShaper().getModelManager().getModel(new ResourceLocation(Culinarium.MODID, "block/millstone_top"));
  }


  @Override
  public void render(MillstoneTile millstone, float partial, PoseStack stack, MultiBufferSource source, int packedLight, int packedOverlay) {
    ItemStack millable = millstone.getInventory().getStackInSlot(0);
    ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

    int i = millable.isEmpty() ? 187 : Item.getId(millable.getItem()) + millable.getDamageValue();
    stack.pushPose();
    stack.translate(0.5, 0.325, 0.5);
    renderer.renderStatic(millable, ItemDisplayContext.GROUND, LevelRenderer.getLightColor(millstone.getLevel(), millstone.getBlockPos()), OverlayTexture.NO_OVERLAY, stack, source, millstone.getLevel(), i);
    stack.popPose();

    Direction direction = millstone.getBlockState().getValue(CulinariumRotatableBlock.FACING_HORIZONTAL);

    stack.pushPose();
    stack.translate(0.5, 0.325, 0.5);
    switch (direction) {
      case NORTH -> stack.mulPose(Axis.YN.rotationDegrees(90));
      case EAST -> stack.mulPose(Axis.YN.rotationDegrees(180));
      case SOUTH -> stack.mulPose(Axis.YP.rotationDegrees(90));
      case WEST -> {
      }
    }

    if (millstone.activeRecipe != null)
      stack.mulPose(Axis.YP.rotationDegrees(360f * ((float) millstone.duration / (float) millstone.activeRecipe.getDuration())));
    stack.translate(-0.5, 0, -0.5);
    RenderUtil.renderBEModelWithTesselatedAO(
            context,
            millstone,
            model,
            CulinariumBlockRegistry.MILLSTONE.getBlock()
                    .defaultBlockState()
                    .setValue(CulinariumRotatableBlock.FACING_HORIZONTAL, millstone.getBlockState().getValue(CulinariumRotatableBlock.FACING_HORIZONTAL)),
            stack,
            source.getBuffer(RenderType.cutout()),
            false,
            ModelData.EMPTY,
            RenderType.cutout());
    stack.popPose();
  }
}
