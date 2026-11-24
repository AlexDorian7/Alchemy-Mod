package studios.versel.alchemy.fluid;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

import static studios.versel.alchemy.Constants.MOD_ID;

public final class ModFluids {

    public static final DeferredRegister<Fluid> FLUIDS =
            DeferredRegister.create(MOD_ID, Registries.FLUID);

    private static Fluid getFlowingMithril() {
        return MOLTEN_MITHRIL_FLOWING.get();
    }

    private static Fluid getStillMithril() {
        return MOLTEN_MITHRIL_STILL.get();
    }

    private static Fluid getFlowingIron() {
        return MOLTEN_IRON_FLOWING.get();
    }

    private static Fluid getStillIron() {
        return MOLTEN_IRON_STILL.get();
    }

    private static Fluid getFlowingCopper() {
        return MOLTEN_COPPER_FLOWING.get();
    }

    private static Fluid getStillCopper() {
        return MOLTEN_COPPER_STILL.get();
    }

    private static Fluid getFlowingGold() {
        return MOLTEN_GOLD_FLOWING.get();
    }

    private static Fluid getStillGold() {
        return MOLTEN_GOLD_STILL.get();
    }


    public static final ArchitecturyFluidAttributes MOLTEN_MITHRIL_ATTRIBUTES = new MoltenMithrilFluidAttributes(ModFluids::getFlowingMithril, ModFluids::getStillMithril);

    public static final RegistrySupplier<ArchitecturyFlowingFluid.Source> MOLTEN_MITHRIL_STILL = FLUIDS.register("molten_mithril", () -> new ArchitecturyFlowingFluid.Source(MOLTEN_MITHRIL_ATTRIBUTES));
    public static final RegistrySupplier<ArchitecturyFlowingFluid.Flowing> MOLTEN_MITHRIL_FLOWING = FLUIDS.register("flowing_molten_mithril", () -> new ArchitecturyFlowingFluid.Flowing(MOLTEN_MITHRIL_ATTRIBUTES));


    public static final ArchitecturyFluidAttributes MOLTEN_IRON_ATTRIBUTES = new MoltenIronFluidAttributes(ModFluids::getFlowingIron, ModFluids::getStillIron);

    public static final RegistrySupplier<ArchitecturyFlowingFluid.Source> MOLTEN_IRON_STILL = FLUIDS.register("molten_iron", () -> new ArchitecturyFlowingFluid.Source(MOLTEN_IRON_ATTRIBUTES));
    public static final RegistrySupplier<ArchitecturyFlowingFluid.Flowing> MOLTEN_IRON_FLOWING = FLUIDS.register("flowing_molten_iron", () -> new ArchitecturyFlowingFluid.Flowing(MOLTEN_IRON_ATTRIBUTES));


    public static final ArchitecturyFluidAttributes MOLTEN_COPPER_ATTRIBUTES = new MoltenCopperFluidAttributes(ModFluids::getFlowingCopper, ModFluids::getStillCopper);

    public static final RegistrySupplier<ArchitecturyFlowingFluid.Source> MOLTEN_COPPER_STILL = FLUIDS.register("molten_copper", () -> new ArchitecturyFlowingFluid.Source(MOLTEN_COPPER_ATTRIBUTES));
    public static final RegistrySupplier<ArchitecturyFlowingFluid.Flowing> MOLTEN_COPPER_FLOWING = FLUIDS.register("flowing_molten_copper", () -> new ArchitecturyFlowingFluid.Flowing(MOLTEN_COPPER_ATTRIBUTES));

    public static final ArchitecturyFluidAttributes MOLTEN_GOLD_ATTRIBUTES = new MoltenGoldFluidAttributes(ModFluids::getFlowingGold, ModFluids::getStillGold);

    public static final RegistrySupplier<ArchitecturyFlowingFluid.Source> MOLTEN_GOLD_STILL = FLUIDS.register("molten_gold", () -> new ArchitecturyFlowingFluid.Source(MOLTEN_GOLD_ATTRIBUTES));
    public static final RegistrySupplier<ArchitecturyFlowingFluid.Flowing> MOLTEN_GOLD_FLOWING = FLUIDS.register("flowing_molten_gold", () -> new ArchitecturyFlowingFluid.Flowing(MOLTEN_GOLD_ATTRIBUTES));



    public static void register() {
        FLUIDS.register();
    }

    private ModFluids() {}
}