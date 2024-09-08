package net.zuperz.aurora.screen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zuperz.aurora.aurora;
import net.zuperz.aurora.block.ModBlocks;
import net.zuperz.aurora.block.entity.custom.MyBlockTile;

import java.util.function.Supplier;


public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, aurora.MOD_ID);

    public static final Supplier<MenuType<MyMenu>> MY_MENU = MENUS.register("my_menu",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new MyMenu(windowId, inv.player, data.readBlockPos())));

    public static final Supplier<MenuType<AlcheFlameMenu>> ALCHE_FLAME_MENU = MENUS.register("alche_flame_menu",
            () -> IMenuTypeExtension.create((windowId, inv, data) -> new AlcheFlameMenu(windowId, inv.player, data.readBlockPos())));

    private static <T extends AbstractContainerMenu> Supplier<MenuType<T>> registerMenuType(IContainerFactory<T> factory,
                                                                                            String name) {
        return MENUS.register(name, () -> IMenuTypeExtension.create(factory));
    }

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
