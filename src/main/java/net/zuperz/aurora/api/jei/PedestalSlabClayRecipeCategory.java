package net.zuperz.aurora.api.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.zuperz.aurora.Recipes.PedestalSlabClayRecipe;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.ModItems;
import org.jetbrains.annotations.NotNull;

public class PedestalSlabClayRecipeCategory implements IRecipeCategory<PedestalSlabClayRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "pedestal_slab_clay");
    public static final RecipeType<PedestalSlabClayRecipe> RECIPE_TYPE = RecipeType.create(aurora.MOD_ID, "pedestal_slab_clay", PedestalSlabClayRecipe.class);
    public final static ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/slot.png");
    public final static ResourceLocation BLOCK = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/pedestal_slab_block.png");
    public final static ResourceLocation ARROWBACK = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/null_magi_slab.png");
    private final IDrawable background;
    private final IDrawable icon1;
    private final IDrawable icon2;

    private final IDrawableStatic Block;

    private final IDrawableStatic slot_1;
    private final IDrawableStatic slot_2;

    private final IDrawableStatic arrowbacki;
    private final IDrawableAnimated progress;

    public PedestalSlabClayRecipeCategory(IGuiHelper helper) {
        ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/magi_slab.png");

        this.background = helper.createBlankDrawable(100, 60);
        this.icon1 = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.PEDESTAL_SLAB.get()));
        this.icon2 = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.CLAY_DUST.get()));

        IDrawableStatic progressDrawable = helper.drawableBuilder(ARROW, 0, 0, 100, 60).setTextureSize(100, 60).addPadding(0, 0, 0, 0).build();

        this.arrowbacki = helper.drawableBuilder(ARROWBACK, 0, 0, 100, 60).setTextureSize(100, 60).addPadding(0, 0, 0, 0).build();

        this.slot_1 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(8,0,41,0).build();
        this.slot_2 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(32,0,41,0).build();

        this.Block = helper.drawableBuilder(BLOCK, 0, 0, 46, 28).setTextureSize(46, 28).addPadding(29,0,27,0).build();

        this.progress = helper.createAnimatedDrawable(progressDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM,
                false);
    }

    @Override
    public RecipeType<PedestalSlabClayRecipe> getRecipeType() {
        return JEIPlugin.PEDESTAL_SLAB_CLAY_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Pedestal Slab Clay 5x5");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void draw(PedestalSlabClayRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
                     double mouseY) {

        this.Block.draw(guiGraphics);

        this.slot_1.draw(guiGraphics);
        this.slot_2.draw(guiGraphics);

        this.arrowbacki.draw(guiGraphics);
        this.progress.draw(guiGraphics);
    }

    @Override
    public IDrawable getIcon() {
        return new CompositeDrawable(icon1, icon2);
    }


    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, PedestalSlabClayRecipe recipe, @NotNull IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 42, 33)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 42, 9).addItemStack(recipe.output);
    }

    public class CompositeDrawable implements IDrawable {
        private final IDrawable icon1;
        private final IDrawable icon2;

        public CompositeDrawable(IDrawable icon1, IDrawable icon2) {
            this.icon1 = icon1;
            this.icon2 = icon2;
        }

        @Override
        public void draw(GuiGraphics guiGraphics, int x, int y) {
            int icon2X = x;
            int icon2Y = y + icon1.getHeight() - icon2.getHeight();

            icon2.draw(guiGraphics, icon2X, icon2Y -3);

            icon1.draw(guiGraphics, x, y +1);
        }

        @Override
        public int getWidth() {
            return icon1.getWidth();
        }

        @Override
        public int getHeight() {
            return icon1.getHeight();
        }
    }
}