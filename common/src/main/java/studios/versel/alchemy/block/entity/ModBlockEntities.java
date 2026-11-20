
package studios.versel.alchemy.block.entity;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.block.ModBlocks;

public final class ModBlockEntities {

    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<AlchemyPortEntity>> ALCHEMY_PORT =
            BLOCK_ENTITIES.register(
                    "alchemy_port",
                    () -> BlockEntityType.Builder.of(
                            AlchemyPortEntity::new,
                            ModBlocks.ALCHEMY_PORT.get()
                    ).build(null)
            );

    public static final RegistrySupplier<BlockEntityType<AlchemyTableEntity>> ALCHEMY_TABLE =
            BLOCK_ENTITIES.register(
                    "alchemy_table",
                    () -> BlockEntityType.Builder.of(
                            AlchemyTableEntity::new,
                            ModBlocks.ALCHEMY_TABLE.get()
                    ).build(null)
            );

    public static void register() {
        BLOCK_ENTITIES.register();
    }

    private ModBlockEntities() {}
}
