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
import net.zuperz.aurora.Recipes.MyBlockRecipe;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class MyBlockRecipeCategory implements IRecipeCategory<MyBlockRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "my_recipe");
    public final static ResourceLocation ARROWBACK = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/null_arrow.png");
    public static final RecipeType<MyBlockRecipe> RECIPE_TYPE = RecipeType.create(aurora.MOD_ID, "my_recipe", MyBlockRecipe.class);
    public final static ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/slot.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated progress;

    private final IDrawableStatic slot_1;
    private final IDrawableStatic slot_2;

    private final IDrawableStatic arrowbacki;

    public MyBlockRecipeCategory(IGuiHelper helper) {
        ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/arrow.png");
        this.background = helper.createBlankDrawable(100, 30);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.MY_BLOCK.get()));

        IDrawableStatic progressDrawable = helper.drawableBuilder(ARROW, 0, 0, 23, 15).setTextureSize(23, 15).addPadding(7, 0, 40, 0).build();

        this.arrowbacki = helper.drawableBuilder(ARROWBACK, 0, 0, 23, 15).setTextureSize(23, 15).addPadding(7, 0, 39, 0).build();

        this.slot_1 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(6,0,12,0).build();
        this.slot_2 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(6,0,70,0).build();

        this.progress = helper.createAnimatedDrawable(progressDrawable, 300, IDrawableAnimated.StartDirection.LEFT,
                false);
    }

    @Override
    public RecipeType<MyBlockRecipe> getRecipeType() {
        return JEIPlugin.MY_BLOCK_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("My Block");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void draw(MyBlockRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
                     double mouseY) {

        this.slot_1.draw(guiGraphics);
        this.slot_2.draw(guiGraphics);

        this.arrowbacki.draw(guiGraphics);
        this.progress.draw(guiGraphics);
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, MyBlockRecipe recipe, @NotNull IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 13, 7)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 71, 7).addItemStack(recipe.output);
    }
}