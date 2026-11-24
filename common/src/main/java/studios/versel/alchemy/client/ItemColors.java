package studios.versel.alchemy.client;

import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.world.item.Item;
import studios.versel.alchemy.fluid.ModFluids;
import studios.versel.alchemy.item.ModItems;

import java.util.HashMap;

public class ItemColors {

    private static final HashMap<RegistrySupplier<? extends Item>, Integer> COLORS = new HashMap<>();

    static {
        COLORS.put(ModItems.MOLTEN_MITHRIL_BUCKET, ModFluids.MOLTEN_MITHRIL_ATTRIBUTES.getColor());
        COLORS.put(ModItems.MOLTEN_MITHRIL, ModFluids.MOLTEN_MITHRIL_ATTRIBUTES.getColor());
        COLORS.put(ModItems.MOLTEN_IRON_BUCKET, ModFluids.MOLTEN_IRON_ATTRIBUTES.getColor());
        COLORS.put(ModItems.MOLTEN_IRON, ModFluids.MOLTEN_IRON_ATTRIBUTES.getColor());
        COLORS.put(ModItems.MOLTEN_COPPER_BUCKET, ModFluids.MOLTEN_COPPER_ATTRIBUTES.getColor());
        COLORS.put(ModItems.MOLTEN_COPPER, ModFluids.MOLTEN_COPPER_ATTRIBUTES.getColor());
        COLORS.put(ModItems.MOLTEN_GOLD_BUCKET, ModFluids.MOLTEN_GOLD_ATTRIBUTES.getColor());
        COLORS.put(ModItems.MOLTEN_GOLD, ModFluids.MOLTEN_GOLD_ATTRIBUTES.getColor());
    }


    public static void registerItemColors() {

        for (RegistrySupplier<? extends Item> itemSupplier : COLORS.keySet()) {
            Item item = itemSupplier.get();
            int index = item instanceof ArchitecturyBucketItem ? 1 : 0;
            ColorHandlerRegistry.registerItemColors(
                    (itemStack, tintIndex) -> {
                        if (tintIndex == index) return COLORS.get(itemSupplier);
                        return -1;
                    },
                    item
            );
        }
    }
}
