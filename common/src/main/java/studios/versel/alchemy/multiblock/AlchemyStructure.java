package studios.versel.alchemy.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import studios.versel.alchemy.block.AlchemyTable;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.block.states.ModBlockProperties;

import java.util.*;

public final class AlchemyStructure {

    public static final StructureDefinition ALCHEMY_TABLE =
            StructureDefinition.builder()
                    .fill(-1, -1, 0, 1, 1, 1, ModBlocks.ALCHEMY_BLOCK.value())
                    .add(0, 0, 0, ModBlocks.ALCHEMY_TABLE.value())
                    .add(-1, -1, 0, ModBlocks.ALCHEMY_PORT.value())
                    .add(1, -1, 0, ModBlocks.ALCHEMY_PORT.value())
                    .build();

    private AlchemyStructure() {}

    public static void updateAround(Level level, BlockPos changedPos) {
        updateAround(level, changedPos, null);
    }

    public static void updateAround(
            Level level,
            BlockPos changedPos,
            Direction removedTableFacing
    ) {
        if (level.isClientSide()) {
            return;
        }

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        Map<BlockPos, Candidate> candidates = new HashMap<>();

        queue.add(changedPos.immutable());

        if (removedTableFacing != null) {
            addFootprint(queue, changedPos, removedTableFacing);
        }

        /*
         * Discover candidate structures.
         */
        while (!queue.isEmpty()) {
            BlockPos anchor = queue.poll();

            if (!visited.add(anchor)) {
                continue;
            }

            for (Direction facing : Direction.Plane.HORIZONTAL) {
                for (BlockPos offset : ALCHEMY_TABLE.getBlocks().keySet()) {

                    BlockPos tablePos = anchor
                            .relative(facing.getClockWise(), -offset.getX())
                            .relative(Direction.UP, -offset.getY())
                            .relative(facing, offset.getZ());

                    BlockState state = level.getBlockState(tablePos);

                    if (!state.is(ModBlocks.ALCHEMY_TABLE.value())) {
                        continue;
                    }

                    Direction actualFacing =
                            state.getValue(HorizontalDirectionalBlock.FACING);

                    if (candidates.containsKey(tablePos)) {
                        continue;
                    }

                    Candidate candidate =
                            new Candidate(tablePos.immutable(), actualFacing);

                    candidates.put(tablePos.immutable(), candidate);

                    // Search the candidate's entire footprint for other tables.
                    addFootprint(queue, tablePos, actualFacing);
                }
            }
        }

        /*
         * Physically validate every discovered candidate.
         */
        List<Candidate> validCandidates = new ArrayList<>();

        for (Candidate candidate : candidates.values()) {
            if (StructureValidator.validate(
                    level,
                    candidate.pos,
                    candidate.facing,
                    ALCHEMY_TABLE
            )) {
                validCandidates.add(candidate);
            }
        }

        /*
         * Detect overlapping structures.
         * Both structures are rejected when they overlap.
         */
        Set<Candidate> conflicting = new HashSet<>();

        for (int i = 0; i < validCandidates.size(); i++) {
            Candidate first = validCandidates.get(i);

            Set<BlockPos> firstPositions =
                    getFootprint(first.pos, first.facing);

            for (int j = i + 1; j < validCandidates.size(); j++) {
                Candidate second = validCandidates.get(j);

                Set<BlockPos> secondPositions =
                        getFootprint(second.pos, second.facing);

                if (!Collections.disjoint(firstPositions, secondPositions)) {
                    conflicting.add(first);
                    conflicting.add(second);
                }
            }
        }

        /*
         * Collect completed structure positions.
         */
        Set<BlockPos> completedPositions = new HashSet<>();

        for (Candidate candidate : validCandidates) {
            if (!conflicting.contains(candidate)) {
                completedPositions.addAll(
                        getFootprint(candidate.pos, candidate.facing)
                );
            }
        }

        /*
         * Synchronize affected blocks.
         */
        Set<BlockPos> affectedPositions = new HashSet<>(visited);

        for (Candidate candidate : candidates.values()) {
            affectedPositions.addAll(
                    getFootprint(candidate.pos, candidate.facing)
            );
        }

        for (BlockPos pos : affectedPositions) {
            BlockState state = level.getBlockState(pos);

            boolean completed = completedPositions.contains(pos);

            if (state.hasProperty(ModBlockProperties.COMPLETED)) {
                if (state.getValue(ModBlockProperties.COMPLETED) != completed) {
                    level.setBlock(
                            pos,
                            state.setValue(ModBlockProperties.COMPLETED, completed),
                            Block.UPDATE_CLIENTS
                    );
                }
            }
        }
    }

    /**
     * Adds every position in the structure definition to the search queue.
     */
    private static void addFootprint(
            Queue<BlockPos> queue,
            BlockPos origin,
            Direction facing
    ) {
        for (BlockPos offset : ALCHEMY_TABLE.getBlocks().keySet()) {
            queue.add(
                    StructureValidator.toWorldPos(origin, offset, facing)
            );
        }
    }

    /**
     * Gets every world position occupied by the structure definition.
     */
    private static Set<BlockPos> getFootprint(
            BlockPos origin,
            Direction facing
    ) {
        Set<BlockPos> positions = new HashSet<>();

        for (BlockPos offset : ALCHEMY_TABLE.getBlocks().keySet()) {
            positions.add(
                    StructureValidator.toWorldPos(origin, offset, facing)
            );
        }

        return positions;
    }

    private record Candidate(BlockPos pos, Direction facing) {}
}