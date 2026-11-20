package studios.versel.alchemy.neoforge;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;
import studios.versel.alchemy.CommonAlchemy;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.neoforge.fluid.NeoForgeFluidCapabilities;

@Mod(Constants.MOD_ID)
public class Alchemy {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Constants.MOD_ID);

    public Alchemy(IEventBus modEventBus) {
        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        Constants.LOG.info("Hello NeoForge world!");
        CommonAlchemy.init();

        modEventBus.addListener(NeoForgeFluidCapabilities::register);
    }
}