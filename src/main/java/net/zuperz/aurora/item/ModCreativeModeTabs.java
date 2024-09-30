package net.zuperz.aurora.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;

import java.util.function.Supplier;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, aurora.MOD_ID);

    public static final Supplier<CreativeModeTab> AMBER_AURORA_TAB =
            CREATIVE_MODE_TABS.register("aurora_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.aurora.aurora_tab"))
                    .icon(() -> new ItemStack(ModItems.SKRAP_AURORA.get()))
                    .displayItems((pParameters, pOutput) -> {
                        // Decorative Blocks
                        pOutput.accept(ModBlocks.AURORA_PILLER.get());
                        pOutput.accept(ModBlocks.PEDESTAL_SLAB.get());
                        pOutput.accept(ModBlocks.ALCHE_FLAME.get());
                        pOutput.accept(ModBlocks.BEAM.get());
                        pOutput.accept(ModBlocks.ARCANE_PEDESTAL.get());
                        pOutput.accept(ModBlocks.ARCANE_POWER_TABLE.get());
                        pOutput.accept(ModBlocks.BLUESTONE.get());
                        pOutput.accept(ModBlocks.STONE_SKULL_STONE.get());

                        // Functional Blocks
                        pOutput.accept(ModBlocks.VOID_STONE.get());
                        pOutput.accept(ModBlocks.LUMINOUS_VOID_STONE.get());
                        pOutput.accept(ModBlocks.COBBLE_VOID_STONE.get());

                        // Raw Materials
                        pOutput.accept(ModItems.AURORA_INGOT.get());
                        pOutput.accept(ModItems.AURORA_DUST.get());
                        pOutput.accept(ModItems.CLAY_DUST.get());
                        pOutput.accept(ModItems.BLUESTONE_DUST.get());
                        pOutput.accept(ModItems.BLUESTONE_SHARD.get());
                        pOutput.accept(ModItems.GLOW_STONE_SHARD.get());
                        pOutput.accept(ModItems.SKRAP_AURORA.get());

                        // Special Items
                        pOutput.accept(ModItems.SOFT_CLAY_JAR.get());
                        pOutput.accept(ModItems.CLAY_JAR.get());
                        pOutput.accept(ModItems.SAPLING_CLAY_JAR.get());
                        pOutput.accept(ModItems.FIRE_CLAY_JAR.get());
                        pOutput.accept(ModItems.TWIG_CLAY_JAR.get());
                        pOutput.accept(ModItems.MAGIC_CLAY_JAR.get());
                        pOutput.accept(ModItems.EXTRACTER_CLAY_JAR.get());

                        // Miscellaneous Items
                        pOutput.accept(ModItems.HARD_CLAY_BALL.get());
                        pOutput.accept(ModItems.STONE_SKULL.get());
                        pOutput.accept(ModItems.AURORA_SKULL.get());
                        pOutput.accept(ModItems.TWIG.get());
                        pOutput.accept(ModItems.SKULL_TWIG.get());
                        pOutput.accept(ModItems.MATERIALE_TWIG.get());
                        pOutput.accept(ModItems.NETHERITE_MATERIALE_TWIG.get());
                        pOutput.accept(ModItems.VOID_STONE_SHARD.get());
                        pOutput.accept(ModItems.SOUL_SHARD.get());
                        pOutput.accept(ModItems.SOUL_SHARD_ZOMBIE.get());
                        pOutput.accept(ModItems.WITHER_SKULL_CASTING.get());
                        pOutput.accept(ModItems.LUMINOUS.get());

                        // Summoning Scrolls
                        pOutput.accept(ModItems.SUMMONING_SCROLL_ZOMBIE.get());
                        pOutput.accept(ModItems.SUMMONING_SCROLL_ENDERMAN.get());
                        pOutput.accept(ModItems.SUMMONING_SCROLL_GHAST.get());

                        pOutput.accept(ModItems.RIFT.get());
                    }).build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
