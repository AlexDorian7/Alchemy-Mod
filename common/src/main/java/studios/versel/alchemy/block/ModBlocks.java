package studios.versel.alchemy.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.block.states.ModBlockProperties;

public final class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    // Block properties
    private static BlockBehaviour.Properties alchemyProperties() {
        return BlockBehaviour.Properties.of()
                .strength(10, 10)
                .pushReaction(PushReaction.BLOCK)
                .sound(SoundType.METAL)
                .mapColor(MapColor.COLOR_ORANGE)
                .noOcclusion()
                .isValidSpawn(ModBlocks::never)
                .isRedstoneConductor(ModBlocks::never)
                .isSuffocating(ModBlocks::never)
                .isViewBlocking(ModBlocks::never)
                .lightLevel(state ->
                        state.getValue(ModBlockProperties.COMPLETED) ? 15 : 0
                );
    }

    // Block registration
    public static final RegistrySupplier<AlchemyTable> ALCHEMY_TABLE =
            BLOCKS.register(
                    ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "alchemy_table"),
                    () -> new AlchemyTable(alchemyProperties())
            );

    public static final RegistrySupplier<AlchemyBlock> ALCHEMY_BLOCK =
            BLOCKS.register(
                    "alchemy_block",
                    () -> new AlchemyBlock(alchemyProperties())
            );

    public static final RegistrySupplier<AlchemyPort> ALCHEMY_PORT =
            BLOCKS.register(
                    "alchemy_port",
                    () -> new AlchemyPort(alchemyProperties())
            );

    private static boolean never(
            BlockState state,
            BlockGetter level,
            BlockPos pos
    ) {
        return false;
    }

    private static boolean never(
            BlockState state,
            BlockGetter level,
            BlockPos pos,
            EntityType<?> entity
    ) {
        return false;
    }

    public static void register() {
        BLOCKS.register();
    }

    private ModBlocks() {}
}