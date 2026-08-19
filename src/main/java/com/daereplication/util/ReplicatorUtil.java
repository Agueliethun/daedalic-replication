package com.daereplication.util;

import com.daereplication.recipe.ReplicationLearnRecipe;
import com.daereplication.recipe.ReplicationReplicateRecipe;

public class ReplicatorUtil {

    public static final long MACHINE_MAX_ENERGY = 2048000L;

    public static double getEfficiency(ReplicationReplicateRecipe recipe, int numStored) {
        if (recipe == null) {
            return 0.0;
        }

        return numStored / (numStored + (double)recipe.getLearnFactor());
    }

    public static double getEfficiencyForEnchantLevel(int enchLevel) {
        return 1.0 - (enchLevel / (enchLevel + 3.0));
    }

    public static double getDoubleChanceForFortuneLevel(int fortLevel) {
        return (fortLevel / (fortLevel + 3.0));
    }
}
