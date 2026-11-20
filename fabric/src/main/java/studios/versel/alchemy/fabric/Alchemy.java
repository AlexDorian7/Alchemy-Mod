package studios.versel.alchemy.fabric;

import net.fabricmc.api.ModInitializer;
import studios.versel.alchemy.CommonAlchemy;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.fabric.fluid.FabricFluidStorage;

public class Alchemy implements ModInitializer {
    
    @Override
    public void onInitialize() {
        
        // This method is invoked by the Fabric mod loader when it is ready
        // to load your mod. You can access Fabric and Common code in this
        // project.

        // Use Fabric to bootstrap the Common mod.
        Constants.LOG.info("Hello Fabric world!");
        CommonAlchemy.init();

        FabricFluidStorage.register();
    }
}
