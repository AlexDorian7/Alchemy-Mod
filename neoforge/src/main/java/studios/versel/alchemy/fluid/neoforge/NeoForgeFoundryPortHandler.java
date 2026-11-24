package studios.versel.alchemy.fluid.neoforge;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import studios.versel.alchemy.block.entity.AlchemyPortEntity;
import studios.versel.alchemy.block.entity.FoundryPortEntity;

public class NeoForgeFoundryPortHandler implements IFluidHandler {

    private final FoundryPortEntity port;

    public NeoForgeFoundryPortHandler(FoundryPortEntity port) {
        this.port = port;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @Override
    public FluidStack getFluidInTank(int tank) {
        if (tank != 0) {
            return FluidStack.EMPTY;
        }

        var fluid = port.getTank().getFluid();

        if (fluid.isEmpty()) {
            return FluidStack.EMPTY;
        }

        return new FluidStack(
                fluid.getFluid(),
                Math.toIntExact(fluid.getAmount())
        );
    }

    @Override
    public int getTankCapacity(int tank) {
        if (tank != 0) {
            return 0;
        }

        return Math.toIntExact(port.getTank().getCapacity());
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        if (tank != 0 || stack.isEmpty()) {
            return false;
        }

        var stored = port.getTank().getFluid();

        return stored.isEmpty() || stored.getFluid() == stack.getFluid();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return 0;
        }

        var stack = dev.architectury.fluid.FluidStack.create(
                resource.getFluid(),
                resource.getAmount()
        );

        long accepted = port.getTank().fill(
                stack,
                action.simulate()
        );

        if (accepted > 0 && !action.simulate()) {
            port.setChanged();
        }

        return Math.toIntExact(accepted);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return FluidStack.EMPTY;
        }

        var stored = port.getTank().getFluid();

        if (stored.isEmpty() || stored.getFluid() != resource.getFluid()) {
            return FluidStack.EMPTY;
        }

        return drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain <= 0) {
            return FluidStack.EMPTY;
        }

        var drained = port.getTank().drain(
                maxDrain,
                action.simulate()
        );

        if (drained.isEmpty()) {
            return FluidStack.EMPTY;
        }

        if (!action.simulate()) {
            port.setChanged();
        }

        return new FluidStack(
                drained.getFluid(),
                Math.toIntExact(drained.getAmount())
        );
    }
}