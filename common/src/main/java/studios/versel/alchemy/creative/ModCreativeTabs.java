package studios.versel.alchemy.creative;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.item.ModItems;

public final class ModCreativeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Constants.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> ALCHEMY =
            TABS.register("alchemy", () ->
                    CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                            .title(Component.translatable("itemGroup.alchemy"))
                            .icon(() -> new ItemStack(ModItems.ALCHEMY_TABLE.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.ALCHEMY_TABLE.get());
                                output.accept(ModItems.ALCHEMY_BLOCK.get());
                                output.accept(ModItems.ALCHEMY_PORT.get());
                                output.accept(ModItems.FOUNDRY.get());
                                output.accept(ModItems.FOUNDRY_BRICKS.get());
                                output.accept(ModItems.FOUNDRY_PORT.get());
                                output.accept(ModItems.MITHRIL_ORE.get());
                                output.accept(ModItems.DEEPSLATE_MITHRIL_ORE.get());
                                output.accept(ModItems.RAW_MITHRIL_BLOCK.get());
                                output.accept(ModItems.RAW_MITHRIL.get());
                                output.accept(ModItems.MITHRIL_INGOT.get());
                            })
                            .build()
            );

    public static final RegistrySupplier<CreativeModeTab> ALCHEMY_FLUIDS =
            TABS.register("alchemy_fluids", () ->
                    CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                            .title(Component.translatable("itemGroup.alchemy_fluids"))
                            .icon(() -> new ItemStack(ModItems.MOLTEN_MITHRIL_BUCKET.get()))
                            .displayItems((parameters, output) -> {
                                output.accept(ModItems.MOLTEN_MITHRIL_BUCKET.get());
                                output.accept(ModItems.MOLTEN_MITHRIL.get());
                                output.accept(ModItems.MOLTEN_COPPER_BUCKET.get());
                                output.accept(ModItems.MOLTEN_COPPER.get());
                                output.accept(ModItems.MOLTEN_IRON_BUCKET.get());
                                output.accept(ModItems.MOLTEN_IRON.get());
                                output.accept(ModItems.MOLTEN_GOLD_BUCKET.get());
                                output.accept(ModItems.MOLTEN_GOLD.get());
                            })
                            .build()
            );

    public static void register() {
        TABS.register();
    }

    private ModCreativeTabs() {}
}