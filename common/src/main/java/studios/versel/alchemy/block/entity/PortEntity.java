package studios.versel.alchemy.block.entity;

import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidBucketHooks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import studios.versel.alchemy.fluid.FluidTank;

import java.util.Optional;

public class PortEntity extends BlockEntity {

    private static final String TANK_TAG = "tank";

    private final FluidTank tank;

    public PortEntity(BlockEntityType<?> blockEntityType, long tankCapacity, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);

        tank = new FluidTank(tankCapacity, Optional.of(this::setChangedAndSync));
    }


    public FluidTank getTank() {
        return tank;
    }

    public ItemInteractionResult interactWithBucket(
            Player player,
            InteractionHand hand
    ) {
        ItemStack held = player.getItemInHand(hand);

        if (!(held.getItem() instanceof BucketItem bucketItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        Fluid bucketFluid = FluidBucketHooks.getFluid(bucketItem);

        // Empty bucket: extract fluid.
        if (bucketFluid == Fluids.EMPTY) {
            if (tank.isEmpty() || tank.getAmount() < FluidStack.bucketAmount()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            FluidStack drained = tank.drain(FluidStack.bucketAmount(), false);

            ItemStack result = drained.getFluid().getBucket().getDefaultInstance();

            if (!player.getAbilities().instabuild) {
                held.shrink(1);

                if (held.isEmpty()) {
                    player.setItemInHand(hand, result);
                } else if (!player.getInventory().add(result)) {
                    player.drop(result, false);
                }
            }

            setChanged();
            return ItemInteractionResult.SUCCESS;
        }

        // Filled bucket: insert fluid.
        FluidStack resource = FluidStack.create(
                bucketFluid,
                FluidStack.bucketAmount()
        );

        if (tank.fill(resource, true) < FluidStack.bucketAmount()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        tank.fill(resource, false);

        if (!player.getAbilities().instabuild) {
            held.shrink(1);

            ItemStack emptyBucket = Items.BUCKET.getDefaultInstance();

            if (held.isEmpty()) {
                player.setItemInHand(hand, emptyBucket);
            } else if (!player.getInventory().add(emptyBucket)) {
                player.drop(emptyBucket, false);
            }
        }

        setChanged();
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        CompoundTag tankTag = new CompoundTag();
        tank.writeNbt(registries, tankTag);
        tag.put(TANK_TAG, tankTag);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(TANK_TAG)) {
            tank.readNbt(registries, tag.getCompound(TANK_TAG));
        }
    }

    private void setChangedAndSync() {
        setChanged();

        if (level != null) {
            level.sendBlockUpdated(
                    worldPosition,
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_ALL
            );
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}