package studios.versel.alchemy.block;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.block.states.ModBlockProperties;
import studios.versel.alchemy.fluid.ModFluids;

import java.util.function.Supplier;

public final class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    // Block properties
    private static BlockBehaviour.Properties alchemyProperties() {
        return BlockBehaviour.Properties.of()
                .strength(10, 10)
                .pushReaction(PushReaction.BLOCK)
                .sound(SoundType.METAL)
                .mapColor(MapColor.COLOR_CYAN)
                .noOcclusion()
                .isValidSpawn(ModBlocks::never)
                .isRedstoneConductor(ModBlocks::never)
                .isSuffocating(ModBlocks::never)
                .isViewBlocking(ModBlocks::never)
                .requiresCorrectToolForDrops()
                .lightLevel(state ->
                        state.getValue(ModBlockProperties.COMPLETED) ? 15 : 0
                );
    }

    private static BlockBehaviour.Properties foundryProperties() {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS)
                .noOcclusion()
                .isValidSpawn(ModBlocks::never)
                .isRedstoneConductor(ModBlocks::never)
                .isSuffocating(ModBlocks::never)
                .isViewBlocking(ModBlocks::never)
                .requiresCorrectToolForDrops()
                .lightLevel(state ->
                        state.getValue(ModBlockProperties.COMPLETED) ? 15 : 0
                );
    }

    // Block registration
    public static final RegistrySupplier<AlchemyTable> ALCHEMY_TABLE = BLOCKS.register("alchemy_table", () -> new AlchemyTable(alchemyProperties()));
    public static final RegistrySupplier<MultiblockBlock> ALCHEMY_BLOCK = BLOCKS.register("alchemy_block", () -> new MultiblockBlock(alchemyProperties()));
    public static final RegistrySupplier<AlchemyPort> ALCHEMY_PORT = BLOCKS.register("alchemy_port", () -> new AlchemyPort(alchemyProperties()));

    public static final RegistrySupplier<FoundryBlock> FOUNDRY = BLOCKS.register("foundry", () -> new FoundryBlock(foundryProperties()));
    public static final RegistrySupplier<FoundryPort> FOUNDRY_PORT = BLOCKS.register("foundry_port", () -> new FoundryPort(foundryProperties()));
    public static final RegistrySupplier<MultiblockBlock> FOUNDRY_BRICKS = BLOCKS.register("foundry_bricks", () -> new MultiblockBlock(foundryProperties()));
    public static final RegistrySupplier<Block> FOUNDRY_PORT_PLANT = BLOCKS.register("foundry_port_plant", () -> new Block(BlockBehaviour.Properties.of().instabreak().mapColor(MapColor.NONE)));


    public static final RegistrySupplier<Block> MITHRIL_ORE = BLOCKS.register("mithril_ore", () -> new DropExperienceBlock(UniformInt.of(7,10), BlockBehaviour.Properties.ofLegacyCopy(Blocks.COPPER_ORE).strength(50F, 6.0F)));
    public static final RegistrySupplier<Block> DEEPSLATE_MITHRIL_ORE = BLOCKS.register("deepslate_mithril_ore", () -> new DropExperienceBlock(UniformInt.of(7,10), BlockBehaviour.Properties.ofLegacyCopy(Blocks.DEEPSLATE_COPPER_ORE).strength(50F, 6.0f)));


    public static final RegistrySupplier<Block> RAW_MITHRIL_BLOCK = BLOCKS.register("raw_mithril_block", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(5.0F, 6.0F)));



    public static final RegistrySupplier<MoltenLiquidBlock> MOLTEN_MITHRIL_FLUID_BLOCK =
            BLOCKS.register(
                    "molten_mithril",
                    () -> new MoltenLiquidBlock(
                            ModFluids.MOLTEN_MITHRIL_STILL,
                            RAW_MITHRIL_BLOCK,
                            BlockBehaviour.Properties.of()
                                    .liquid()
                                    .noLootTable()
                                    .noCollission()
                                    .replaceable()
                                    .pushReaction(PushReaction.DESTROY)
                                    .sound(SoundType.EMPTY)
                                    .mapColor(MapColor.COLOR_CYAN)
                                    .strength(100)
                                    .lightLevel(state -> 15)
                    )
            );

    public static final RegistrySupplier<MoltenLiquidBlock> MOLTEN_IRON_FLUID_BLOCK =
            BLOCKS.register(
                    "molten_iron",
                    () -> new MoltenLiquidBlock(
                            ModFluids.MOLTEN_IRON_STILL,
                            () -> Blocks.RAW_IRON_BLOCK,
                            BlockBehaviour.Properties.of()
                                    .liquid()
                                    .noLootTable()
                                    .noCollission()
                                    .replaceable()
                                    .pushReaction(PushReaction.DESTROY)
                                    .sound(SoundType.EMPTY)
                                    .mapColor(MapColor.COLOR_RED)
                                    .strength(100)
                                    .lightLevel(state -> 15)
                    )
            );

    public static final RegistrySupplier<MoltenLiquidBlock> MOLTEN_COPPER_FLUID_BLOCK =
            BLOCKS.register(
                    "molten_copper",
                    () -> new MoltenLiquidBlock(
                            ModFluids.MOLTEN_COPPER_STILL,
                            () -> Blocks.RAW_COPPER_BLOCK,
                            BlockBehaviour.Properties.of()
                                    .liquid()
                                    .noLootTable()
                                    .noCollission()
                                    .replaceable()
                                    .pushReaction(PushReaction.DESTROY)
                                    .sound(SoundType.EMPTY)
                                    .mapColor(MapColor.COLOR_ORANGE)
                                    .strength(100)
                                    .lightLevel(state -> 15)
                    )
            );

    public static final RegistrySupplier<MoltenLiquidBlock> MOLTEN_GOLD_FLUID_BLOCK =
            BLOCKS.register(
                    "molten_gold",
                    () -> new MoltenLiquidBlock(
                            ModFluids.MOLTEN_GOLD_STILL,
                            () -> Blocks.RAW_GOLD_BLOCK,
                            BlockBehaviour.Properties.of()
                                    .liquid()
                                    .noLootTable()
                                    .noCollission()
                                    .replaceable()
                                    .pushReaction(PushReaction.DESTROY)
                                    .sound(SoundType.EMPTY)
                                    .mapColor(MapColor.GOLD)
                                    .strength(100)
                                    .lightLevel(state -> 15)
                    )
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

    @SuppressWarnings("unchecked")
    public static Supplier<Block> asBlockSupplier(RegistrySupplier<? extends Block> supplier) {
        return (Supplier<Block>) supplier;
    }

    public static void register() {
        BLOCKS.register();
    }

    private ModBlocks() {}
}