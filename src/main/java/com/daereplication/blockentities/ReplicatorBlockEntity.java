package com.daereplication.blockentities;

import com.daereplication.DaedalicReplication;
import com.daereplication.blocks.ReplicatorBlock;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.GenericEnergyStorage;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.menu.ReplicatorMenu;
import com.daereplication.recipe.*;
import com.daereplication.util.ReplicatorUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.Optional;

public class ReplicatorBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, MenuProvider {
    private static final float ENERGY_DISPLAY_FACTOR = 16384L / (float)ReplicatorUtil.MACHINE_MAX_ENERGY;

    private final static long CAPACITY = ReplicatorUtil.MACHINE_MAX_ENERGY;
    private final static long MAX_TRANSFER = 256000L;

    public final static int SLOT_INPUT = 0;
    public final static int SLOT_LEARNER = 1;
    public final static int SLOT_OUTPUT = 2;

    public final static int DATA_LEARN_PROGRESS = 0;
    public final static int DATA_LEARN_TOTAL_TIME = 1;
    public final static int DATA_REPLICATE_PROGRESS = 2;
    public final static int DATA_REPLICATE_TOTAL_TIME = 3;
    public final static int DATA_POWER_AMOUNT = 4;
    public final static int DATA_POWER_MAX = 5;

    protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

    private int learnProgress;
    private int learnMax;
    private int replicateProgress;
    private int replicateMax;

    private final RecipeManager.CachedCheck<ReplicationLearnRecipeInput, ReplicationLearnRecipe> learnQuickCheck;
    private final RecipeManager.CachedCheck<ReplicationReplicateRecipeInput, ReplicationReplicateRecipe> replicateQuickCheck;

    protected final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY, MAX_TRANSFER, MAX_TRANSFER) {
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    public ReplicatorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DRBlockEntities.REPLICATOR_BLOCK_ENTITY, worldPosition, blockState);
        this.learnQuickCheck = RecipeManager.createCheck(DRRecipeTypes.REPLICATION_LEARN_RECIPE_TYPE);
        this.replicateQuickCheck = RecipeManager.createCheck(DRRecipeTypes.REPLICATION_REPLICATE_RECIPE_TYPE);
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);

        this.energyStorage.amount = input.getLongOr("energyAmount", 0L);
        this.learnProgress = input.getIntOr("learnProgress", 0);
        this.replicateProgress = input.getIntOr("replicateProgress", 0);
        this.learnMax = input.getIntOr("learnMax", 0);
        this.replicateMax = input.getIntOr("replicateMax", 0);
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
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);

        GenericEnergyStorage storage = components.get(DRDataComponents.GENERIC_ENERGY_STORAGE);
        if (storage != null && storage.power() > 0) {
            this.energyStorage.amount = storage.power();
            setChanged();
        }
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
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
        // TODO: get rid of this when we add power generating blocks
        entity.insertEnergy(20);

        ItemStack learner = entity.getItem(SLOT_LEARNER);
        if (learner.isEmpty()) {
            entity.learnProgress = 0;
            entity.replicateProgress = 0;

            level.setBlockAndUpdate(pos, state.setValue(ReplicatorBlock.HAS_MODEL, false));

            return;
        }

        level.setBlockAndUpdate(pos, state.setValue(ReplicatorBlock.HAS_MODEL, true));

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
        Optional<RecipeHolder<ReplicationLearnRecipe>> holder = this.learnQuickCheck.getRecipeFor(recipeInput, (ServerLevel)level);

        // No recipe exists for that input
        if (holder.isEmpty()) {
            learnProgress = 0;
            return;
        }

        ReplicationLearnRecipe recipe = holder.get().value();

        // Model has existing data of another type
        if (storage.numberIngested() > 0 && !recipe.getOutput().is(storage.output())) {
            learnProgress = 0;
            return;
        }

        this.learnMax = recipe.getTime();

        if (!extractEnergy(recipe.getEnergy())) {
            return;
        }

        learnProgress++;
        if (learnProgress >= learnMax) {
            int newNumStored = numStored + recipe.getProgress();
            double efficiency = 0.0;

            ItemStack result = recipe.assemble(recipeInput);

            learner.set(DRDataComponents.REPLICATION_BLOCK_STORAGE, new ReplicationBlockStorage(newNumStored, 0.0, result.typeHolder()));
            input.setCount(input.getCount() - 1);

            ReplicationReplicateRecipeInput replicateRecipeInput = new ReplicationReplicateRecipeInput(learner);
            Optional<RecipeHolder<ReplicationReplicateRecipe>> replicateHolder = this.replicateQuickCheck.getRecipeFor(replicateRecipeInput, (ServerLevel)level);

            if (replicateHolder.isPresent()) {
                efficiency = ReplicatorUtil.getEfficiency(replicateHolder.get().value(), newNumStored);
                learner.set(DRDataComponents.REPLICATION_BLOCK_STORAGE, new ReplicationBlockStorage(newNumStored, efficiency, result.typeHolder()));
            }

            learnProgress = 0;
        }
    }

    private void doReplicate(ItemStack learner, ReplicationBlockStorage storage, int numStored) {
        ReplicationReplicateRecipeInput recipeInput = new ReplicationReplicateRecipeInput(learner);
        Optional<RecipeHolder<ReplicationReplicateRecipe>> holder = this.replicateQuickCheck.getRecipeFor(recipeInput, (ServerLevel)level);

        // No recipe exists for that input
        if (holder.isEmpty()) {
            replicateProgress = 0;
            return;
        }

        ReplicationReplicateRecipe recipe = holder.get().value();

        double efficiency = ReplicatorUtil.getEfficiency(recipe, numStored);
        if (efficiency <= 0) {
            replicateProgress = 0;
            return;
        }

        ItemStack output = getItem(SLOT_OUTPUT);
        if (output.count() >= getMaxStackSize()) {
            replicateProgress = 0;
            return;
        }

        int energyRequired = (int)Math.ceil(recipe.getEnergy() / efficiency);
        int ticksRequired = (int)Math.ceil(recipe.getTime() / efficiency);

        this.replicateMax = ticksRequired;

        if (!extractEnergy(energyRequired)) {
            return;
        }

        replicateProgress++;
        if (replicateProgress >= replicateMax) {
            if (output.isEmpty()) {
                setItem(SLOT_OUTPUT, recipe.assemble(recipeInput));
            } else {
                output.setCount(output.getCount() + 1);
            }

            replicateProgress = 0;
        }
    }

    public boolean insertEnergy(long amount) {
        try (Transaction transaction = Transaction.openOuter()) {
            long amountInserted = this.energyStorage.insert(amount, transaction);
            if (amountInserted == amount) {
                transaction.commit();
                setChanged();
                return true;
            }
        }

        return false;
    }

    public boolean extractEnergy(long amount) {
        try (Transaction transaction = Transaction.openOuter()) {
            long amountExtracted = this.energyStorage.extract(amount, transaction);
            if (amountExtracted == amount) {
                transaction.commit();
                setChanged();
                return true;
            }
        }

        return false;
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
        Optional<RecipeHolder<ReplicationLearnRecipe>> holder = this.learnQuickCheck.getRecipeFor(recipeInput, (ServerLevel)level);

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
        return new ReplicatorMenu(containerId, inventory, this, dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int containerId, Inventory inventory) {
        return new ReplicatorMenu(containerId, inventory);
    }

    protected final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(final int dataId) {
            try {
                switch (dataId) {
                    case DATA_LEARN_PROGRESS:
                        return ReplicatorBlockEntity.this.learnProgress;
                    case DATA_LEARN_TOTAL_TIME:
                        return ReplicatorBlockEntity.this.learnMax;
                    case DATA_REPLICATE_PROGRESS:
                        return ReplicatorBlockEntity.this.replicateProgress;
                    case DATA_REPLICATE_TOTAL_TIME:
                        return ReplicatorBlockEntity.this.replicateMax;
                    case DATA_POWER_AMOUNT:
                        long amount = ReplicatorBlockEntity.this.energyStorage.getAmount();
                        return (int)(amount * ENERGY_DISPLAY_FACTOR);
                    case DATA_POWER_MAX:
                        long max = ReplicatorBlockEntity.this.energyStorage.getCapacity();
                        return (int)(max * ENERGY_DISPLAY_FACTOR);
                }
            } catch (ArithmeticException ae) {
                return 0;
            }

            return 0;
        }

        @Override
        public void set(final int dataId, final int value) {
            DaedalicReplication.LOGGER.warn("Tried to modify ReplicatorBlockEntity data.");
        }

        @Override
        public int getCount() {
            return 6;
        }
    };
}
