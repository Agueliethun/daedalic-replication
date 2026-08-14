package com.daereplication.items;

import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class ReplicationModel extends Item {

    public ReplicationModel(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        if (player.isCrouching()) {
            ItemStack stack = player.getItemInHand(hand);
            Holder<Item> itemHolder = level.registryAccess().lookupOrThrow(Registries.ITEM).wrapAsHolder(Items.DIRT);
            stack.set(DRDataComponents.REPLICATION_BLOCK_STORAGE, new ReplicationBlockStorage(0, 0.0, itemHolder));
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
