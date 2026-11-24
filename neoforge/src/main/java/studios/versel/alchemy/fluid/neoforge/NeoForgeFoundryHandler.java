package studios.versel.alchemy.fluid.neoforge;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import studios.versel.alchemy.block.entity.FoundryBlockEntity;

public class NeoForgeFoundryHandler implements IFluidHandler {

    private final FoundryBlockEntity foundryBlockEntity;

    public NeoForgeFoundryHandler(FoundryBlockEntity foundryBlockEntity) {
        this.foundryBlockEntity = foundryBlockEntity;
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

        var fluid = foundryBlockEntity.getTank().getFluid();

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

        return Math.toIntExact(foundryBlockEntity.getTank().getCapacity());
    }

    @Override
    public boolean isFluidValid(int tank, FluidStack stack) {
        if (tank != 0 || stack.isEmpty()) {
            return false;
        }

        var stored = foundryBlockEntity.getTank().getFluid();

        return stored.isEmpty() || stored.getFluid() == stack.getFluid();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
//        if (resource.isEmpty()) {
//            return 0;
//        }
//
//        var stack = dev.architectury.fluid.FluidStack.create(
//                resource.getFluid(),
//                resource.getAmount()
//        );
//
//        long accepted = foundryBlockEntity.getTank().fill(
//                stack,
//                action.simulate()
//        );
//
//        if (accepted > 0 && !action.simulate()) {
//            foundryBlockEntity.setChanged();
//        }
//
//        return Math.toIntExact(accepted);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty()) {
            return FluidStack.EMPTY;
        }

        var stored = foundryBlockEntity.getTank().getFluid();

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

        var drained = foundryBlockEntity.getTank().drain(
                maxDrain,
                action.simulate()
        );

        if (drained.isEmpty()) {
            return FluidStack.EMPTY;
        }

        if (!action.simulate()) {
            foundryBlockEntity.setChanged();
        }

        return new FluidStack(
                drained.getFluid(),
                Math.toIntExact(drained.getAmount())
        );
    }


}