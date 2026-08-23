package com.daereplication.blocks;

import com.daereplication.blockentities.MachineBlockEntity;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.GenericEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public abstract class MachineBlock extends BaseEntityBlock {

    protected MachineBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        BlockEntity be = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof MachineBlockEntity mbe) {
            ItemStack drop = new ItemStack(this.asItem());
            GenericEnergyStorage component = new GenericEnergyStorage(mbe.getEnergyStorage().getAmount());
            drop.set(DRDataComponents.GENERIC_ENERGY_STORAGE, component);
            return List.of(drop);
        }

        return List.of(new ItemStack(this.asItem()));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof MachineBlockEntity machine) {
            player.openMenu(machine);
        }

        return InteractionResult.SUCCESS;
    }
}
