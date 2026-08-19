package com.daereplication.client.jei;

import com.daereplication.component.DRDataComponents;
import com.daereplication.component.ReplicationBlockStorage;
import com.daereplication.items.ReplicationModel;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class LearnerItemInterpreter implements ISubtypeInterpreter<ItemStack> {
    @Override
    public @Nullable Object getSubtypeData(ItemStack ingredient, UidContext context) {
        if (context == UidContext.Ingredient && ingredient.getItem() instanceof ReplicationModel learner) {
            ReplicationBlockStorage storage = learner.components().get(DRDataComponents.REPLICATION_BLOCK_STORAGE);
            if (storage != null && storage.numberIngested() > 0) {
                return storage.output();
            }
        }

        return null;
    }
}
