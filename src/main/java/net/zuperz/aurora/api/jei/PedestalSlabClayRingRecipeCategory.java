package net.zuperz.aurora.api.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
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
import net.zuperz.aurora.Recipes.PedestalSlabClayRingRecipe;
import net.zuperz.aurora.Recipes.PedestalSlabClayRingRecipe;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.item.ModItems;
import org.jetbrains.annotations.NotNull;

public class PedestalSlabClayRingRecipeCategory implements IRecipeCategory<PedestalSlabClayRingRecipe> {
    public final static ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "pedestal_slab_clay_ring");
    public static final RecipeType<PedestalSlabClayRingRecipe> RECIPE_TYPE = RecipeType.create(aurora.MOD_ID, "pedestal_slab_clay_ring", PedestalSlabClayRingRecipe.class);
    public final static ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/16_slot.png");
    public final static ResourceLocation BLOCK = ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "textures/gui/magi_clay_slab_ring.png");
    private final IDrawable background;
    private final IDrawable icon;

    private final IDrawableStatic Block;

    private final IDrawableStatic slot_1_Ring;

    public PedestalSlabClayRingRecipeCategory(IGuiHelper helper) {

        this.background = helper.createBlankDrawable(100, 60);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.CLAY_DUST.get()));

        this.slot_1_Ring = helper.drawableBuilder(SLOT, 0, 18, 18, 18).setTextureSize(18, 18).addPadding(31,0,41,0).build();

        this.Block = helper.drawableBuilder(BLOCK, 0, 0, 100, 60).setTextureSize(100, 60).addPadding(0,0,0,0).build();
    }

    @Override
    public RecipeType<PedestalSlabClayRingRecipe> getRecipeType() {
        return JEIPlugin.PEDESTAL_SLAB_CLAY_RING_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Pedestal Slab Clay Ring 5x5");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public void draw(PedestalSlabClayRingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX,
                     double mouseY) {

        this.Block.draw(guiGraphics);
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, PedestalSlabClayRingRecipe recipe, @NotNull IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 42, 9)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(RecipeIngredientRole.INPUT, 42, 33)
                .addIngredients(recipe.getIngredients().get(1));
    }
}