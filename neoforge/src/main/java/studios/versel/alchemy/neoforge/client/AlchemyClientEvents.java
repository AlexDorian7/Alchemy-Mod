package studios.versel.alchemy.neoforge.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.client.screen.AlchemyScreen;
import studios.versel.alchemy.menu.ModMenus;

@EventBusSubscriber(
        modid = Constants.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT
)
public final class AlchemyClientEvents {

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(
                ModMenus.ALCHEMY_MENU.get(),
                AlchemyScreen::new
        );
    }

    private AlchemyClientEvents() {}
}