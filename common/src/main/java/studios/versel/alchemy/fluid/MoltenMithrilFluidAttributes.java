package studios.versel.alchemy.fluid;

import net.minecraft.world.level.material.Fluid;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.item.ModItems;

import java.util.function.Supplier;

public class MoltenMithrilFluidAttributes extends MoltenFluidAttributes {


    public MoltenMithrilFluidAttributes(Supplier<? extends Fluid> flowingFluid, Supplier<? extends Fluid> sourceFluid) {
        super(flowingFluid, sourceFluid, 0xFF4198B7);
        this.blockSupplier(() -> ModBlocks.MOLTEN_MITHRIL_FLUID_BLOCK).bucketItemSupplier(() -> ModItems.asItem(ModItems.MOLTEN_MITHRIL_BUCKET));
    }


}
