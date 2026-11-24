package studios.versel.alchemy.client;

import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import studios.versel.alchemy.block.entity.ModBlockEntities;
import studios.versel.alchemy.client.renderer.FoundryBlockEntityRenderer;

public class ClientAlchemy {

    private static void registerRenderers() {
        BlockEntityRendererRegistry.register(
                ModBlockEntities.FOUNDRY.get(),
                FoundryBlockEntityRenderer::new
        );
    }

    public static void register() {
        ItemColors.registerItemColors();
        registerRenderers();
    }
}
