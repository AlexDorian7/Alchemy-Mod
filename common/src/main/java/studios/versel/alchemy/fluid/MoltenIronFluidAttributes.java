package studios.versel.alchemy.fluid;

import net.minecraft.world.level.material.Fluid;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.item.ModItems;

import java.util.function.Supplier;

public class MoltenIronFluidAttributes extends MoltenFluidAttributes {


    public MoltenIronFluidAttributes(Supplier<? extends Fluid> flowingFluid, Supplier<? extends Fluid> sourceFluid) {
        super(flowingFluid, sourceFluid, 0xFFFF3F00);
        this.blockSupplier(() -> ModBlocks.MOLTEN_IRON_FLUID_BLOCK).bucketItemSupplier(() -> ModItems.asItem(ModItems.MOLTEN_IRON_BUCKET));
    }


}
