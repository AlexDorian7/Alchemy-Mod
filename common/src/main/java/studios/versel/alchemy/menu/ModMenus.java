package studios.versel.alchemy.menu;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

import static studios.versel.alchemy.Constants.MOD_ID;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<AlchemyMenu>> ALCHEMY_MENU =
            MENUS.register("alchemy_menu", () ->
                    MenuRegistry.ofExtended(AlchemyMenu::new)
            );

    public static void register() {
        MENUS.register();
    }
}