
package studios.versel.alchemy.item;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import studios.versel.alchemy.block.ModBlocks;

import java.util.function.Function;

import static studios.versel.alchemy.Constants.MOD_ID;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<BlockItem> AlchemyTable =
            item("alchemy_table", blockItem(ModBlocks.ALCHEMY_TABLE));

    public static final RegistrySupplier<BlockItem> AlchemyBlock =
            item("alchemy_block", blockItem(ModBlocks.ALCHEMY_BLOCK));

    public static final RegistrySupplier<BlockItem> AlchemyPort =
            item("alchemy_port", blockItem(ModBlocks.ALCHEMY_PORT));

    public static Function<Item.Properties, BlockItem> blockItem(
            RegistrySupplier<? extends Block> block
    ) {
        return properties -> new BlockItem(block.get(), properties);
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
}
