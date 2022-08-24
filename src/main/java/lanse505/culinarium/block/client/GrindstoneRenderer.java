package lanse505.culinarium.block.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import lanse505.culinarium.Culinarium;
import lanse505.culinarium.block.tile.GrindstoneTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class GrindstoneRenderer implements BlockEntityRenderer<GrindstoneTile> {

  private final BlockEntityRendererProvider.Context context;
  private final BakedModel model;
  private final RandomSource random;

  public GrindstoneRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
    this.model = context.getBlockRenderDispatcher().getBlockModelShaper().getModelManager().getModel(new ResourceLocation(Culinarium.MODID, "block/grindstone_top"));
    this.random = RandomSource.create();
  }

  @Override
  public void render(GrindstoneTile grindstone, float partial, PoseStack stack, MultiBufferSource source, int packedLight, int packedOverlay) {
    ItemStack grindable = grindstone.inventory.getStackInSlot(0);
    ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

    int i = grindable.isEmpty() ? 187 : Item.getId(grindable.getItem()) + grindable.getDamageValue();
    random.setSeed(i);
    stack.translate(0.5, 0.325, 0.5);

    stack.pushPose();
    renderer.renderStatic(grindable, ItemTransforms.TransformType.GROUND, LevelRenderer.getLightColor(grindstone.getLevel(), grindstone.getBlockPos()), OverlayTexture.NO_OVERLAY, stack, source, i);
    stack.popPose();

    stack.pushPose();
    stack.mulPose(new Quaternion(0, 360f * ((float) grindstone.duration / (float) GrindstoneTile.DEFAULT_DURATION), 0, true));
    context.getBlockRenderDispatcher()
            .getModelRenderer()
            .renderModel(
                    stack.last(), source.getBuffer(RenderType.cutout()), null, model,
                    1f, 1f, 1f,
                    packedLight, packedOverlay);
    stack.popPose();

    if (grindstone.duration == GrindstoneTile.DEFAULT_DURATION) {
      grindstone.finishGrindingSpin();
      grindstone.duration = 0;
    }
  }

}
