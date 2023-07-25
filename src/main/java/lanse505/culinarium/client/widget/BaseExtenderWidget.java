package lanse505.culinarium.client.widget;

import lanse505.culinarium.Culinarium;
import net.minecraft.client.gui.Font;
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
    protected int unexpanded_width;
    protected int unexpanded_height;
    protected int expanded_width;
    protected int expanded_height;

    protected List<AbstractWidget> children;

    public BaseExtenderWidget(int x, int y,
                              int width, int height, int expanded_width, int expanded_height,
                              String identifier, boolean isLeftAligned) {
        super(x, y, width, height, Component.translatable("widget.extendable." + identifier), btn -> {}, DEFAULT_NARRATION);
        this.identifier = identifier;
        this.isLeftAligned = isLeftAligned;
        this.unexpanded_width = width;
        this.unexpanded_height = height;
        this.expanded_width = expanded_width;
        this.expanded_height = expanded_height;
        this.children = new ArrayList<>();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (isLeftAligned) {
            guiGraphics.blitNineSliced(
                    GUI_ASSETS,
                    getX(), getY(),
                    15, 15,
                    5, 5, 5,5,
                    15, 15,
                    0, 33
            );
        } else {
            guiGraphics.blitNineSliced(
                    GUI_ASSETS,
                    getX(), getY(),
                    width, height,
                    5, 5, 5,5,
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
            if (this.width == this.unexpanded_height) {
                this.width = this.expanded_width;
            } else {
                this.width = unexpanded_width;
            }
            if (this.height == this.unexpanded_height) {
                this.height = this.expanded_height;
            } else {
                this.height = this.unexpanded_height;
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
