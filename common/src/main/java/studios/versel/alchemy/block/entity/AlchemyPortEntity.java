package studios.versel.alchemy.block.entity;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AlchemyPortEntity extends PortEntity {

    private static final long TANK_CAPACITY = FluidStack.bucketAmount() * 8;

    public AlchemyPortEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_PORT.get(), TANK_CAPACITY, pos, state);
    }

}