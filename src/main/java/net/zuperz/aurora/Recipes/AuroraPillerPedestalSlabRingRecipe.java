package net.zuperz.aurora.Recipes;

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

public class AuroraPillerPedestalSlabRingRecipe implements Recipe<RecipeInput> {

    public final ItemStack output;
    public final Ingredient ingredient0;
    public final Ingredient ingredient1;
    public final Ingredient ingredient2;

    public AuroraPillerPedestalSlabRingRecipe(ItemStack output, Ingredient ingredient0, Ingredient ingredient1, Ingredient ingredient2) {
        this.output = output;
        this.ingredient0 = ingredient0;
        this.ingredient1 = ingredient1;
        this.ingredient2 = ingredient2;
    }
    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider registries) {
        return output;
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "aurora_piller_pedestal_slab_ring");
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
        return ingredient0.test(pContainer.getItem(0)) && ingredient1.test(pContainer.getItem(1)) && ingredient2.test(pContainer.getItem(1));
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
        return "aurora_piller_pedestal_slab_ring";
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AuroraPillerPedestalSlabRingRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return AuroraPillerPedestalSlabRingRecipe.Type.INSTANCE;
    }

    public static final class Type implements RecipeType<AuroraPillerPedestalSlabRingRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "aurora_piller_pedestal_slab_ring";
    }
    public static final class Serializer implements RecipeSerializer<AuroraPillerPedestalSlabRingRecipe> {
        private Serializer() {}
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "aurora_piller_pedestal_slab_ring");

        private final MapCodec<AuroraPillerPedestalSlabRingRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(CodecFix.ITEM_STACK_CODEC.fieldOf("output").forGetter((recipe) -> {
                return recipe.output;
            }), Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((recipe) -> {
                return recipe.ingredient0;
            }), Ingredient.CODEC_NONEMPTY.fieldOf("wire").forGetter((recipe) -> {
                return recipe.ingredient1;
            }), Ingredient.CODEC_NONEMPTY.fieldOf("piller").forGetter((recipe) -> {
                return recipe.ingredient2;
            })).apply(instance, AuroraPillerPedestalSlabRingRecipe::new);
        });

        private final StreamCodec<RegistryFriendlyByteBuf, AuroraPillerPedestalSlabRingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public MapCodec<AuroraPillerPedestalSlabRingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AuroraPillerPedestalSlabRingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static AuroraPillerPedestalSlabRingRecipe read(RegistryFriendlyByteBuf  buffer) {
            Ingredient input0 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient input1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            Ingredient input2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);

            return new AuroraPillerPedestalSlabRingRecipe(output, input0, input1, input2);
        }

        private static void write(RegistryFriendlyByteBuf  buffer, AuroraPillerPedestalSlabRingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient0);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient1);
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient2);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.output);
        }
    }
}