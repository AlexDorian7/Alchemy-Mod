package studios.versel.alchemy.multiblock;

import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.block.states.ModBlockProperties;

public final class AlchemyStructure {

    public static final StructureDefinition ALCHEMY_TABLE =
            StructureDefinition.builder()
                    .fill(-1, -1, 0, 1, 1, 1, ModBlocks.asBlockSupplier(ModBlocks.ALCHEMY_BLOCK))
                    .add(0, 0, 0, ModBlocks.asBlockSupplier(ModBlocks.ALCHEMY_TABLE))
                    .add(-1, -1, 0, ModBlocks.asBlockSupplier(ModBlocks.ALCHEMY_PORT))
                    .add(1, -1, 0, ModBlocks.asBlockSupplier(ModBlocks.ALCHEMY_PORT))
                    .build();

    public static final MultiblockType ALCHEMY_TABLE_TYPE =
            new MultiblockType(
                    ALCHEMY_TABLE,
                    state -> state.is(ModBlocks.ALCHEMY_TABLE.value()),
                    state -> state.getValue(HorizontalDirectionalBlock.FACING),
                    ModBlockProperties.COMPLETED
            );

    private AlchemyStructure() {}

    public static void register() {
        MultiblockManager.register(ALCHEMY_TABLE_TYPE);
    }
}