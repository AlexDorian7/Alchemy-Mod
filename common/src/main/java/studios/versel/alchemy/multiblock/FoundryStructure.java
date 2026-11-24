package studios.versel.alchemy.multiblock;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.block.states.ModBlockProperties;

public final class FoundryStructure {

    public static final StructureDefinition FOUNDRY =
            StructureDefinition.builder()
                    .fill(-1, -1, 0, 1, 1, 2, ModBlocks.asBlockSupplier(ModBlocks.FOUNDRY_BRICKS))
                    .add(0, 0, 0, ModBlocks.asBlockSupplier(ModBlocks.FOUNDRY)) // Front middle
                    .add(0, -1, 2, ModBlocks.asBlockSupplier(ModBlocks.FOUNDRY_PORT)) // Back bottom
                    .remove(0,0,1) // Remove middle
                    .remove(0,1,1) // Remove top middle
                    .build();

    public static final MultiblockType FOUNDRY_TYPE =
            new MultiblockType(
                    FOUNDRY,
                    state -> state.is(ModBlocks.FOUNDRY.value()),
                    state -> state.getValue(HorizontalDirectionalBlock.FACING),
                    ModBlockProperties.COMPLETED
            );

    private FoundryStructure() {}

    public static void register() {
        MultiblockManager.register(FOUNDRY_TYPE);
    }
}