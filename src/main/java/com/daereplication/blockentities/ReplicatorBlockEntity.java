package com.daereplication.blockentities;

import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.menu.ReplicatorMenu;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationLearnRecipeInput;
import com.daereplication.util.ReplicatorUtil;
import net.fabricmc.fabric.mixin.recipe.sync.RecipeManagerAccessor;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.List;
import java.util.Optional;

public class ReplicatorBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, MenuProvider {
    private final static long CAPACITY = 2048000L;
    private final static long MAX_TRANSFER = 256000L;

    private final static int REPLICATE_TIME = 40;
    private final static double MIN_EFFICIENCY = 0.01;

    public final static int SLOT_INPUT = 0;
    public final static int SLOT_LEARNER = 1;
    public final static int SLOT_OUTPUT = 2;

    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    private int learnProgress;
    private int replicateProgress;

    private final RecipeManager.CachedCheck<ReplicationLearnRecipeInput, ReplicationLearnRecipe> quickCheck;

    protected final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY, MAX_TRANSFER, MAX_TRANSFER) {
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    public ReplicatorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DRBlockEntities.REPLICATOR_BLOCK_ENTITY, worldPosition, blockState);
        this.learnProgress = 0;
        this.replicateProgress = 0;
        this.quickCheck = RecipeManager.createCheck(DRRecipeTypes.REPLICATION_LEARN_RECIPE_TYPE);
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);

        this.energyStorage.amount = input.getLongOr("energyAmount", 0L);
        this.learnProgress = input.getIntOr("learnProgress", 0);
        this.replicateProgress = input.getIntOr("replicateProgress", 0);
    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);

        output.putLong("energyAmount", this.energyStorage.amount);
        output.putInt("learnProgress", this.learnProgress);
        output.putInt("replicateProgress", this.replicateProgress);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.daedalic-replication.replicator");
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registryLookup) {
        return saveWithoutMetadata(registryLookup);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void setChanged() {
        super.setChanged();

        if (level == null) return;

        BlockState state = getBlockState();
        level.sendBlockUpdated(worldPosition, state, state, Block.UPDATE_ALL);
    }

    public static void serverTick(final Level level, final BlockPos pos, BlockState state, final ReplicatorBlockEntity entity) {
        ItemStack learner = entity.getItem(SLOT_LEARNER);
        if (learner.isEmpty()) {
            entity.learnProgress = 0;
            entity.replicateProgress = 0;
            return;
        }

        Holder<Item> itemHolder = level.registryAccess().lookupOrThrow(Registries.ITEM).wrapAsHolder(Items.DIRT);
        ReplicationBlockStorage storage = learner.getOrDefault(
                DRDataComponents.REPLICATION_BLOCK_STORAGE,
                new ReplicationBlockStorage(0, 0.0, itemHolder)
        );
        int numStored = Math.max(0, storage.numberIngested());

        entity.doLearn(learner, storage, numStored);
        entity.doReplicate(learner, storage, numStored);
    }

    private void doLearn(ItemStack learner, ReplicationBlockStorage storage, int numStored) {
        ItemStack input = getItem(SLOT_INPUT);
        if (input.isEmpty() || input.getCount() <= 0) {
            learnProgress = 0;
            return;
        }

        ReplicationLearnRecipeInput recipeInput = new ReplicationLearnRecipeInput(learner, input, this.energyStorage.getAmount());
        Optional<RecipeHolder<ReplicationLearnRecipe>> holder = this.quickCheck.getRecipeFor(recipeInput, (ServerLevel)level);

        // No recipe exists for that input
        if (holder.isEmpty()) {
            learnProgress = 0;
            return;
        }

        ReplicationLearnRecipe recipe = holder.get().value();

        // Model has existing data of another type
        if (!recipe.getOutput().is(storage.output())) {
            learnProgress = 0;
            return;
        }

        learnProgress++;
        if (learnProgress >= 20) {
            int newNumStored = numStored + 1;
            double efficiency = ReplicatorUtil.getEfficiency(recipe, newNumStored);

            learner.set(DRDataComponents.REPLICATION_BLOCK_STORAGE, new ReplicationBlockStorage(numStored + 1, efficiency, input.typeHolder()));
            input.setCount(input.getCount() - 1);

            learnProgress = 0;
        }
    }

    private void doReplicate(ItemStack learner, ReplicationBlockStorage storage, int numStored) {
        double efficiency = numStored / (numStored + 9.0);
        if (efficiency <= 0) {
            replicateProgress = 0;
            return;
        }

        ItemStack output = getItem(SLOT_OUTPUT);
        if (numStored > 0 && storage.output().kind().compareTo(output.typeHolder().kind()) != 0) {
            replicateProgress = 0;
            return;
        }

        if (output.count() >= getMaxStackSize()) {
            replicateProgress = 0;
            return;
        }

        int ticksRequired = (int)Math.ceil(REPLICATE_TIME / efficiency);
        replicateProgress++;

        if (replicateProgress >= ticksRequired) {
            if (output.isEmpty()) {
                setItem(SLOT_OUTPUT, new ItemStack(storage.output()));
            } else {
                output.setCount(output.getCount() + 1);
            }

            replicateProgress = 0;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {SLOT_INPUT, SLOT_LEARNER, SLOT_OUTPUT};
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return canPlaceItem(slot, itemStack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot == SLOT_OUTPUT;
    }

    public boolean isInput(ItemStack itemStack) {
        ItemStack learner = this.getItem(SLOT_LEARNER);
        ItemStack input = getItem(SLOT_INPUT);

        ReplicationLearnRecipeInput recipeInput = new ReplicationLearnRecipeInput(learner, input, this.energyStorage.getAmount());
        Optional<RecipeHolder<ReplicationLearnRecipe>> holder = this.quickCheck.getRecipeFor(recipeInput, (ServerLevel)level);

        if (holder.isPresent()) {
            ReplicationLearnRecipe recipe = holder.get().value();
            return true;
        }

        return false;
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public int getMaxStackSize(ItemStack itemStack) {
        return itemStack.is(DRItemIds.REPLICATION_MODEL) ? 1 : getMaxStackSize();
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return switch (slot) {
            case SLOT_INPUT -> !itemStack.is(DRItemIds.REPLICATION_MODEL) && isInput(itemStack);
            case SLOT_LEARNER -> itemStack.is(DRItemIds.REPLICATION_MODEL);
            default -> false;
        };
    }

    @Override
    public boolean canTakeItem(Container into, int slot, ItemStack itemStack) {
        return WorldlyContainer.super.canTakeItem(into, slot, itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ReplicatorMenu(containerId, inventory, this);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ReplicatorMenu(containerId, inventory);
    }
}
