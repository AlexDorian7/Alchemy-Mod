package studios.versel.alchemy.fabric;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.InventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import studios.versel.alchemy.block.entity.ModBlockEntities;
import studios.versel.alchemy.fluid.fabric.FabricAlchemyPortHandler;
import studios.versel.alchemy.fluid.fabric.FabricFoundryHandler;
import studios.versel.alchemy.fluid.fabric.FabricFoundryPortHandler;

public final class FabricStorage {

    public static void register() {
        FluidStorage.SIDED.registerForBlockEntity(
                (port, direction) -> new FabricAlchemyPortHandler(port),
                ModBlockEntities.ALCHEMY_PORT.get()
        );

        FluidStorage.SIDED.registerForBlockEntity(
                (foundry, direction) -> new FabricFoundryHandler(foundry),
                ModBlockEntities.FOUNDRY.get()
        );

        FluidStorage.SIDED.registerForBlockEntity(
                (port, direction) -> new FabricFoundryPortHandler(port),
                ModBlockEntities.FOUNDRY_PORT.get()
        );

        ItemStorage.SIDED.registerForBlockEntity(
                InventoryStorage::of,
                ModBlockEntities.FOUNDRY.get()
        );
    }

    private FabricStorage() {}
}