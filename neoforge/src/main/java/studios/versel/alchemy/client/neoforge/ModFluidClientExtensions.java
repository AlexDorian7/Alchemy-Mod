package studios.versel.alchemy.client.neoforge;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.fluid.ModFluids;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;


/**
 * Fix a bug in Architectury that failed to register fluids
 * <p>
 * See also <a href="github.com/architectury/architectury-api/issues/551">github.com/architectury/architectury-api/issues/551</a>
 */
@EventBusSubscriber(
        modid = Constants.MOD_ID,
        value = Dist.CLIENT
)
public final class ModFluidClientExtensions {

    @SubscribeEvent
    public static void registerFluidExtensions(RegisterClientExtensionsEvent event) {
        Set<FluidType> registeredTypes = new HashSet<>();

        for (RegistrySupplier<Fluid> fluid : ModFluids.FLUIDS) {
            Fluid instance = fluid.get();

            if (!(instance instanceof ArchitecturyFlowingFluid.Source)) {
                continue;
            }

            FluidType type = instance.getFluidType();

            if (!registeredTypes.add(type)) {
                continue;
            }

            try {
                Method initializeClient = type.getClass()
                        .getDeclaredMethod("initializeClient", Consumer.class);

                initializeClient.setAccessible(true);

                initializeClient.invoke(type,
                        (Consumer<net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions>)
                                extension -> event.registerFluidType(extension, type));

            } catch (ReflectiveOperationException exception) {
                throw new RuntimeException(
                        "Failed to register a fluid client extension for " + fluid,
                        exception
                );
            }
        }
    }

    private ModFluidClientExtensions() {}
}