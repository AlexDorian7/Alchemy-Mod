package studios.versel.alchemy.fabric;

import net.fabricmc.api.ModInitializer;

import studios.versel.alchemy.CommonAlchemy;

public final class AlchemyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        CommonAlchemy.init();

        FabricStorage.register();
    }
}
