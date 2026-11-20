package studios.versel.alchemy.menu;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import studios.versel.alchemy.block.ModBlocks;
import studios.versel.alchemy.block.states.ModBlockProperties;

public class AlchemyMenu extends AbstractContainerMenu {

    private final BlockPos tablePosition;
    private final ContainerData data;

    // Server
    public AlchemyMenu(
            int containerId,
            Inventory inventory,
            BlockPos tablePosition,
            ContainerData data
    ) {
        super(ModMenus.ALCHEMY_MENU.get(), containerId);

        this.tablePosition = tablePosition;
        this.data = data;

        addDataSlots(data);
    }

    // Client
    public AlchemyMenu(
            int containerId,
            Inventory inventory,
            FriendlyByteBuf buffer
    ) {
        this( // Call server constructor with data received from byte buff
                containerId,
                inventory,
                buffer.readBlockPos(),
                new SimpleContainerData(18)
        );
    }

    public BlockPos getTablePosition() {
        return tablePosition;
    }

    public static BlockPos getLeftPortPos(BlockPos tablePos, Direction facing) {
        return tablePos
                .relative(Direction.DOWN)
                .relative(facing.getCounterClockWise());
    }

    public static BlockPos getRightPortPos(BlockPos tablePos, Direction facing) {
        return tablePos
                .relative(Direction.DOWN)
                .relative(facing.getClockWise());
    }

    private static final int VALUES_PER_PORT = 9;

    public int getFluidId(int port) {
        validatePort(port);
        return data.get(port * VALUES_PER_PORT);
    }

    public Fluid getFluid(int port) {
        return BuiltInRegistries.FLUID.byId(getFluidId(port));
    }

    public long getFluidAmount(int port) {
        validatePort(port);
        return readLong(port * VALUES_PER_PORT + 1);
    }

    public long getCapacity(int port) {
        validatePort(port);
        return readLong(port * VALUES_PER_PORT + 5);
    }

    private long readLong(int offset) {
        return ((long) (data.get(offset) & 0xFFFF))
                | ((long) (data.get(offset + 1) & 0xFFFF) << 16)
                | ((long) (data.get(offset + 2) & 0xFFFF) << 32)
                | ((long) (data.get(offset + 3) & 0xFFFF) << 48);
    }

    private void validatePort(int port) {
        if (port < 0 || port > 1) {
            throw new IndexOutOfBoundsException("Invalid port: " + port);
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        if (player.distanceToSqr(
                tablePosition.getX() + 0.5,
                tablePosition.getY() + 0.5,
                tablePosition.getZ() + 0.5
        ) > 64.0) {
            return false;
        }

        BlockState state = player.level().getBlockState(tablePosition);

        return state.is(ModBlocks.ALCHEMY_TABLE.get())
                && state.getValue(ModBlockProperties.COMPLETED);
    }
}