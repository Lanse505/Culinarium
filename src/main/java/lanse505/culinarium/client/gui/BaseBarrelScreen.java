package lanse505.culinarium.client.gui;

import lanse505.culinarium.client.widget.BarrelSealingButton;
import lanse505.culinarium.common.menu.base.BaseBarrelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class BaseBarrelScreen<T extends BaseBarrelMenu> extends AbstractContainerScreen<T> {

    public BaseBarrelScreen(T menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        addWidget(
                new BarrelSealingButton(
                        this,
                        getGuiLeft(), getGuiTop(),
                        20, 20,
                        60, 60,
                        true)
        );
    }

    public boolean isBarrelSealed() {
        return menu.isSealed();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {}
}
