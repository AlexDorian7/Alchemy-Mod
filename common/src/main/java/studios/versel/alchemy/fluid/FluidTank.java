package studios.versel.alchemy.fluid;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.Optional;

public class FluidTank {
    private final long capacity;
    private final Optional<Runnable> changed;
    private FluidStack fluid = FluidStack.empty();

    public FluidTank(long capacity, Optional<Runnable> changed) {
        this.changed = changed;
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }

        this.capacity = capacity;
    }

    public long getCapacity() {
        return capacity;
    }

    public FluidStack getFluid() {
        return fluid.copy();
    }

    public long getAmount() {
        return fluid.getAmount();
    }

    public boolean isEmpty() {
        return fluid.isEmpty();
    }

    public long fill(FluidStack resource, boolean simulate) {
        if (resource.isEmpty()) {
            return 0;
        }

        if (!fluid.isEmpty() && !fluid.isFluidEqual(resource)) {
            return 0;
        }

        long accepted = Math.min(
                resource.getAmount(),
                capacity - fluid.getAmount()
        );

        if (accepted <= 0) {
            return 0;
        }

        if (!simulate) {
            if (fluid.isEmpty()) {
                fluid = resource.copyWithAmount(accepted);
            } else {
                fluid.grow(accepted);
            }

            onChanged();
        }
        return accepted;
    }

    public FluidStack drain(long amount, boolean simulate) {
        if (amount <= 0 || fluid.isEmpty()) {
            return FluidStack.empty();
        }

        long drained = Math.min(amount, fluid.getAmount());
        FluidStack result = fluid.copyWithAmount(drained);

        if (!simulate) {
            fluid.shrink(drained);

            if (fluid.isEmpty()) {
                fluid = FluidStack.empty();
            }

            onChanged();
        }

        return result;
    }

    public void setFluid(FluidStack fluid) {
        if (fluid.isEmpty()) {
            this.fluid = FluidStack.empty();
            onChanged();
            return;
        }

        if (fluid.getAmount() > capacity) {
            throw new IllegalArgumentException("Fluid exceeds tank capacity");
        }

        this.fluid = fluid.copy();
        onChanged();
    }

    public void writeNbt(HolderLookup.Provider provider, CompoundTag tag) {
        if (fluid.isEmpty()) {
            return;
        }

        tag.put("Fluid", fluid.write(provider, new CompoundTag()));
    }

    public void readNbt(HolderLookup.Provider provider, CompoundTag tag) {
        if (!tag.contains("Fluid", Tag.TAG_COMPOUND)) {
            fluid = FluidStack.empty();
            return;
        }

        fluid = FluidStack.read(provider, tag.getCompound("Fluid")).orElseThrow();

        if (fluid.getAmount() > capacity) {
            fluid = fluid.copyWithAmount(capacity);
        }
    }

    private void onChanged() {
        changed.ifPresent(Runnable::run);
    }
}