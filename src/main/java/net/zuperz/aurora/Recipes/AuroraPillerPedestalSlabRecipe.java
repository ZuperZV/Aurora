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

public class AuroraPillerPedestalSlabRecipe implements Recipe<RecipeInput> {

    public final ItemStack output;
    public final Ingredient ingredient0;

    public AuroraPillerPedestalSlabRecipe(ItemStack output, Ingredient ingredient0) {
        this.output = output;
        this.ingredient0 = ingredient0;
    }
    @Override
    public ItemStack assemble(RecipeInput container, HolderLookup.Provider registries) {
        return output;
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "aurora_piller_pedestal_slab");
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
        return "aurora_piller_pedestal_slab";
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AuroraPillerPedestalSlabRecipe.Serializer.INSTANCE;
    }

    @Override
    public RecipeType<?> getType() {
        return AuroraPillerPedestalSlabRecipe.Type.INSTANCE;
    }

    public static final class Type implements RecipeType<AuroraPillerPedestalSlabRecipe> {
        private Type() { }
        public static final Type INSTANCE = new Type();
        public static final String ID = "aurora_piller_pedestal_slab";
    }
    public static final class Serializer implements RecipeSerializer<AuroraPillerPedestalSlabRecipe> {
        private Serializer() {}
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                ResourceLocation.fromNamespaceAndPath(aurora.MOD_ID, "aurora_piller_pedestal_slab");

        private final MapCodec<AuroraPillerPedestalSlabRecipe> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
            return instance.group(CodecFix.ITEM_STACK_CODEC.fieldOf("output").forGetter((recipe) -> {
                return recipe.output;
            }), Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter((recipe) -> {
                return recipe.ingredient0;
            })).apply(instance, AuroraPillerPedestalSlabRecipe::new);
        });

        private final StreamCodec<RegistryFriendlyByteBuf, AuroraPillerPedestalSlabRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::write, Serializer::read);

        @Override
        public MapCodec<AuroraPillerPedestalSlabRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AuroraPillerPedestalSlabRecipe> streamCodec() {
            return STREAM_CODEC;
        }


        private static AuroraPillerPedestalSlabRecipe read(RegistryFriendlyByteBuf  buffer) {
            Ingredient input0 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            ItemStack output = ItemStack.OPTIONAL_STREAM_CODEC.decode(buffer);
            return new AuroraPillerPedestalSlabRecipe(output, input0);
        }


        private static void write(RegistryFriendlyByteBuf  buffer, AuroraPillerPedestalSlabRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient0);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buffer, recipe.output);
        }
    }
}