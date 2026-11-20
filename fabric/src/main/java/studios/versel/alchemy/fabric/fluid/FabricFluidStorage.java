package studios.versel.alchemy.fabric.fluid;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import studios.versel.alchemy.block.entity.ModBlockEntities;

public final class FabricFluidStorage {

    public static void register() {
        FluidStorage.SIDED.registerForBlockEntity(
                (port, direction) -> new FabricAlchemyPortHandler(port),
                ModBlockEntities.ALCHEMY_PORT.get()
        );
    }

    private FabricFluidStorage() {}
}