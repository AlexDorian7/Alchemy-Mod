package studios.versel.alchemy.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screens.MenuScreens;
import studios.versel.alchemy.client.screen.AlchemyScreen;
import studios.versel.alchemy.menu.ModMenus;

public class AlchemyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MenuScreens.register(
                ModMenus.ALCHEMY_MENU.get(),
                AlchemyScreen::new
        );
    }
}
