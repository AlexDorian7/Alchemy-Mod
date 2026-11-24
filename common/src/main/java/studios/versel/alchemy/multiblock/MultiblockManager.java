package studios.versel.alchemy.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.*;

public final class MultiblockManager {

    private static final Set<MultiblockType> TYPES = new HashSet<>();

    private MultiblockManager() {}

    public static void register(MultiblockType type) {
        TYPES.add(type);
    }

    public static void updateAround(Level level, BlockPos changedPos) {
        updateAround(level, changedPos, null);
    }

    public static void updateAround(
            Level level,
            BlockPos changedPos,
            Direction removedFacing
    ) {
        if (level.isClientSide()) {
            return;
        }

        Queue<BlockPos> queue = new ArrayDeque<>();
        Set<BlockPos> visited = new HashSet<>();
        Set<Candidate> candidates = new HashSet<>();

        queue.add(changedPos.immutable());

        if (removedFacing != null) {
            for (MultiblockType type : TYPES) {
                addFootprint(queue, changedPos, removedFacing, type);
            }
        }

        /*
         * Discover candidate structures.
         */
        while (!queue.isEmpty()) {
            BlockPos anchor = queue.poll();

            if (!visited.add(anchor)) {
                continue;
            }

            for (MultiblockType type : TYPES) {
                StructureDefinition definition = type.getDefinition();

                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    for (BlockPos offset : definition.getBlocks().keySet()) {

                        BlockPos controllerPos = anchor
                                .relative(facing.getClockWise(), -offset.getX())
                                .relative(Direction.UP, -offset.getY())
                                .relative(facing, offset.getZ());

                        BlockState controllerState =
                                level.getBlockState(controllerPos);

                        if (!type.isController(controllerState)) {
                            continue;
                        }

                        Direction actualFacing = type.getFacing(controllerState);

                        if (actualFacing == null
                                || !actualFacing.getAxis().isHorizontal()) {
                            continue;
                        }

                        Candidate candidate = new Candidate(
                                type,
                                controllerPos.immutable(),
                                actualFacing
                        );

                        if (candidates.add(candidate)) {
                            addFootprint(
                                    queue,
                                    controllerPos,
                                    actualFacing,
                                    type
                            );
                        }
                    }
                }
            }
        }

        /*
         * Physically validate every discovered candidate.
         */
        List<Candidate> validCandidates = new ArrayList<>();

        for (Candidate candidate : candidates) {
            if (StructureValidator.validate(
                    level,
                    candidate.pos(),
                    candidate.facing(),
                    candidate.type().getDefinition()
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

            Set<BlockPos> firstPositions = getFootprint(
                    first.pos(),
                    first.facing(),
                    first.type()
            );

            for (int j = i + 1; j < validCandidates.size(); j++) {
                Candidate second = validCandidates.get(j);

                Set<BlockPos> secondPositions = getFootprint(
                        second.pos(),
                        second.facing(),
                        second.type()
                );

                if (!Collections.disjoint(firstPositions, secondPositions)) {
                    conflicting.add(first);
                    conflicting.add(second);
                }
            }
        }

        /*
         * Collect completed positions, grouped by completion property.
         */
        Map<Property<Boolean>, Set<BlockPos>> completedPositions =
                new HashMap<>();

        for (Candidate candidate : validCandidates) {
            if (conflicting.contains(candidate)) {
                continue;
            }

            Property<Boolean> property =
                    candidate.type().getCompletedProperty();

            completedPositions.computeIfAbsent(
                    property,
                    ignored -> new HashSet<>()
            ).addAll(
                    getFootprint(
                            candidate.pos(),
                            candidate.facing(),
                            candidate.type()
                    )
            );
        }

        /*
         * Synchronize affected blocks.
         */
        for (MultiblockType type : TYPES) {
            Property<Boolean> property = type.getCompletedProperty();

            Set<BlockPos> completed = completedPositions.getOrDefault(
                    property,
                    Collections.emptySet()
            );

            Set<BlockPos> affected = new HashSet<>(visited);

            for (Candidate candidate : candidates) {
                if (candidate.type() == type) {
                    affected.addAll(
                            getFootprint(
                                    candidate.pos(),
                                    candidate.facing(),
                                    type
                            )
                    );
                }
            }

            for (BlockPos pos : affected) {
                BlockState state = level.getBlockState(pos);

                if (!state.hasProperty(property)) {
                    continue;
                }

                boolean isCompleted = completed.contains(pos);

                if (state.getValue(property) != isCompleted) {
                    level.setBlock(
                            pos,
                            state.setValue(property, isCompleted),
                            Block.UPDATE_CLIENTS
                    );
                }
            }
        }
    }

    private static void addFootprint(
            Queue<BlockPos> queue,
            BlockPos origin,
            Direction facing,
            MultiblockType type
    ) {
        for (BlockPos offset : type.getDefinition().getBlocks().keySet()) {
            queue.add(
                    StructureValidator.toWorldPos(origin, offset, facing)
            );
        }
    }

    private static Set<BlockPos> getFootprint(
            BlockPos origin,
            Direction facing,
            MultiblockType type
    ) {
        Set<BlockPos> positions = new HashSet<>();

        for (BlockPos offset : type.getDefinition().getBlocks().keySet()) {
            positions.add(
                    StructureValidator.toWorldPos(origin, offset, facing)
            );
        }

        return positions;
    }

    private record Candidate(
            MultiblockType type,
            BlockPos pos,
            Direction facing
    ) {}
}