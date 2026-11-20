package studios.versel.alchemy.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;
import java.util.Set;

public final class StructureValidator {

    private StructureValidator() {}

    /**
     * Checks whether a structure exists at the specified position and facing.
     *
     * The origin represents local coordinate (0, 0, 0).
     */
    public static boolean validate(
            Level level,
            BlockPos origin,
            Direction facing,
            StructureDefinition definition
    ) {
        if (level == null || origin == null || facing == null || definition == null) {
            return false;
        }

        if (!facing.getAxis().isHorizontal()) {
            return false;
        }

        for (Map.Entry<BlockPos, Set<net.minecraft.world.level.block.Block>> entry
                : definition.getBlocks().entrySet()) {

            BlockPos localPos = entry.getKey();
            BlockPos worldPos = toWorldPos(origin, localPos, facing);

            BlockState state = level.getBlockState(worldPos);

            // Optional positions are allowed to be empty.
            if (definition.isOptional(localPos) && state.isAir()) {
                continue;
            }

            if (!definition.matches(localPos, state)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts a local structure position into a world position.
     *
     * X = right relative to facing
     * Y = up
     * Z = backward relative to facing
     */
    public static BlockPos toWorldPos(
            BlockPos origin,
            BlockPos localPos,
            Direction facing
    ) {
        return origin
                .relative(facing.getClockWise(), localPos.getX())
                .relative(Direction.UP, localPos.getY())
                .relative(facing.getOpposite(), localPos.getZ());
    }
}