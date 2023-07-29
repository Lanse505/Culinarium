package lanse505.culinarium.client.gui;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.client.RenderUtil;
import lanse505.culinarium.client.widget.BarrelSealingButton;
import lanse505.culinarium.common.menu.BrewingBarrelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.ProgressView;
import xyz.brassgoggledcoders.shadyskies.containersyncing.object.TankView;

public class BrewingBarrelScreen extends BaseBarrelScreen<BrewingBarrelMenu> {

  public static final ResourceLocation BREWING_LOCATION = new ResourceLocation(Culinarium.MODID, "textures/gui/container/brewing_barrel.png");
  private static int[] BUBBLELENGTHS = new int[]{0, 6, 11, 16, 20, 24, 29};

  public BrewingBarrelScreen(BrewingBarrelMenu menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    imageWidth = 176;
    imageHeight = 216;
  }

  @Override
  protected void init() {
    super.init();
    this.addRenderableWidget(new BarrelSealingButton(
            this,
            getGuiLeft() + imageWidth, getGuiTop(),
            30, 30,
            75, 30,
            false
    ));
  }

  @Override
  public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
    super.render(graphics, mouseX, mouseY, partial);
    renderTanks(graphics);
    renderProgress(graphics);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.blit(
            BREWING_LOCATION,
            this.leftPos, this.topPos - 12,
            0, 0,
            176, 216
    );
  }

  private void renderTanks(GuiGraphics graphics) {
    renderTank(true, graphics, this.menu.getBrewable().getOrElse(TankView.NULL));
    renderTank(false, graphics, this.menu.getBrewed().getOrElse(TankView.NULL));
  }

  private void renderTank(boolean isTop, GuiGraphics graphics, TankView tank) {
    if (isTop) {
      RenderUtil.renderFluid(this, graphics, this.leftPos + 8, this.topPos + 8, 160, 32, tank);
      RenderUtil.renderFluidTankBar(graphics, this.leftPos + 8, this.topPos + 3);
      RenderUtil.renderFluidTankBar(graphics, this.leftPos + 160, this.topPos + 3);
    } else {
      RenderUtil.renderFluid(this, graphics, this.leftPos + 8, this.topPos + 92, 160, 32, tank);
      RenderUtil.renderFluidTankBar(graphics, this.leftPos + 8, this.topPos + 88);
      RenderUtil.renderFluidTankBar(graphics, this.leftPos + 160, this.topPos + 88);
    }
  }

  private void renderProgress(GuiGraphics guiGraphics) {
    ProgressView progress = this.menu.getProgress().getOrElse(ProgressView.NULL);
    int elapsed = progress.current();
    if (elapsed > 0) {
      int stage = (int) (7.0F * (1.0F - (float) elapsed / (float) progress.max()));
//            if (stage > 0) {
//                guiGraphics.blit(BREWING_LOCATION, centeredX + 97, centeredY + 16, 178, 2, 9, stage);
//            }
      stage = BUBBLELENGTHS[stage];
      if (stage > 0) {
        guiGraphics.blit(BREWING_LOCATION, getGuiLeft() + 63, getGuiTop() + 14 + (29 - stage), 185, (29 - stage), 12, stage);
      }
    }
  }
}
