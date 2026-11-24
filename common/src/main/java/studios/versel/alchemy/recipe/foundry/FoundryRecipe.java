package studios.versel.alchemy.recipe.foundry;

import dev.architectury.fluid.FluidStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import studios.versel.alchemy.fluid.FluidTank;
import studios.versel.alchemy.fluid.ModFluids;
import studios.versel.alchemy.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public record FoundryRecipe(Supplier<ItemStack> input, Supplier<FluidStack> output, int processTime, long fuelPerTick) {

    private static final ArrayList<FoundryRecipe> registry = new ArrayList<>();

    public static final FoundryRecipe MOLTEN_MITHRIL = register(new FoundryRecipe(() -> ModItems.RAW_MITHRIL_BLOCK.get().getDefaultInstance(), () -> FluidStack.create(ModFluids.MOLTEN_MITHRIL_STILL.get(), FluidStack.bucketAmount()), 200, milibucketAmount(5)));
    public static final FoundryRecipe MOLTEN_IRON = register(new FoundryRecipe(Items.IRON_BLOCK::getDefaultInstance, () -> FluidStack.create(ModFluids.MOLTEN_IRON_STILL.get(), FluidStack.bucketAmount()), 200, milibucketAmount(5)));
    public static final FoundryRecipe MOLTEN_COPPER = register(new FoundryRecipe(Items.COPPER_BLOCK::getDefaultInstance, () -> FluidStack.create(ModFluids.MOLTEN_COPPER_STILL.get(), FluidStack.bucketAmount()), 200, milibucketAmount(5)));
    public static final FoundryRecipe MOLTEN_GOLD = register(new FoundryRecipe(Items.GOLD_BLOCK::getDefaultInstance, () -> FluidStack.create(ModFluids.MOLTEN_GOLD_STILL.get(), FluidStack.bucketAmount()), 200, milibucketAmount(5)));

    private static FoundryRecipe register(FoundryRecipe foundryRecipe) {
        registry.add(foundryRecipe);
        return foundryRecipe;
    }

    public static List<FoundryRecipe> getRecipes() {
        return registry;
    }

    public boolean match(ItemStack match) {
        return match.is(input.get().getItem()) && match.getCount() >= input.get().getCount();
    }

    public void use(ItemStack stack) {
        stack.consume(input.get().getCount(), null);
    }

    /**
     * This needs to be called once per tick
     * @param fuelTank The tank that contains fuel
     * @param inputStack The input item stack to work on
     * @param outputTank The tank the output fluid goes into
     * @param startTick The tick this process was started
     * @param currentTick The current tick
     * @return If processing this tick was successful
     */
    public ProcessResult process(FluidTank fuelTank, ItemStack inputStack, FluidTank outputTank, long startTick, long currentTick) {
        long fill = outputTank.fill(output.get(), true);
        if (fill != output.get().getAmount()) return ProcessResult.FAIL; // not enough space for full output do nothing
        if (!match(inputStack)) return ProcessResult.FAIL; // Input is not correct for this recipe
        if (fuelTank.isEmpty()) return ProcessResult.FAIL; // No fuel
        if (fuelTank.getAmount() < fuelPerTick) return ProcessResult.FAIL; // Not enough Fuel
        if (!fuelTank.getFluid().getFluid().is(FluidTags.LAVA)) return ProcessResult.FAIL; // Need more heat

        boolean finished = (currentTick - startTick + 1L) >= processTime;
        fuelTank.drain(fuelPerTick, false);
        if (finished) {
            inputStack.consume(input.get().getCount(), null);
            outputTank.fill(output.get().copy(), false);
            return ProcessResult.SUCCESS;
        }
        return ProcessResult.CONTINUE;
    }

    public static long milibucketAmount(long milibuckets) {
        return FluidStack.bucketAmount()/1000 * milibuckets;
    }

    public enum ProcessResult {
        FAIL,
        SUCCESS,
        CONTINUE;
    }

}
