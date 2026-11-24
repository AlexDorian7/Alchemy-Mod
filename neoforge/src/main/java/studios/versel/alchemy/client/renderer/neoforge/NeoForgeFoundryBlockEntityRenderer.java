package studios.versel.alchemy.client.renderer.neoforge;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import studios.versel.alchemy.client.renderer.FoundryBlockEntityRenderer;
import studios.versel.alchemy.block.entity.FoundryBlockEntity;

public class NeoForgeFoundryBlockEntityRenderer extends FoundryBlockEntityRenderer {

    public NeoForgeFoundryBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public AABB getRenderBoundingBox(FoundryBlockEntity blockEntity) {
        return super.getRenderBoundingBox(blockEntity);
    }
}