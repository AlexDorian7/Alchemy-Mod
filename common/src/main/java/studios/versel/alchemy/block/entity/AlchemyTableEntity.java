package studios.versel.alchemy.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import studios.versel.alchemy.menu.AlchemyMenu;

public class AlchemyTableEntity extends BlockEntity {

    public AlchemyTableEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ALCHEMY_TABLE.get(), pos, state);
    }

    public ContainerData createMenuData() {
        Direction facing = getBlockState().getValue(HorizontalDirectionalBlock.FACING);

        BlockPos leftPortPos = AlchemyMenu.getLeftPortPos(worldPosition, facing);
        BlockPos rightPortPos = AlchemyMenu.getRightPortPos(worldPosition, facing);

        BlockPos[] portPositions = {
                leftPortPos,
                rightPortPos
        };

        return new ContainerData() {

            @Override
            public int get(int index) {
                int portIndex = index / 9;
                int property = index % 9;

                if (portIndex < 0 || portIndex >= 2) {
                    throw new IndexOutOfBoundsException("Invalid port index: " + portIndex);
                }

                if (level == null ||
                        !(level.getBlockEntity(portPositions[portIndex]) instanceof AlchemyPortEntity port)) {
                    return 0;
                }

                var tank = port.getTank();

                return switch (property) {
                    case 0 -> BuiltInRegistries.FLUID.getId(
                            tank.getFluid().getFluid()
                    );

                    case 1, 2, 3, 4 -> getLongPart(
                            tank.getAmount(),
                            property - 1
                    );

                    case 5, 6, 7, 8 -> getLongPart(
                            tank.getCapacity(),
                            property - 5
                    );

                    default -> 0;
                };
            }

            private int getLongPart(long value, int part) {
                return (int) ((value >>> (part * 16)) & 0xFFFFL);
            }

            @Override
            public void set(int index, int value) {
                // Server-authoritative data.
            }

            @Override
            public int getCount() {
                return 18;
            }
        };
    }
}