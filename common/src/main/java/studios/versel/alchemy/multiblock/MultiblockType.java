package studios.versel.alchemy.multiblock;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.function.Function;
import java.util.function.Predicate;

public final class MultiblockType {

    private final StructureDefinition definition;
    private final Predicate<BlockState> controllerPredicate;
    private final Function<BlockState, Direction> facingGetter;
    private final Property<Boolean> completedProperty;

    public MultiblockType(
            StructureDefinition definition,
            Predicate<BlockState> controllerPredicate,
            Function<BlockState, Direction> facingGetter,
            Property<Boolean> completedProperty
    ) {
        this.definition = definition;
        this.controllerPredicate = controllerPredicate;
        this.facingGetter = facingGetter;
        this.completedProperty = completedProperty;
    }

    public StructureDefinition getDefinition() {
        return definition;
    }

    public boolean isController(BlockState state) {
        return controllerPredicate.test(state);
    }

    public Direction getFacing(BlockState state) {
        return facingGetter.apply(state);
    }

    public Property<Boolean> getCompletedProperty() {
        return completedProperty;
    }
}