package studios.versel.alchemy.neoforge.fluid;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import studios.versel.alchemy.block.entity.ModBlockEntities;

public final class NeoForgeFluidCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.ALCHEMY_PORT.get(),
                (port, side) -> new NeoForgeAlchemyPortHandler(port)
        );
    }

    private NeoForgeFluidCapabilities() {}
}