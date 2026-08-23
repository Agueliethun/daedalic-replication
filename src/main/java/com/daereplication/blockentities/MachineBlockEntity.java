package com.daereplication.blockentities;

import com.daereplication.blocks.MachineBlock;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.GenericEnergyStorage;
import com.daereplication.component.UpgradeStorage;
import com.daereplication.items.UpgradeItem;
import com.daereplication.recipe.DRRecipeTypes;
import com.daereplication.util.ReplicatorUtil;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.*;
import java.util.function.Function;

public abstract class MachineBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, MenuProvider {

    // Of the form (ax + c) / (x + c)
    // We like these constants because f(0) = 1.0 and f(32) = 2.0
    private static final float UPGRADE_CONSTANT_C = 16F;    // Half Saturation Constant
    private static final float UPGRADE_CONSTANT_A = 4F;   // Asymptote

    public static final float ENERGY_DISPLAY_FACTOR = 16384L / (float) ReplicatorUtil.MACHINE_MAX_ENERGY;

    protected static final long CAPACITY = ReplicatorUtil.MACHINE_MAX_ENERGY;
    protected static final long MAX_TRANSFER = 256000L;

    protected NonNullList<ItemStack> items;

    protected final SimpleEnergyStorage energyStorage = new SimpleEnergyStorage(CAPACITY, MAX_TRANSFER, MAX_TRANSFER) {
        @Override
        protected void onFinalCommit() {
            setChanged();
        }
    };

    public MachineBlockEntity(
            BlockEntityType<? extends MachineBlockEntity> blockEntityType,
            BlockPos worldPosition,
            BlockState blockState,
            int slotCount
    ) {
        super(blockEntityType, worldPosition, blockState);
        items = NonNullList.withSize(slotCount, ItemStack.EMPTY);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.items);

        this.energyStorage.amount = input.getLongOr("energyAmount", 0L);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);

        output.putLong("energyAmount", this.energyStorage.amount);
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
    public boolean canPlaceItemThroughFace(int slot, ItemStack itemStack, @Nullable Direction direction) {
        return canPlaceItem(slot, itemStack);
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.items = items;
    }

    public abstract Map<Integer, UpgradeItem.SlotType> getUpgradeSlots();

    protected Map<UpgradeStorage, Integer> getUpgradeStorages() {
        Map<UpgradeStorage, Integer> storages = new HashMap<>();

        for (int slot : getUpgradeSlots().keySet()) {
            ItemStack stack = getItem(slot);
            if (!stack.isEmpty() && stack.has(DRDataComponents.UPGRADE_STORAGE)) {
                storages.put(stack.get(DRDataComponents.UPGRADE_STORAGE), stack.getCount());
            }
        }

        return storages;
    }

    public int getLearnBonus() {
        return getLearnBonus(getUpgradeStorages());
    }

    public int getLearnBonus(Map<UpgradeStorage, Integer> storages) {
        return getBonusValue(storages, UpgradeStorage::learnBonus);
    }

    public float getEfficiencyBonus() {
        return getEfficiencyBonus(getUpgradeStorages());
    }

    public float getEfficiencyBonus(Map<UpgradeStorage, Integer> storages) {
        return getAppliedBonus(getBonusValue(storages, UpgradeStorage::efficiency));
    }

    public float getSpeedBonus() {
        return getSpeedBonus(getUpgradeStorages());
    }

    public float getSpeedBonus(Map<UpgradeStorage, Integer> storages) {
        return getAppliedBonus(getBonusValue(storages, UpgradeStorage::speed));
    }

    public int getBonusValue(Map<UpgradeStorage, Integer> storages, Function<UpgradeStorage, Integer> mapper) {
        int total = 0;

        for (Map.Entry<UpgradeStorage, Integer> entry : storages.entrySet()) {
            total += entry.getValue() * mapper.apply(entry.getKey());
        }

        return total;
    }

    public static float getAppliedBonus(int value) {
        return (UPGRADE_CONSTANT_C + UPGRADE_CONSTANT_A * value) / (UPGRADE_CONSTANT_C + value);
    }
}
