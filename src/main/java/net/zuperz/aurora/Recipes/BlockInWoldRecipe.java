package net.zuperz.aurora.Recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.zuperz.aurora.aurora;

public class BlockInWoldRecipe implements Recipe<RecipeInput> {

    public final ItemStack output;
    public final Ingredient ingredient0;
    public final Ingredient ingredient1;
    public final Ingredient ingredient2;

    public BlockInWoldRecipe(ItemStack output, Ingredient ingredient0, Ingredient ingredient1, Ingredient ingredient2) {
        this.output = output;
        this.ingredient0 = ingredient0;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }

    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider registries) {
        return output;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return output.copy();
    }

    public ItemStack getResultEmi() {
        return output.copy();
    }

    @Override
    public boolean matches(RecipeInput pContainer, Level pLevel) {
        if (pLevel.isClientSide()) {
            return false;
        }

        ItemStack ingredient0Stack = pContainer.getItem(0);
        boolean ingredient0Matches = ingredient0Stack.isEmpty() || ingredient0Stack.getItem() == Items.BARRIER;

        boolean ingredient1Matches = ingredient1.test(pContainer.getItem(1));
        boolean ingredient2Matches = ingredient2.test(pContainer.getItem(2));

        // Return true if ingredient0 is empty or a barrier, and the others match
        return ingredient0Matches && ingredient1Matches && ingredient2Matches;
    }



    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.createWithCapacity(3);
        ingredients.add(0, ingredient0);
        ingredients.add(1, ingredient1);
        ingredients.add(2, ingredient2);
        return ingredients;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public String getGroup() {
        return "in_world_block";
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BlockInWoldRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return BlockInWoldRecipe.Type.INSTANCE;
    }

    public static final class Type implements RecipeType<BlockInWoldRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "in_world_block";
    }

    public static final class Serializer implements RecipeSerializer<BlockInWoldRecipe> {
        private Serializer() {}
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "in_world_block");

        private final MapCodec<BlockInWoldRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(
                    CodecFix.ITEM_STACK_CODEC.fieldOf("output").forGetter((recipe) -> recipe.output),
                    Ingredient.CODEC_NONEMPTY.fieldOf("block_1").forGetter((recipe) -> recipe.ingredient0),
                    Ingredient.CODEC_NONEMPTY.fieldOf("block_2").forGetter((recipe) -> recipe.ingredient1),
                    Ingredient.CODEC_NONEMPTY.fieldOf("block_3").forGetter((recipe) -> recipe.ingredient2)
            ).apply(instance, BlockInWoldRecipe::new);
        });

        private final StreamCodec<RegistryFriendlyByteBuf, BlockInWoldRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public MapCodec<BlockInWoldRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, BlockInWoldRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static BlockInWoldRecipe read(RegistryFriendlyByteBuf buffer) {
            Ingredient input0 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient input1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient input2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);

            return new BlockInWoldRecipe(output, input0, input1, input2);
        }

        private static void write(RegistryFriendlyByteBuf buffer, BlockInWoldRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient0);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient2);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.output);
        }
    }
}
