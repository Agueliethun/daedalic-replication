package com.daereplication.blockentities;

import com.daereplication.DaedalicReplication;
import com.daereplication.blocks.ReplicatorBlock;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.component.UpgradeStorage;
import com.daereplication.items.DRItemIds;
import com.daereplication.items.UpgradeItem;
import com.daereplication.menu.MachineMenu;
import com.daereplication.menu.ReplicatorMenu;
import com.daereplication.recipe.*;
import com.daereplication.util.ReplicatorUtil;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReplicatorBlockEntity extends MachineBlockEntity {

    public static final int SLOT_INPUT = 0;
    public static final int SLOT_LEARNER = 1;
    public static final int SLOT_OUTPUT = 2;

    public static final int SLOT_UPGRADE_MAIN_1 = 3;
    public static final int SLOT_UPGRADE_SECONDARY_1 = 4;
    public static final int SLOT_UPGRADE_SECONDARY_2 = 5;

    public static final int DATA_LEARN_PROGRESS = 0;
    public static final int DATA_LEARN_TOTAL_TIME = 1;
    public static final int DATA_REPLICATE_PROGRESS = 2;
    public static final int DATA_REPLICATE_TOTAL_TIME = 3;
    public static final int DATA_POWER_AMOUNT = 4;
    public static final int DATA_POWER_MAX = 5;

    private int learnProgress;
    private int learnMax;
    private int replicateProgress;
    private int replicateMax;

    private final RecipeManager.CachedCheck<ReplicationLearnRecipeInput, ReplicationLearnRecipe> learnQuickCheck;
    private final RecipeManager.CachedCheck<ReplicationReplicateRecipeInput, ReplicationReplicateRecipe> replicateQuickCheck;

    public ReplicatorBlockEntity(BlockPos worldPosition, BlockState blockState) {
        super(DRBlockEntities.REPLICATOR_BLOCK_ENTITY, worldPosition, blockState, 6);
        this.learnQuickCheck = RecipeManager.createCheck(DRRecipeTypes.REPLICATION_LEARN_RECIPE_TYPE);
        this.replicateQuickCheck = RecipeManager.createCheck(DRRecipeTypes.REPLICATION_REPLICATE_RECIPE_TYPE);
    }

    @Override
    public Map<Integer, UpgradeItem.SlotType> getUpgradeSlots() {
        Map<Integer, UpgradeItem.SlotType> upgradeSlots = new HashMap<>();

        upgradeSlots.put(SLOT_UPGRADE_MAIN_1, UpgradeItem.SlotType.SLOT_MAIN);
        upgradeSlots.put(SLOT_UPGRADE_SECONDARY_1, UpgradeItem.SlotType.SLOT_SECONDARY);
        upgradeSlots.put(SLOT_UPGRADE_SECONDARY_2, UpgradeItem.SlotType.SLOT_SECONDARY);

        return upgradeSlots;
    }

    @Override
    protected void loadAdditional(final ValueInput input) {
        super.loadAdditional(input);

        this.learnProgress = input.getIntOr("learnProgress", 0);
        this.replicateProgress = input.getIntOr("replicateProgress", 0);
        this.learnMax = input.getIntOr("learnMax", 0);
        this.replicateMax = input.getIntOr("replicateMax", 0);
    }

    @Override
    protected void saveAdditional(final ValueOutput output) {
        super.saveAdditional(output);

        output.putInt("learnProgress", this.learnProgress);
        output.putInt("replicateProgress", this.replicateProgress);
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.daedalic-replication.replicator");
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

        Holder<Enchantment> fortune = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
        Holder<Enchantment> efficiency = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.EFFICIENCY);

        int fortuneLevel = 0;
        int enchEfficiencyLvl = 0;
        ItemEnchantments enchantments = learner.get(DataComponents.ENCHANTMENTS);
        if (enchantments != null) {
            fortuneLevel = enchantments.getLevel(fortune);
            enchEfficiencyLvl = enchantments.getLevel(efficiency);
        }

        Map<UpgradeStorage, Integer> storages = entity.getUpgradeStorages();
        int upgradeLearn = entity.getLearnBonus(storages);
        float upgradeEfficiency = entity.getEfficiencyBonus(storages);
        float upgradeSpeed = entity.getSpeedBonus(storages);

        entity.doLearn(learner, storage, upgradeLearn, upgradeEfficiency, upgradeSpeed, enchEfficiencyLvl);
        entity.doReplicate(learner, storage, upgradeEfficiency, upgradeSpeed, enchEfficiencyLvl, fortuneLevel);
    }

    private void doLearn(ItemStack learner, ReplicationBlockStorage storage, int upgradeLearn, float upgradeEfficiency, float upgradeSpeed, int enchEfficiencyLvl) {
        ItemStack input = getItem(SLOT_INPUT);
        if (input.isEmpty() || input.getCount() <= 0) {
            learnProgress = 0;
            return;
        }

        ReplicationLearnRecipeInput recipeInput = new ReplicationLearnRecipeInput(learner, input);
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

        double efficiencyValue = ReplicatorUtil.getEfficiencyForEnchantLevel(enchEfficiencyLvl);
        this.learnMax = (int)Math.ceil(recipe.getTime() * efficiencyValue / upgradeSpeed);

        int energyRequired = (int)Math.ceil(recipe.getEnergy() * efficiencyValue / upgradeEfficiency);
        if (!extractEnergy(energyRequired)) {
            return;
        }

        int numStored = Math.max(0, storage.numberIngested());

        learnProgress++;
        if (learnProgress >= learnMax) {
            int newNumStored = numStored + recipe.getProgress() + upgradeLearn;
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

    private void doReplicate(ItemStack learner, ReplicationBlockStorage storage, float upgradeEfficiency, float upgradeSpeed, int efficiencyLevel, int fortuneLevel) {
        ReplicationReplicateRecipeInput recipeInput = new ReplicationReplicateRecipeInput(learner);
        Optional<RecipeHolder<ReplicationReplicateRecipe>> holder = this.replicateQuickCheck.getRecipeFor(recipeInput, (ServerLevel) level);

        double efficiencyValue = ReplicatorUtil.getEfficiencyForEnchantLevel(efficiencyLevel);
        double fortuneChance = ReplicatorUtil.getDoubleChanceForFortuneLevel(fortuneLevel);

        // No recipe exists for that input
        if (holder.isEmpty()) {
            replicateProgress = 0;
            return;
        }

        ReplicationReplicateRecipe recipe = holder.get().value();

        int numStored = Math.max(0, storage.numberIngested());
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

        int energyRequired = (int) Math.ceil(recipe.getEnergy() * efficiencyValue / (efficiency * upgradeEfficiency));
        this.replicateMax = (int) Math.ceil(recipe.getTime() * efficiencyValue / (efficiency * upgradeSpeed));

        if (!extractEnergy(energyRequired)) {
            return;
        }

        replicateProgress++;
        if (replicateProgress >= replicateMax) {
            int made = 1;

            if (level.getRandom().nextDouble() >= fortuneChance) {
                made = 2;
            }

            if (output.isEmpty()) {
                setItem(SLOT_OUTPUT, recipe.assemble(recipeInput));
            } else {
                output.setCount(Math.min(output.getCount() + made, getMaxStackSize()));
            }

            replicateProgress = 0;
        }
    }

    @Override
    public int[] getSlotsForFace(Direction direction) {
        return new int[] {SLOT_INPUT, SLOT_LEARNER, SLOT_OUTPUT, SLOT_UPGRADE_MAIN_1, SLOT_UPGRADE_SECONDARY_1, SLOT_UPGRADE_SECONDARY_2};
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
        return slot == SLOT_OUTPUT;
    }

    public boolean isInput(ItemStack itemStack) {
        ItemStack learner = this.getItem(SLOT_LEARNER);

        ReplicationLearnRecipeInput recipeInput = new ReplicationLearnRecipeInput(learner, itemStack);
        Optional<RecipeHolder<ReplicationLearnRecipe>> holder = this.learnQuickCheck.getRecipeFor(recipeInput, (ServerLevel)level);

        if (holder.isPresent()) {
            ReplicationLearnRecipe recipe = holder.get().value();
            return true;
        }

        return false;
    }

    @Override
    public int getContainerSize() {
        return 6;
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
        return slot == SLOT_OUTPUT;
    }

    @Override
    public @Nullable MachineMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new ReplicatorMenu(containerId, inventory, this, dataAccess);
    }

    @Override
    protected MachineMenu createMenu(int containerId, Inventory inventory) {
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
