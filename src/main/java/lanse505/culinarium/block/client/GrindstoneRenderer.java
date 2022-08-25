package lanse505.culinarium.block.client;

import com.hrznstudio.titanium.block.RotatableBlock;
import com.hrznstudio.titanium.util.FacingUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.block.tile.GrindstoneTile;
import lanse505.culinarium.register.CulinariumBlockRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.client.model.data.ModelData;

public class GrindstoneRenderer implements BlockEntityRenderer<GrindstoneTile> {

  private final BlockEntityRendererProvider.Context context;
  private final BakedModel model;

  public GrindstoneRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
    this.model = context.getBlockRenderDispatcher().getBlockModelShaper().getModelManager().getModel(new ResourceLocation(Culinarium.MODID, "block/grindstone_top"));
  }

  @Override
  public void render(GrindstoneTile grindstone, float partial, PoseStack stack, MultiBufferSource source, int packedLight, int packedOverlay) {
    ItemStack grindable = grindstone.inventory.getStackInSlot(0);
    ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

    int i = grindable.isEmpty() ? 187 : Item.getId(grindable.getItem()) + grindable.getDamageValue();
    stack.translate(0.5, 0.325, 0.5);

    stack.pushPose();
    renderer.renderStatic(grindable, ItemTransforms.TransformType.GROUND, LevelRenderer.getLightColor(grindstone.getLevel(), grindstone.getBlockPos()), OverlayTexture.NO_OVERLAY, stack, source, i);
    stack.popPose();

    stack.pushPose();

    Direction direction = grindstone.getBlockState().getValue(RotatableBlock.FACING_HORIZONTAL);
    switch (direction) {
      case NORTH -> stack.mulPose(Vector3f.YN.rotationDegrees(90));
      case EAST -> stack.mulPose(Vector3f.YN.rotationDegrees(180));
      case SOUTH -> stack.mulPose(Vector3f.YP.rotationDegrees(90));
      case WEST -> {}
    }

    if (grindstone.isGrinding) {
      stack.mulPose(Vector3f.YP.rotationDegrees(360f * ((float) grindstone.duration / (float) GrindstoneTile.DEFAULT_DURATION)));
    }

    stack.translate(-0.5, 0, -0.5);
    context.getBlockRenderDispatcher()
            .getModelRenderer()
            .tesselateWithAO(
                    grindstone.getLevel(),
                    model,
                    CulinariumBlockRegistry.GRINDSTONE.get()
                            .defaultBlockState()
                            .setValue(RotatableBlock.FACING_HORIZONTAL, grindstone.getBlockState().getValue(RotatableBlock.FACING_HORIZONTAL)),
                    grindstone.getBlockPos(),
                    stack,
                    source.getBuffer(RenderType.cutout()),
                    false,
                    RandomSource.create(),
                    grindstone.getBlockPos().asLong(),
                    OverlayTexture.NO_OVERLAY,
                    ModelData.EMPTY,
                    RenderType.cutout()
            );
//    context.getBlockRenderDispatcher()
//            .getModelRenderer()
//            .renderModel(
//                    stack.last(), source.getBuffer(RenderType.cutout()), null, model,
//                    1f, 1f, 1f,
//                    packedLight, packedOverlay, ModelData.EMPTY, RenderType.cutout());
    stack.popPose();
  }
}
