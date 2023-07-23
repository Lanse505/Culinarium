package lanse505.culinarium.client.gui;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.client.RenderUtil;
import lanse505.culinarium.common.container.BrewingBarrelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.fluids.IFluidTank;

public class BrewingBarrelScreen extends AbstractContainerScreen<BrewingBarrelMenu> {

    public static final ResourceLocation BREWING_LOCATION = new ResourceLocation(Culinarium.MODID, "textures/gui/container/brewing_barrel.png");
    private static int[] BUBBLELENGTHS = new int[]{0, 6, 11, 16, 20, 24, 29};

    private int centeredX;
    private int centeredY;

    public BrewingBarrelScreen(BrewingBarrelMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 216;
        centeredX = this.leftPos - this.imageWidth / 2;
        centeredY = this.height + this.imageHeight / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        renderBg(graphics, partial, mouseX, mouseY);
        super.render(graphics, mouseX, mouseY, partial);
        renderTanks(graphics);
        renderProgress(graphics);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        guiGraphics.blit(
                BREWING_LOCATION,
                this.leftPos, centeredY,
                0, 0,
                176, 216
        );
    }

    private void renderTanks(GuiGraphics graphics) {
        renderTank(true, graphics, menu.getBrewable());
        renderTank(false, graphics, menu.getBrewed());
    }

    private void renderTank(boolean isTop, GuiGraphics graphics, IFluidTank tank) {
        if (isTop) {
            RenderUtil.renderFluid(this, graphics, this.leftPos + 8, centeredY + 8, 160, 32, tank);
            RenderUtil.renderFluidTankBar(graphics, this.leftPos + 8, centeredY + 16);
            RenderUtil.renderFluidTankBar(graphics, this.leftPos + 160, centeredY + 16);
        } else {
            RenderUtil.renderFluid(this, graphics, this.leftPos + 8, centeredY + 92, 160, 32, tank);
            RenderUtil.renderFluidTankBar(graphics, this.leftPos + 8, centeredY + 100);
            RenderUtil.renderFluidTankBar(graphics, this.leftPos + 160, centeredY + 100);
        }
    }

    private void renderProgress(GuiGraphics guiGraphics) {
        int elapsed = this.menu.getTicks();
        if (elapsed > 0) {
            int stage = (int)(28.0F * (1.0F - (float)elapsed / (float) this.menu.getMaxTicks()));
//            if (stage > 0) {
//                guiGraphics.blit(BREWING_LOCATION, centeredX + 97, centeredY + 16, 178, 2, 9, stage);
//            }
            stage = BUBBLELENGTHS[stage];
            if (stage > 0) {
                guiGraphics.blit(BREWING_LOCATION, centeredX + 63, centeredY + 14 + 29 - stage, 185, 29 - stage, 12, stage);
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}
}
