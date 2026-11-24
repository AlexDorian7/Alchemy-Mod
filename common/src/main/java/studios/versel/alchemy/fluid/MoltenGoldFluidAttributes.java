package studios.versel.alchemy.fluid;

import net.minecraft.world.level.material.Fluid;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.item.ModItems;

import java.util.function.Supplier;

public class MoltenGoldFluidAttributes extends MoltenFluidAttributes {


    public MoltenGoldFluidAttributes(Supplier<? extends Fluid> flowingFluid, Supplier<? extends Fluid> sourceFluid) {
        super(flowingFluid, sourceFluid, 0xFFF7C431);
        this.blockSupplier(() -> ModBlocks.MOLTEN_GOLD_FLUID_BLOCK).bucketItemSupplier(() -> ModItems.asItem(ModItems.MOLTEN_GOLD_BUCKET));
    }


}
