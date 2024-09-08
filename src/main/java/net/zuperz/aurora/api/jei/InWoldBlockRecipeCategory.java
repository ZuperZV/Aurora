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
import net.zuperz.aurora.Recipes.BlockInWoldRecipe;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import org.jetbrains.annotations.NotNull;

public class InWoldBlockRecipeCategory implements IRecipeCategory<BlockInWoldRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "in_world_block");
    public static final RecipeType<BlockInWoldRecipe> RECIPE_TYPE = RecipeType.create(aurora.MOD_ID, "in_world_block", BlockInWoldRecipe.class);
    public final static ResourceLocation ARROWBACK = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/null_arrow.png");
    public final static ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/slot.png");
    public final static ResourceLocation BLOCK_0 = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/gui_stone_block.png");
    public final static ResourceLocation BLOCK_1 = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/gui_dirt_block.png");
    public final static ResourceLocation BLOCK_2 = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/gui_grass_block.png");
    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableAnimated progress;
    private final IDrawableStatic arrowbacki;


    private final IDrawableStatic Block;
    private final IDrawableStatic Block_2;
    private final IDrawableStatic Block_3;

    private final IDrawableStatic slot_1;
    private final IDrawableStatic slot_2;
    private final IDrawableStatic slot_3;

    private final IDrawableStatic slot_4;

    public InWoldBlockRecipeCategory(IGuiHelper helper) {
        ResourceLocation ARROW = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/arrow.png");

        this.background = helper.createBlankDrawable(100, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.AURORA_PILLER.get()));

        IDrawableStatic progressDrawable = helper.drawableBuilder(ARROW, 0, 0, 23, 15).setTextureSize(23, 15).addPadding(22, 0, 55, 0).build();
        this.arrowbacki = helper.drawableBuilder(ARROWBACK, 0, 0, 23, 15).setTextureSize(23, 15).addPadding(22, 0, 54, 0).build();

        this.slot_1 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(2,0,36,0).build();
        this.slot_2 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(21,0,36,0).build();
        this.slot_3 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(40,0,36,0).build();

        this.slot_4 = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(21,0,80,0).build();

        this.Block = helper.drawableBuilder(BLOCK_0, 18, 18, 18, 18).setTextureSize(18, 18).addPadding(34,0,8,0).build();
        this.Block_2 = helper.drawableBuilder(BLOCK_1, 18, 18, 18, 18).setTextureSize(18, 18).addPadding(22,0,8,0).build();
        this.Block_3 = helper.drawableBuilder(BLOCK_2, 18, 18, 18, 18).setTextureSize(18, 18).addPadding(9,0,8,0).build();

        this.progress = helper.createAnimatedDrawable(progressDrawable, 200, IDrawableAnimated.StartDirection.LEFT,
                false);
    }

    @Override
    public RecipeType<BlockInWoldRecipe> getRecipeType() {
        return JEIPlugin.IND_WOLD_BLOCK_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Block In World Transition");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void draw(BlockInWoldRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
                     double mouseY) {

        this.Block.draw(guiGraphics);

        this.slot_1.draw(guiGraphics);
        this.slot_2.draw(guiGraphics);
        this.slot_3.draw(guiGraphics);

        this.slot_4.draw(guiGraphics);

        this.Block.draw(guiGraphics);
        this.Block_2.draw(guiGraphics);
        this.Block_3.draw(guiGraphics);

        this.arrowbacki.draw(guiGraphics);
        this.progress.draw(guiGraphics);
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, BlockInWoldRecipe recipe, @NotNull IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 37, 3)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.INPUT, 37, 22)
                .addIngredients(recipe.getIngredients().get(1));

        builder.addSlot(RecipeIngredientRole.INPUT, 37, 41)
                .addIngredients(recipe.getIngredients().get(2));

        builder.addSlot(RecipeIngredientRole.OUTPUT, 81, 22).addItemStack(recipe.output);
    }
}