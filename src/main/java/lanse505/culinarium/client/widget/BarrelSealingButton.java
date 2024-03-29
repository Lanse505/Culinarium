package lanse505.culinarium.client.widget;

import lanse505.culinarium.client.gui.BaseBarrelScreen;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class BarrelSealingButton extends BaseExtenderWidget {

  private final BaseBarrelScreen<?> screen;
  private final Button sealBtn;
  private final Button unsealBtn;

  public BarrelSealingButton(BaseBarrelScreen<?> screen, int x, int y, int width, int height, int expanded_width, int expanded_height, boolean isLeftAligned) {
    super(x, y, width, height, expanded_width, expanded_height, "barrel.sealer", isLeftAligned);
    this.screen = screen;
    this.sealBtn = Button.builder(
            Component.translatable("widget.extendable.barrel.seal"),
            btn -> screen.getMenu().toggleSealed()
    ).bounds(x + 6, y + 4, 62, 20).build();
    this.unsealBtn = Button.builder(
            Component.translatable("widget.extendable.barrel.unseal"),
            btn -> screen.getMenu().toggleSealed()
    ).bounds(x + 6, y + 4, 62, 20).build();
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    if (!this.isExtended) {
      renderContent(getX(), getY(), guiGraphics, mouseX, mouseY, partialTick);
    } else {
      renderExpandedContent(getX(), getY(), guiGraphics, mouseX, mouseY, partialTick);
    }
  }

  @Override
  protected void renderContent(int x, int y, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    guiGraphics.renderItem(new ItemStack(CulinariumBlockRegistry.BREWING_BARREL.asItem(), 1), x + 5, y + 5);
  }

  @Override
  protected void renderExpandedContent(int x, int y, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    if (screen.isBarrelSealed()) {
      unsealBtn.render(guiGraphics, mouseX, mouseY, partialTick);
    } else {
      sealBtn.render(guiGraphics, mouseX, mouseY, partialTick);
    }
  }

  @Override
  public void onClick(double mouseX, double mouseY) {
    if (isExtended && screen.isBarrelSealed() && unsealBtn.isMouseOver(mouseX, mouseY)) {
      unsealBtn.onClick(mouseX, mouseY);
      return;
    } else if (isExtended && sealBtn.isMouseOver(mouseX, mouseY)) {
      sealBtn.onClick(mouseX, mouseY);
      return;
    }
    super.onClick(mouseX, mouseY);
  }
}
