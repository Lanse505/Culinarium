package lanse505.culinarium.client.widget;

import lanse505.culinarium.Culinarium;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseExtenderWidget extends Button {

  public static final ResourceLocation GUI_ASSETS = new ResourceLocation(Culinarium.MODID, "textures/gui/container/test_assets.png");

  private final String identifier;
  protected boolean isLeftAligned;
  protected boolean isExtended;
  protected int unexpandedWidth;
  protected int unexpandedHeight;
  protected int expandedWidth;
  protected int expandedHeight;

  protected List<AbstractWidget> children;

  public BaseExtenderWidget(int x, int y,
                            int width, int height, int expandedWidth, int expandedHeight,
                            String identifier, boolean isLeftAligned) {
    super(x, y, width, height, Component.translatable("widget.extendable." + identifier), btn -> {
    }, DEFAULT_NARRATION);
    this.identifier = identifier;
    this.isLeftAligned = isLeftAligned;
    this.unexpandedWidth = width;
    this.unexpandedHeight = height;
    this.expandedWidth = expandedWidth;
    this.expandedHeight = expandedHeight;
    this.children = new ArrayList<>();
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    if (isLeftAligned) {
      guiGraphics.blitNineSliced(
              GUI_ASSETS,
              getX(), getY(),
              15, 15,
              5, 5, 5, 5,
              15, 15,
              0, 33
      );
    } else {
      guiGraphics.blitNineSliced(
              GUI_ASSETS,
              getX(), getY(),
              width, height,
              5, 5, 5, 5,
              15, 15,
              0, 17
      );
    }
  }

  protected abstract void renderContent(int x, int y, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

  protected abstract void renderExpandedContent(int x, int y, GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

  @Override
  public void onClick(double mouseX, double mouseY) {
    this.onPress();
    if (isMouseOver(mouseX, mouseY)) {
      this.isExtended = !this.isExtended;
      if (this.width == this.unexpandedHeight) {
        this.width = this.expandedWidth;
      } else {
        this.width = unexpandedWidth;
      }
      if (this.height == this.unexpandedHeight) {
        this.height = this.expandedHeight;
      } else {
        this.height = this.unexpandedHeight;
      }
    }
  }

  @Override
  public boolean isMouseOver(double mouseX, double mouseY) {
    return mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
  }

  @Nullable
  @Override
  public Tooltip getTooltip() {
    return Tooltip.create(Component.translatable("widget.extendable." + identifier));
  }
}
