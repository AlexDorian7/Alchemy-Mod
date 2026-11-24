package studios.versel.alchemy.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studios.versel.alchemy.block.entity.AlchemyPortEntity;
import studios.versel.alchemy.block.states.ModBlockProperties;
import studios.versel.alchemy.multiblock.AlchemyStructure;
import studios.versel.alchemy.multiblock.MultiblockManager;

public class AlchemyPort extends MultiblockEntityBlock {
    public AlchemyPort(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState()
                .setValue(ModBlockProperties.COMPLETED, false));
    }

    public static final MapCodec<AlchemyPort> CODEC =
            simpleCodec(AlchemyPort::new);

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemyPortEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return ((AlchemyPortEntity) level.getBlockEntity(pos)).interactWithBucket(player, hand);
    }
}
