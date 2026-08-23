package com.daereplication.blocks;

import com.daereplication.blockentities.DRBlockEntities;
import com.daereplication.blockentities.ReplicatorBlockEntity;
import com.daereplication.component.DRDataComponents;
import com.daereplication.component.GenericEnergyStorage;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ReplicatorBlock extends MachineBlock {

    public static final BooleanProperty HAS_MODEL = BooleanProperty.create("has_model");

    protected ReplicatorBlock(Properties properties) {
        super(properties);

        registerDefaultState(defaultBlockState().setValue(HAS_MODEL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_MODEL);
    }

    public static int getLuminance(BlockState blockState) {
        return blockState.getValue(ReplicatorBlock.HAS_MODEL) ? 12 : 0;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return simpleCodec(ReplicatorBlock::new);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ReplicatorBlockEntity(pos, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : createTickerHelper(type, DRBlockEntities.REPLICATOR_BLOCK_ENTITY, ReplicatorBlockEntity::serverTick);
    }
}
