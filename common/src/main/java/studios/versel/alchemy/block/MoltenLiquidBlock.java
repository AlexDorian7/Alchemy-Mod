package studios.versel.alchemy.block;

import com.google.common.collect.UnmodifiableIterator;
import dev.architectury.core.block.ArchitecturyLiquidBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class MoltenLiquidBlock extends ArchitecturyLiquidBlock {
    private final Supplier<Block> solidBlock;
    private final Supplier<Block> flowBlock;

    public MoltenLiquidBlock(Supplier<? extends FlowingFluid> fluid, Supplier<Block> sourceBlock, Properties properties) {
        this(fluid, sourceBlock, () -> Blocks.COBBLESTONE, properties);
    }

    public MoltenLiquidBlock(Supplier<? extends FlowingFluid> fluid, Supplier<Block> sourceBlock, Supplier<Block> flowBlock, Properties properties) {
        super(fluid, properties);
        this.solidBlock = sourceBlock;
        this.flowBlock = flowBlock;
    }

    protected void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (this.shouldSpreadLiquid(level, blockPos, blockState)) {
            level.scheduleTick(blockPos, blockState.getFluidState().getType(), this.fluid.getTickDelay(level));
        }

    }

    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (this.shouldSpreadLiquid(level, blockPos, blockState)) {
            level.scheduleTick(blockPos, blockState.getFluidState().getType(), this.fluid.getTickDelay(level));
        }

    }

    private boolean shouldSpreadLiquid(Level level, BlockPos blockPos, BlockState blockState) {
        for (Direction direction : POSSIBLE_FLOW_DIRECTIONS) {
            BlockPos attemptedFlowPos = blockPos.relative(direction.getOpposite());

            if (level.getFluidState(attemptedFlowPos).is(FluidTags.WATER)) {
                Block block = level.getFluidState(blockPos).isSource() ? solidBlock.get() : flowBlock.get();
                level.setBlockAndUpdate(blockPos, block.defaultBlockState());
                this.fizz(level, blockPos);
                return false;
            }
        }

        return true;
    }

    private void fizz(LevelAccessor levelAccessor, BlockPos blockPos) {
        levelAccessor.levelEvent(1501, blockPos, 0);
    }

}
