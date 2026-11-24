package studios.versel.alchemy.fluid;

import net.minecraft.world.level.material.Fluid;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.item.ModItems;

import java.util.function.Supplier;

public class MoltenCopperFluidAttributes extends MoltenFluidAttributes {


    public MoltenCopperFluidAttributes(Supplier<? extends Fluid> flowingFluid, Supplier<? extends Fluid> sourceFluid) {
        super(flowingFluid, sourceFluid, 0xFFC95E35);
        this.blockSupplier(() -> ModBlocks.MOLTEN_COPPER_FLUID_BLOCK).bucketItemSupplier(() -> ModItems.asItem(ModItems.MOLTEN_COPPER_BUCKET));
    }


}
