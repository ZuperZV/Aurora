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
import net.zuperz.aurora.Recipes.ArcanePedestalRecipe;
import net.zuperz.aurora.Recipes.ArcanePedestalRecipe;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class ArcanePedestalRecipeCategory implements IRecipeCategory<ArcanePedestalRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "arcane_pedestal");
    public static final RecipeType<ArcanePedestalRecipe> RECIPE_TYPE = RecipeType.create(aurora.MOD_ID, "arcane_pedestal", ArcanePedestalRecipe.class);
    public final static ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/slot.png");
    public final static ResourceLocation ARROWBACK = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/null_magi_slab.png");
    private final IDrawable background;
    private final IDrawable icon1;

    private final IDrawableStatic slot_1;

    private final IDrawableStatic arrowbacki;
    private final IDrawableAnimated progress;

    public ArcanePedestalRecipeCategory(IGuiHelper helper) {
        ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/magi_slab.png");

        this.background = helper.createBlankDrawable(100, 60);

        this.icon1 = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.ARCANE_PEDESTAL.get()));

        IDrawableStatic progressDrawable = helper.drawableBuilder(ARROW, 0, 0, 100, 60).setTextureSize(100, 60).addPadding(0, 0, 0, 0).build();

        this.arrowbacki = helper.drawableBuilder(ARROWBACK, 0, 0, 100, 60).setTextureSize(100, 60).addPadding(0, 0, 0, 0).build();

        this.slot_1 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(32,0,41,0).build();

        this.progress = helper.createAnimatedDrawable(progressDrawable, 200, IDrawableAnimated.StartDirection.BOTTOM,
                false);

    }

    @Override
    public RecipeType<ArcanePedestalRecipe> getRecipeType() {
        return JEIPlugin.ARCANE_PEDESTAL_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Arcane Pedestal");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void draw(ArcanePedestalRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
                     double mouseY) {
        this.slot_1.draw(guiGraphics);

        this.arrowbacki.draw(guiGraphics);
        this.progress.draw(guiGraphics);

    }

    @Override
    public IDrawable getIcon() {
        return icon1;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, ArcanePedestalRecipe recipe, @NotNull IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 42, 33)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 42, 9).addItemStack(recipe.output);
    }
}