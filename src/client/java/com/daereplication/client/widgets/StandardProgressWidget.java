package com.daereplication.client.widgets;

import com.daereplication.DaedalicReplication;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.Identifier;

public class StandardProgressWidget extends CraftingProgressWidget {

    private static final int IMAGE_WIDTH = 22;
    private static final int IMAGE_HEIGHT = 16;

    private static final Identifier SPRITE = Identifier.fromNamespaceAndPath(DaedalicReplication.MOD_ID, "container/dr_progress");

    public StandardProgressWidget(int x, int y, IRecipeType<?> recipeType) {
        super(x, y, IMAGE_WIDTH, IMAGE_HEIGHT, SPRITE, recipeType);
    }
}
