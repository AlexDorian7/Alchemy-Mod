package studios.versel.alchemy.neoforge;

import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.wrapper.SidedInvWrapper;
import studios.versel.alchemy.block.entity.ModBlockEntities;
import studios.versel.alchemy.fluid.neoforge.NeoForgeAlchemyPortHandler;
import studios.versel.alchemy.fluid.neoforge.NeoForgeFoundryHandler;
import studios.versel.alchemy.fluid.neoforge.NeoForgeFoundryPortHandler;

public final class NeoForgeCapabilities {

    public static void register(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.ALCHEMY_PORT.get(),
                (port, side) -> new NeoForgeAlchemyPortHandler(port)
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.FOUNDRY.get(),
                (foundry, side) -> new NeoForgeFoundryHandler(foundry)
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.FOUNDRY_PORT.get(),
                (port, side) -> new NeoForgeFoundryPortHandler(port)
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                ModBlockEntities.FOUNDRY.get(),
                (foundry, side) -> new NeoForgeFoundryHandler(foundry)
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                ModBlockEntities.FOUNDRY.get(),
                SidedInvWrapper::new
        );
    }

    private NeoForgeCapabilities() {}
}