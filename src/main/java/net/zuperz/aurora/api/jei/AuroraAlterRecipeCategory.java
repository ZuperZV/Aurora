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
import net.zuperz.aurora.Recipes.AlterRecipe;
import net.zuperz.aurora.Recipes.AuroraAlterRecipe;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.ModItems;
import org.jetbrains.annotations.NotNull;

public class AuroraAlterRecipeCategory implements IRecipeCategory<AuroraAlterRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "aurora_alter");
    public static final RecipeType<AuroraAlterRecipe> RECIPE_TYPE = RecipeType.create(aurora.MOD_ID, "aurora_alter", AuroraAlterRecipe.class);
    public final static ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/slot.png");
    public final static ResourceLocation ARROWBACK = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/null_magi_slab.png");
    private final IDrawable background;
    private final IDrawable icon1;
    private final IDrawable icon2;

    private final IDrawableStatic slot_1;
    private final IDrawableStatic slot_2;
    private final IDrawableStatic slot_3;
    private final IDrawableStatic slot_4;
    private final IDrawableStatic slot_5;
    private final IDrawableStatic slot_6;

    private final IDrawableStatic arrowbacki;
    private final IDrawableAnimated progress;

    public AuroraAlterRecipeCategory(IGuiHelper helper) {
        ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/magi_slab.png");

        this.background = helper.createBlankDrawable(100, 60);
        this.icon1 = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ALTER.get()));
        this.icon2 = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.AURORA_DUST.get()));

        IDrawableStatic progressDrawable = helper.drawableBuilder(ARROW, 0, 0, 100, 60).setTextureSize(100, 60).addPadding(0, 0, 0, 0).build();

        this.arrowbacki = helper.drawableBuilder(ARROWBACK, 0, 0, 100, 60).setTextureSize(100, 60).addPadding(0, 0, 0, 0).build();

        this.progress = helper.createAnimatedDrawable(progressDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM,
                false);

        this.slot_1 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(12,0,7,0).build();
        this.slot_2 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(12,0,75,0).build();
        this.slot_3 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(34,0,15,0).build();
        this.slot_4 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(34,0,67,0).build();
        this.slot_5 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(32,0,41,0).build();
        this.slot_6 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(12,0,7,0).build();
    }

    @Override
    public RecipeType<AuroraAlterRecipe> getRecipeType() {
        return JEIPlugin.AURORA_ALTER_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Aurora Alter");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void draw(AuroraAlterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
                     double mouseY) {
        this.arrowbacki.draw(guiGraphics);
        this.progress.draw(guiGraphics);

        this.slot_1.draw(guiGraphics);
        this.slot_2.draw(guiGraphics);
        this.slot_3.draw(guiGraphics);
        this.slot_4.draw(guiGraphics);
        this.slot_5.draw(guiGraphics);
        this.slot_6.draw(guiGraphics);
    }

    @Override
    public IDrawable getIcon() {
        return new CompositeDrawable(icon1, icon2);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, AuroraAlterRecipe recipe, @NotNull IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 8, 13)
                .addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 76, 13)
                .addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 35)
                .addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 68, 35)
                .addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 42, 33)
                .addIngredients(recipe.getIngredients().get(4));

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

            icon2.draw(guiGraphics, icon2X +5, icon2Y +3);

            icon1.draw(guiGraphics, x -2, y-2);
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