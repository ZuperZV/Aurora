package net.zuperz.aurora.Recipes;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.component.StarDustData;
import net.zuperz.aurora.item.custom.AuroraSkullItem;

public class ArcanePedestalRecipe implements Recipe<RecipeInput> {

    public final ItemStack output;
    public final Ingredient ingredient0;

    public ArcanePedestalRecipe(ItemStack output, Ingredient ingredient0) {
        this.output = output;
        this.ingredient0 = ingredient0;
    }
    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider registries) {
        return output;
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "arcane_pedestal");
    }
    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }
    public ItemStack getResultEmi(){
        return output.copy();
    }
    @Override
    public boolean matches(RecipeInput pContainer, Level pLevel) {
        if(pLevel.isClientSide()) {
            return false;
        }
        return ingredient0.test(pContainer.getItem(0));
    }
    @Override
    public boolean isSpecial() {
        return true;
    }
    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(1);
        ingredients.add(0, ingredient0);
        return ingredients;
    }
    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }
    @Override
    public String getGroup() {
        return "arcane_pedestal";
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ArcanePedestalRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return ArcanePedestalRecipe.Type.INSTANCE;
    }

    public static final class Type implements RecipeType<ArcanePedestalRecipe> {
        private Type() { }
        public static final ArcanePedestalRecipe.Type INSTANCE = new ArcanePedestalRecipe.Type();
        public static final String ID = "arcane_pedestal";
    }
    public static final class Serializer implements RecipeSerializer<ArcanePedestalRecipe> {
        private Serializer() {}
        public static final ArcanePedestalRecipe.Serializer INSTANCE = new ArcanePedestalRecipe.Serializer();
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "arcane_pedestal");

        private final MapCodec<ArcanePedestalRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(CodecFix.ITEM_STACK_CODEC.fieldOf("output").forGetter((recipe) -> {
                return recipe.output;
            }), Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((recipe) -> {
                return recipe.ingredient0;
            })).apply(instance, ArcanePedestalRecipe::new);
        });

        private final StreamCodec<RegistryFriendlyByteBuf, ArcanePedestalRecipe> STREAM_CODEC = StreamCodec.of(
                ArcanePedestalRecipe.Serializer::write, ArcanePedestalRecipe.Serializer::read);

        @Override
        public MapCodec<ArcanePedestalRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ArcanePedestalRecipe> streamCodec() {
            return STREAM_CODEC;
        }


        private static ArcanePedestalRecipe read(RegistryFriendlyByteBuf  buffer) {
            Ingredient input0 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            return new ArcanePedestalRecipe(output, input0);
        }


        private static void write(RegistryFriendlyByteBuf  buffer, ArcanePedestalRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient0);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.output);
        }
    }
}