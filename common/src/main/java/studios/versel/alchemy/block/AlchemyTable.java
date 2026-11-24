package studios.versel.alchemy.block;

import com.mojang.serialization.MapCodec;
import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studios.versel.alchemy.block.entity.AlchemyTableEntity;
import studios.versel.alchemy.block.states.ModBlockProperties;
import studios.versel.alchemy.menu.AlchemyMenu;
import studios.versel.alchemy.multiblock.AlchemyStructure;
import studios.versel.alchemy.multiblock.MultiblockManager;

public class AlchemyTable extends VisibleMultiblockEntityBlock {

    public static final MapCodec<AlchemyTable> CODEC = simpleCodec(AlchemyTable::new);

    public AlchemyTable(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState()
                .setValue(ModBlockProperties.COMPLETED, false)
                .setValue(HorizontalDirectionalBlock.FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState()
                .setValue(ModBlockProperties.COMPLETED, false)
                .setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void onMultiblockRemoved(
            BlockState state,
            Level level,
            BlockPos pos
    ) {
        MultiblockManager.updateAround(
                level,
                pos,
                state.getValue(HorizontalDirectionalBlock.FACING)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyTableEntity(pos, state);
    }

    @Override
    protected @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult
    ) {
        if (!state.getValue(ModBlockProperties.COMPLETED)) {
            return InteractionResult.PASS;
        }

        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.PASS;
        }

        if (!(level.getBlockEntity(pos) instanceof AlchemyTableEntity tableEntity)) {
            return InteractionResult.PASS;
        }

        MenuProvider provider = new SimpleMenuProvider(
                (id, inventory, p) -> new AlchemyMenu(
                        id,
                        inventory,
                        pos,
                        tableEntity.createMenuData()
                ),
                Component.literal("Alchemy")
        );

        MenuRegistry.openExtendedMenu(
                serverPlayer,
                provider,
                buffer -> buffer.writeBlockPos(pos)
        );

        return InteractionResult.CONSUME;
    }
}