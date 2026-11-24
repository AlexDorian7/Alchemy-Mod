package studios.versel.alchemy.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import studios.versel.alchemy.client.ClientAlchemy;
import studios.versel.alchemy.client.screen.AlchemyScreen;
import studios.versel.alchemy.menu.ModMenus;

public final class AlchemyModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientAlchemy.register();

        MenuScreens.register(
                ModMenus.ALCHEMY_MENU.get(),
                AlchemyScreen::new
        );
    }
}
