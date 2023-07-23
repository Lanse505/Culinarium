package lanse505.culinarium.client;

import com.mojang.blaze3d.systems.RenderSystem;
import lanse505.culinarium.client.gui.BrewingBarrelScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import java.awt.*;

public class RenderUtil {

    public static void renderFluid(Screen screen, GuiGraphics graphics, int x, int y, int width, int maxHeight, IFluidTank tank) {
        if (!tank.getFluid().isEmpty()) {
            FluidStack stack = tank.getFluid();
            Fluid fluid = stack.getFluid();
            int fluidLevel = tank.getFluidAmount();
            int maxFluidLevel = tank.getCapacity();
            int fluidHeight = (int) ((float) fluidLevel / maxFluidLevel * maxHeight);
            int paddingTop = maxHeight - fluidHeight;
            IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
            ResourceLocation textureId = renderProperties.getStillTexture();
            if (textureId != null) {
                AbstractTexture texture = screen.getMinecraft().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS);
                if (texture instanceof TextureAtlas atlas) {
                    TextureAtlasSprite sprite = atlas.getSprite(textureId);
                    if (sprite != null) {
                        Color color = new Color(renderProperties.getTintColor(stack));
                        graphics.setColor(
                                color.getRed() / 255f,
                                color.getGreen() / 255f,
                                color.getBlue() / 255f,
                                color.getAlpha() / 255f
                        );
                        RenderSystem.enableBlend();
                        graphics.blit(
                                x, y + paddingTop,
                                0,
                                width, fluidHeight,
                                sprite
                        );
                        RenderSystem.disableBlend();
                        graphics.setColor(1, 1, 1, 1);
                    }
                }
            }
        }
    }

    public static void renderFluidTankBar(GuiGraphics graphics, int x, int y) {
        graphics.blit(BrewingBarrelScreen.BREWING_LOCATION, x, y, 193, 3, 9, 18);
    }
}
