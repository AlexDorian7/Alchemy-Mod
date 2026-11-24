package studios.versel.alchemy.block.entity;

import dev.architectury.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studios.versel.alchemy.fluid.FluidTank;
import studios.versel.alchemy.recipe.foundry.FoundryRecipe;

import java.util.Optional;

public class FoundryBlockEntity extends BlockEntity implements WorldlyContainer {

    private static final String TANK_TAG = "tank";
    private static final String ACTIVE_STACK_TAG = "active_stack";
    private static final String PROCESS_START_TICK_TAG = "process_start_tick";

    private long processStartTick = -1L;

    private FoundryRecipe processingRecipe;

    private ItemStack activeStack = ItemStack.EMPTY;

    private final FluidTank tank = new FluidTank(FluidStack.bucketAmount() * 8, Optional.of(this::setChangedAndSync));


    public FoundryBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FOUNDRY.get(), pos, state);
    }

    public static void serverTick(
            Level level,
            BlockPos pos,
            BlockState state,
            FoundryBlockEntity blockEntity
    ) {
        if (level.isClientSide) {
            return;
        }

        // There must be an input item.
        if (blockEntity.activeStack.isEmpty()) {
            blockEntity.resetProcessing();
            return;
        }

        // Find a recipe matching the current input.
        FoundryRecipe recipe = FoundryRecipe.getRecipes()
                .stream()
                .filter(candidate -> candidate.match(blockEntity.activeStack))
                .findFirst()
                .orElse(null);

        if (recipe == null) {
            blockEntity.resetProcessing();
            return;
        }

        // Find the fuel tank in the Foundry Port.
        Direction back = state.getValue(HorizontalDirectionalBlock.FACING)
                .getOpposite();

        BlockPos fuelPos = pos.relative(back, 2).below();

        if (!(level.getBlockEntity(fuelPos) instanceof FoundryPortEntity portEntity)) {
            blockEntity.resetProcessing();
            return;
        }

        FluidTank fuelTank = portEntity.getTank();

        long currentTick = level.getGameTime();

        // If the recipe changed, restart processing.
        if (blockEntity.processingRecipe != null
                && blockEntity.processingRecipe != recipe) {
            blockEntity.resetProcessing();
        }

        // Start processing if necessary.
        if (blockEntity.processStartTick < 0L) {
            blockEntity.processStartTick = currentTick;
            blockEntity.setChangedAndSync();
        }

        blockEntity.processingRecipe = recipe;

        // Process one tick of the recipe.
        FoundryRecipe.ProcessResult result = recipe.process(
                fuelTank,
                blockEntity.activeStack,
                blockEntity.tank,
                blockEntity.processStartTick,
                currentTick
        );

        switch (result) {
            case FAIL -> blockEntity.resetProcessing();

            case CONTINUE -> {
                // Continue processing on the next tick.
            }

            case SUCCESS -> {
                // The recipe has consumed its input and produced its output.
                blockEntity.resetProcessing();
                blockEntity.setChangedAndSync();
            }
        }
    }

    private void resetProcessing() {
        boolean changed = processStartTick != -1L || processingRecipe != null;

        processStartTick = -1L;
        processingRecipe = null;

        if (changed) {
            setChangedAndSync();
        }
    }

    public ItemInteractionResult useItem(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (!activeStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (itemStack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        int accepted = Math.min(itemStack.getCount(), getMaxStackSize());

        ItemStack inserted = itemStack.copy();
        inserted.setCount(accepted);

        setItem(INPUT_SLOT, inserted);
        itemStack.consume(accepted, player);

        return ItemInteractionResult.CONSUME;
    }

    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult) {
        if (activeStack.isEmpty()) {
            return InteractionResult.CONSUME;
        }

        ItemStack removed = removeItemNoUpdate(INPUT_SLOT);

        if (!player.getInventory().add(removed)) {
            player.drop(removed, false);
        }

        return InteractionResult.CONSUME;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);

        CompoundTag tankTag = new CompoundTag();
        tank.writeNbt(registries, tankTag);
        tag.put(TANK_TAG, tankTag);
        tag.putLong(PROCESS_START_TICK_TAG, processStartTick);

        if (!activeStack.isEmpty()) {
            tag.put(ACTIVE_STACK_TAG, activeStack.save(registries));
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains(TANK_TAG)) {
            tank.readNbt(registries, tag.getCompound(TANK_TAG));
        }
        processStartTick = tag.contains(PROCESS_START_TICK_TAG)
                ? tag.getLong(PROCESS_START_TICK_TAG)
                : -1L;

        processingRecipe = null;

        if (tag.contains(ACTIVE_STACK_TAG)) {
            activeStack = ItemStack.parse(registries, tag.getCompound(ACTIVE_STACK_TAG))
                    .orElse(ItemStack.EMPTY);
        } else {
            activeStack = ItemStack.EMPTY;
        }
    }

    public FluidTank getTank() {
        return tank;
    }

    public ItemStack getActiveStack() {
        return activeStack;
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

    private static final int INPUT_SLOT = 0;
    private static final int[] SLOTS = {INPUT_SLOT};

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return activeStack.isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        if (slot != INPUT_SLOT) {
            return ItemStack.EMPTY;
        }

        return activeStack;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if (slot != INPUT_SLOT || activeStack.isEmpty() || amount <= 0) {
            return ItemStack.EMPTY;
        }

        ItemStack removed = activeStack.split(amount);

        if (activeStack.isEmpty()) {
            activeStack = ItemStack.EMPTY;
        }

        resetProcessing();
        setChangedAndSync();

        return removed;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != INPUT_SLOT) {
            return ItemStack.EMPTY;
        }

        ItemStack removed = activeStack;
        activeStack = ItemStack.EMPTY;

        if (!removed.isEmpty()) {
            resetProcessing();
            setChangedAndSync();
        }

        return removed;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot != INPUT_SLOT) {
            return;
        }

        if (!stack.isEmpty() && !canPlaceItem(slot, stack)) {
            return;
        }

        activeStack = stack.copy();

        if (activeStack.getCount() > getMaxStackSize()) {
            activeStack.setCount(getMaxStackSize());
        }

        if (activeStack.isEmpty()) {
            activeStack = ItemStack.EMPTY;
        }

        resetProcessing();
        setChangedAndSync();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (slot != INPUT_SLOT || stack.isEmpty()) {
            return false;
        }

        return activeStack.isEmpty()
                || ItemStack.isSameItemSameComponents(activeStack, stack);
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return SLOTS;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == INPUT_SLOT && !activeStack.isEmpty();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        if (!activeStack.isEmpty()) {
            activeStack = ItemStack.EMPTY;
            resetProcessing();
            setChangedAndSync();
        }
    }
}