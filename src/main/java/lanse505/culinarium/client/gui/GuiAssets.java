package lanse505.culinarium.client.gui;

import lanse505.culinarium.Culinarium;
import net.minecraft.resources.ResourceLocation;

public class GuiAssets {

  public static final ResourceLocation DEFAULT_ASSETS = new ResourceLocation(Culinarium.MODID, "gui_assets");

  // Progress
  public static final GuiAsset PROGRESS_BACKGROUND_SMELTING = new GuiAsset(DEFAULT_ASSETS, 0, 16, 13, 13);
  public static final GuiAsset PROGRESS_FOREGROUND_SMELTING = new GuiAsset(DEFAULT_ASSETS, 0, 30, 14, 14);
  public static final GuiAsset PROGRESS_BACKGROUND_H_ARROW = new GuiAsset(DEFAULT_ASSETS, 16, 0, 22, 15);
  public static final GuiAsset PROGRESS_FOREGROUND_H_ARROW = new GuiAsset(DEFAULT_ASSETS, 39, 0, 22, 16);
  public static final GuiAsset PROGRESS_BACKGROUND_V_ARROW = new GuiAsset(DEFAULT_ASSETS, 46, 18, 7, 16);
  public static final GuiAsset PROGRESS_FOREGROUND_V_ARROW = new GuiAsset(DEFAULT_ASSETS, 54, 18, 7, 27);
  public static final GuiAsset PROGRESS_BACKGROUND_BUBBLES = new GuiAsset(DEFAULT_ASSETS, 23, 16, 10, 27);
  public static final GuiAsset PROGRESS_FOREGROUND_BUBBLES = new GuiAsset(DEFAULT_ASSETS, 34, 16, 11, 28);

  public record GuiAsset(ResourceLocation texture, int textureX, int textureY, int width, int height) {
  }
}
