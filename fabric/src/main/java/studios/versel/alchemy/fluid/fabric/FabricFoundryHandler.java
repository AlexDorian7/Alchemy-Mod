package studios.versel.alchemy.fluid.fabric;

import dev.architectury.fluid.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import studios.versel.alchemy.block.entity.FoundryBlockEntity;

public class FabricFoundryHandler extends SingleVariantStorage<FluidVariant> {

    private final FoundryBlockEntity foundryBlockEntity;

    public FabricFoundryHandler(FoundryBlockEntity foundryBlockEntity) {
        this.foundryBlockEntity = foundryBlockEntity;
    }

    @Override
    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    @Override
    protected long getCapacity(FluidVariant fluidVariant) {
        return foundryBlockEntity.getTank().getCapacity();
    }

    @Override
    protected void onFinalCommit() {
        // Synchronize committed Fabric transactions with the common tank.
        if (variant.isBlank() || amount <= 0) {
            foundryBlockEntity.getTank().setFluid(FluidStack.empty());
        } else {
            foundryBlockEntity.getTank().setFluid(
                    FluidStack.create(variant.getFluid(), amount)
            );
        }
        foundryBlockEntity.setChanged();
    }

    @Override
    protected boolean canInsert(FluidVariant variant) {
        return false;
    }

    @Override
    protected boolean canExtract(FluidVariant variant) {
        return true;
    }
}
