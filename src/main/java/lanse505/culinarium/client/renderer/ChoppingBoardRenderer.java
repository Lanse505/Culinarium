package lanse505.culinarium.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import lanse505.culinarium.common.block.base.CulinariumRotatableBlock;
import lanse505.culinarium.common.block.impl.tile.ChoppingBoardTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ChoppingBoardRenderer implements BlockEntityRenderer<ChoppingBoardTile> {

  private final BlockEntityRendererProvider.Context context;

  public ChoppingBoardRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
  }

  // TODO: Make it render "slices" of the actual texture, so it looks like it's being chopped.
  @Override
  public void render(ChoppingBoardTile choppingBoard, float partial, PoseStack stack, MultiBufferSource source, int light, int overlay) {
    ItemStack choppable = choppingBoard.getInventory().getStackInSlot(0);
    ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();

    int i = choppable.isEmpty() ? 187 : Item.getId(choppable.getItem()) + choppable.getDamageValue();
    stack.pushPose();
    stack.translate(0.5, 0.25, 0.5);

    Direction direction = choppingBoard.getBlockState().getValue(CulinariumRotatableBlock.FACING_HORIZONTAL);
    switch (direction) {
      case NORTH, WEST -> {
        stack.mulPose(Axis.XN.rotationDegrees(90));
      }
      case EAST, SOUTH -> {
        stack.mulPose(Axis.XP.rotationDegrees(90));
      }
    }

    switch (direction) {
      case NORTH -> {
        stack.translate(0.075, 0.085, -0.075);
        stack.mulPose(Axis.ZN.rotationDegrees(225));
      }
      case WEST -> {
        stack.translate(-0.075, 0.085, -0.075);
        stack.mulPose(Axis.ZN.rotationDegrees(135));
      }
      case EAST -> {
        stack.translate(0.075, -0.085, 0.075);
        stack.mulPose(Axis.ZP.rotationDegrees(45));

      }
      case SOUTH -> {
        stack.translate(0.075, 0.085, 0.075);
        stack.mulPose(Axis.ZP.rotationDegrees(135));
      }
    }
    renderer.renderStatic(choppable, ItemDisplayContext.GROUND, LevelRenderer.getLightColor(choppingBoard.getLevel(), choppingBoard.getBlockPos()), OverlayTexture.NO_OVERLAY, stack, source, choppingBoard.getLevel(), i);
    stack.popPose();

  }
}
