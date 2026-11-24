package studios.versel.alchemy.neoforge;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import studios.versel.alchemy.CommonAlchemy;
import studios.versel.alchemy.Constants;
import studios.versel.alchemy.block.entity.ModBlockEntities;
import studios.versel.alchemy.client.ClientAlchemy;
import studios.versel.alchemy.client.renderer.neoforge.NeoForgeFoundryBlockEntityRenderer;

@Mod(Constants.MOD_ID)
public final class AlchemyMod {
    public AlchemyMod(IEventBus modEventBus) {
        // Run our common setup.
        CommonAlchemy.init();

        modEventBus.addListener(NeoForgeCapabilities::register);
        modEventBus.addListener(this::onClientSetup);
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        ClientAlchemy.register();
        BlockEntityRenderers.register(ModBlockEntities.FOUNDRY.get(), NeoForgeFoundryBlockEntityRenderer::new);
    }
}
