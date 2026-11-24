
package studios.versel.alchemy.item;

import dev.architectury.core.item.ArchitecturyBucketItem;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.fluid.ModFluids;

import java.util.function.Function;

import static studios.versel.alchemy.Constants.MOD_ID;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<BlockItem> ALCHEMY_TABLE =
            item("alchemy_table", blockItem(ModBlocks.ALCHEMY_TABLE));

    public static final RegistrySupplier<BlockItem> ALCHEMY_BLOCK =
            item("alchemy_block", blockItem(ModBlocks.ALCHEMY_BLOCK));

    public static final RegistrySupplier<BlockItem> ALCHEMY_PORT =
            item("alchemy_port", blockItem(ModBlocks.ALCHEMY_PORT));

    public static final RegistrySupplier<BlockItem> RAW_MITHRIL_BLOCK =
            item("raw_mithril_block", blockItem(ModBlocks.RAW_MITHRIL_BLOCK));

    public static final RegistrySupplier<BlockItem> MITHRIL_ORE =
            item("mithril_ore", blockItem(ModBlocks.MITHRIL_ORE));

    public static final RegistrySupplier<BlockItem> DEEPSLATE_MITHRIL_ORE =
            item("deepslate_mithril_ore", blockItem(ModBlocks.DEEPSLATE_MITHRIL_ORE));

    public static final RegistrySupplier<BlockItem> FOUNDRY =
            item("foundry", blockItem(ModBlocks.FOUNDRY));
    public static final RegistrySupplier<BlockItem> FOUNDRY_BRICKS =
            item("foundry_bricks", blockItem(ModBlocks.FOUNDRY_BRICKS));
    public static final RegistrySupplier<BlockItem> FOUNDRY_PORT =
            item("foundry_port", blockItem(ModBlocks.FOUNDRY_PORT));

    public static final RegistrySupplier<Item> RAW_MITHRIL = item("raw_mithril", Item::new);
    public static final RegistrySupplier<Item> MITHRIL_INGOT = item("mithril_ingot", Item::new);

    public static final RegistrySupplier<ArchitecturyBucketItem> MOLTEN_MITHRIL_BUCKET = item("molten_mithril_bucket", bucketItem(ModFluids.MOLTEN_MITHRIL_STILL));
    public static final RegistrySupplier<BlockItem> MOLTEN_MITHRIL = item("molten_mithril", blockItem(ModBlocks.MOLTEN_MITHRIL_FLUID_BLOCK));

    public static final RegistrySupplier<ArchitecturyBucketItem> MOLTEN_IRON_BUCKET = item("molten_iron_bucket", bucketItem(ModFluids.MOLTEN_IRON_STILL));
    public static final RegistrySupplier<BlockItem> MOLTEN_IRON = item("molten_iron", blockItem(ModBlocks.MOLTEN_IRON_FLUID_BLOCK));

    public static final RegistrySupplier<ArchitecturyBucketItem> MOLTEN_COPPER_BUCKET = item("molten_copper_bucket", bucketItem(ModFluids.MOLTEN_COPPER_STILL));
    public static final RegistrySupplier<BlockItem> MOLTEN_COPPER = item("molten_copper", blockItem(ModBlocks.MOLTEN_COPPER_FLUID_BLOCK));

    public static final RegistrySupplier<ArchitecturyBucketItem> MOLTEN_GOLD_BUCKET = item("molten_gold_bucket", bucketItem(ModFluids.MOLTEN_GOLD_STILL));
    public static final RegistrySupplier<BlockItem> MOLTEN_GOLD = item("molten_gold", blockItem(ModBlocks.MOLTEN_GOLD_FLUID_BLOCK));


    public static Function<Item.Properties, BlockItem> blockItem(
            RegistrySupplier<? extends Block> block
    ) {
        return properties -> new BlockItem(block.get(), properties);
    }

    public static Function<Item.Properties, ArchitecturyBucketItem> bucketItem(
            RegistrySupplier<? extends Fluid> fluid
    ) {
        return properties -> new ArchitecturyBucketItem(fluid, properties);
    }

    public static <T extends Item> RegistrySupplier<T> item(
            String name,
            Function<Item.Properties, T> factory
    ) {
        return ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    public static void register() {
        ITEMS.register();
    }

    @SuppressWarnings("unchecked")
    public static RegistrySupplier<Item> asItem(RegistrySupplier<? extends Item> item) {
        return (RegistrySupplier<Item>) item;
    }
}
