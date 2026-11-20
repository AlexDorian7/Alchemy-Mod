package studios.versel.alchemy.fabric.fluid;

import dev.architectury.fluid.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import studios.versel.alchemy.block.entity.AlchemyPortEntity;

public class FabricAlchemyPortHandler extends SingleVariantStorage<FluidVariant> {

    private final AlchemyPortEntity port;

    public FabricAlchemyPortHandler(AlchemyPortEntity port) {
        this.port = port;

        // Initialize the Fabric storage from the common tank.
        FluidStack stored = port.getTank().getFluid();

        if (!stored.isEmpty()) {
            this.variant = FluidVariant.of(stored.getFluid());
            this.amount = stored.getAmount();
        }
    }

    @Override
    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    @Override
    protected long getCapacity(FluidVariant variant) {
        return port.getTank().getCapacity();
    }

    @Override
    protected void onFinalCommit() {
        // Synchronize committed Fabric transactions with the common tank.
        if (variant.isBlank() || amount <= 0) {
            port.getTank().setFluid(FluidStack.empty());
        } else {
            port.getTank().setFluid(
                    FluidStack.create(variant.getFluid(), amount)
            );
        }

        port.setChanged();
    }
}