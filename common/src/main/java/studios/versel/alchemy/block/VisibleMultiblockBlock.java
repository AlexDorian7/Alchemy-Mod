package studios.versel.alchemy.block;

import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class VisibleMultiblockBlock extends MultiblockBlock {
    public VisibleMultiblockBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
