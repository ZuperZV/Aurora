package net.zuperz.aurora.item.custom.decorator;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import net.zuperz.aurora.item.custom.StoneSkullTwig;

public class NumberBarDecorator implements IItemDecorator {
    @Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        return false;
    }

    /*@Override
    public boolean render(GuiGraphics guiGraphics, Font font, ItemStack stack, int xOffset, int yOffset) {
        StoneSkullTwig twig = (StoneSkullTwig) stack.getItem();
        int numberValue = twig.getValue(stack);

        boolean isBarVisible = numberValue > 0;

        if (!isBarVisible) {
            return false;
        }

        int barY = yOffset + 13;
        int barWidth = numberValue;
        int barColor = 0xFb6c2c2;

        renderBar(guiGraphics, xOffset + 2, barY, barWidth, barColor);

        return true;
    }

    private void renderBar(GuiGraphics guiGraphics, int x, int y, int width, int color) {
        guiGraphics.fill(RenderType.guiOverlay(), x, y, x + 13, y + 2, 0xFF303030);

        guiGraphics.fill(RenderType.guiOverlay(), x, y, x + width, y + 1, color | 0xFF000000);
    }
     */
}
