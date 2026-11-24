package studios.versel.alchemy.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Supplier;

public final class StructureDefinition {

    private final Map<BlockPos, Set<Supplier<Block>>> blocks;
    private final Set<BlockPos> optionalPositions;

    private StructureDefinition(
            Map<BlockPos, Set<Supplier<Block>>> blocks,
            Set<BlockPos> optionalPositions
    ) {
        Map<BlockPos, Set<Supplier<Block>>> immutableBlocks = new LinkedHashMap<>();

        blocks.forEach((pos, allowedBlocks) ->
                immutableBlocks.put(
                        pos.immutable(),
                        Set.copyOf(allowedBlocks)
                )
        );

        this.blocks = Collections.unmodifiableMap(immutableBlocks);
        this.optionalPositions = Set.copyOf(optionalPositions);
    }

    /**
     * Gets all required and optional block positions.
     */
    public Map<BlockPos, Set<Supplier<Block>>> getBlocks() {
        return blocks;
    }

    /**
     * Gets the allowed blocks at a relative position.
     */
    public Set<Supplier<Block>> getAllowedBlocks(BlockPos pos) {
        return blocks.getOrDefault(pos, Set.of());
    }

    /**
     * Checks whether a position is optional.
     */
    public boolean isOptional(BlockPos pos) {
        return optionalPositions.contains(pos);
    }

    /**
     * Gets all optional positions.
     */
    public Set<BlockPos> getOptionalPositions() {
        return optionalPositions;
    }

    /**
     * Gets the total number of defined positions.
     */
    public int size() {
        return blocks.size();
    }

    /**
     * Gets the number of required positions.
     */
    public int requiredSize() {
        return blocks.size() - optionalPositions.size();
    }

    /**
     * Checks whether a block state is accepted at a position.
     */
    public boolean matches(BlockPos pos, BlockState state) {
        Set<Supplier<Block>> allowedBlocks = blocks.get(pos);

        if (allowedBlocks == null) {
            return false;
        }

        return allowedBlocks.stream().anyMatch(supplier -> state.is(supplier.get()));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private final Map<BlockPos, Set<Supplier<Block>>> blocks = new LinkedHashMap<>();
        private final Set<BlockPos> optionalPositions = new HashSet<>();

        private Builder() {}

        /**
         * Adds a required block at a relative position.
         */
        public Builder add(int x, int y, int z, Supplier<Block> block) {
            return add(new BlockPos(x, y, z), block);
        }

        /**
         * Adds a required block at a relative position.
         */
        public Builder add(BlockPos pos, Supplier<Block> block) {
            Objects.requireNonNull(block, "Block cannot be null");

            BlockPos key = pos.immutable();

            blocks.put(key, new LinkedHashSet<>(Set.of(block)));
            optionalPositions.remove(key);

            return this;
        }

        /**
         * Removes a required block at a relative position.
         */
        public Builder remove(int x, int y, int z) {

            return remove(new BlockPos(x,y,z));
        }

        /**
         * Removes a required block at a relative position.
         */
        public Builder remove(BlockPos pos) {

            BlockPos key = pos.immutable();

            blocks.remove(key);
            optionalPositions.remove(key);

            return this;
        }

        /**
         * Adds multiple accepted blocks at a position.
         * Any of the supplied blocks will satisfy validation.
         */
        public Builder add(
                int x,
                int y,
                int z,
                Supplier<Block>... allowedBlocks
        ) {
            return add(new BlockPos(x, y, z), allowedBlocks);
        }

        /**
         * Adds multiple accepted blocks at a position.
         */
        public Builder add(BlockPos pos, Supplier<Block>... allowedBlocks) {
            Objects.requireNonNull(allowedBlocks, "Blocks cannot be null");

            if (allowedBlocks.length == 0) {
                throw new IllegalArgumentException(
                        "At least one block must be supplied"
                );
            }

            BlockPos key = pos.immutable();

            Set<Supplier<Block>> accepted = new LinkedHashSet<>();

            for (Supplier<Block> block : allowedBlocks) {
                accepted.add(Objects.requireNonNull(block));
            }

            blocks.put(key, accepted);
            optionalPositions.remove(key);

            return this;
        }

        /**
         * Adds an optional block position.
         *
         * The position may contain one of the supplied blocks,
         * or be empty.
         */
        public Builder addOptional(
                int x,
                int y,
                int z,
                Supplier<Block>... allowedBlocks
        ) {
            return addOptional(
                    new BlockPos(x, y, z),
                    allowedBlocks
            );
        }

        /**
         * Adds an optional block position.
         */
        public Builder addOptional(
                BlockPos pos,
                Supplier<Block>... allowedBlocks
        ) {
            add(pos, allowedBlocks);
            optionalPositions.add(pos.immutable());

            return this;
        }

        /**
         * Fills a rectangular region with a block.
         *
         * Existing positions are overwritten.
         */
        public Builder fill(
                int minX, int minY, int minZ,
                int maxX, int maxY, int maxZ,
                Supplier<Block> block
        ) {
            Objects.requireNonNull(block, "Block cannot be null");

            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        add(x, y, z, block);
                    }
                }
            }

            return this;
        }

        /**
         * Fills a rectangular region with multiple accepted blocks.
         */
        public Builder fill(
                int minX, int minY, int minZ,
                int maxX, int maxY, int maxZ,
                Supplier<Block>... allowedBlocks
        ) {
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        add(x, y, z, allowedBlocks);
                    }
                }
            }

            return this;
        }

        /**
         * Builds the immutable structure definition.
         */
        public StructureDefinition build() {
            if (blocks.isEmpty()) {
                throw new IllegalStateException(
                        "Structure definition cannot be empty"
                );
            }

            return new StructureDefinition(blocks, optionalPositions);
        }
    }
}