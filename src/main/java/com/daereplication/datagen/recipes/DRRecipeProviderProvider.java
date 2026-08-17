package com.daereplication.datagen.recipes;

import com.daereplication.DaedalicReplication;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v2.TagUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.references.BlockItemIds;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class DRRecipeProviderProvider extends FabricRecipeProvider {

    protected final Map<Identifier, DRRecipeBuilder<?>> recipeBuilders;

    public DRRecipeProviderProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
        this.recipeBuilders = new HashMap<>();
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        addRecipes(registries);

        return new DRRecipeProvider(registries, output, recipeBuilders);
    }

    protected void addRecipes(HolderLookup.Provider registries) {
        // Amethyst
        int amethystEnergy = 100;
        int amethystTime = DRLearnRecipeBuilder.TIME_LOW;
        createLearnRecipe("amethyst_shard", Ingredient.of(Items.AMETHYST_SHARD), Items.BUDDING_AMETHYST)
                .withTime(amethystTime)
                .withEnergy(amethystEnergy);
        createLearnRecipe("amethyst_block", Ingredient.of(Items.AMETHYST_BLOCK), Items.BUDDING_AMETHYST)
                .withTime(4 * amethystTime)
                .withEnergy(amethystEnergy)
                .withProgress(4);
        createLearnRecipe("budding_amethyst", Ingredient.of(Items.BUDDING_AMETHYST), Items.BUDDING_AMETHYST)
                .withTime(36 * amethystTime)
                .withEnergy(amethystEnergy)
                .withProgress(36);
        createReplicateRecipe("budding_amethyst", getItemHolder(registries, BlockItemIds.BUDDING_AMETHYST.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH)
                .withEnergy(400);

        // Calcite
        createLearnRecipe("calcite", Ingredient.of(Items.CALCITE), Items.CALCITE)
                .withEnergy(5);
        createReplicateRecipe("calcite", getItemHolder(registries, BlockItemIds.CALCITE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Cinnabar
        createLearnRecipe("cinnabar", Ingredient.of(Items.CINNABAR), Items.CINNABAR)
                .withEnergy(5);
        createReplicateRecipe("cinnabar", getItemHolder(registries, BlockItemIds.CINNABAR.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Deepslate
        createLearnRecipe("deepslate", Ingredient.of(Items.DEEPSLATE), Items.DEEPSLATE)
                .withEnergy(5);
        createLearnRecipe("cobbled_deepslate", Ingredient.of(Items.COBBLED_DEEPSLATE), Items.DEEPSLATE)
                .withEnergy(5);
        createReplicateRecipe("deepslate", getItemHolder(registries, BlockItemIds.DEEPSLATE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Tuff
        createLearnRecipe("tuff", Ingredient.of(Items.TUFF), Items.TUFF)
                .withEnergy(5);
        createReplicateRecipe("tuff", getItemHolder(registries, BlockItemIds.TUFF.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Sulfur
        createLearnRecipe("sulfur", Ingredient.of(Items.SULFUR), Items.SULFUR)
                .withEnergy(5);
        createReplicateRecipe("sulfur", getItemHolder(registries, BlockItemIds.SULFUR.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Dripstone Block
        createLearnRecipe("dripstone_block", Ingredient.of(Items.DRIPSTONE_BLOCK), Items.DRIPSTONE_BLOCK)
                .withEnergy(5);
        createReplicateRecipe("dripstone_block", getItemHolder(registries, BlockItemIds.DRIPSTONE_BLOCK.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Cobblestone
        createLearnRecipe("cobblestone", Ingredient.of(Items.COBBLESTONE), Items.COBBLESTONE)
                .withEnergy(5);
        createReplicateRecipe("cobblestone", getItemHolder(registries, BlockItemIds.COBBLESTONE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Gravel
        createLearnRecipe("gravel", Ingredient.of(Items.GRAVEL), Items.GRAVEL)
                .withEnergy(5);
        createReplicateRecipe("gravel", getItemHolder(registries, BlockItemIds.GRAVEL.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Andesite
        createLearnRecipe("andesite", Ingredient.of(Items.ANDESITE), Items.ANDESITE)
                .withEnergy(5);
        createReplicateRecipe("andesite", getItemHolder(registries, BlockItemIds.ANDESITE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Diorite
        createLearnRecipe("diorite", Ingredient.of(Items.DIORITE), Items.DIORITE)
                .withEnergy(5);
        createReplicateRecipe("diorite", getItemHolder(registries, BlockItemIds.DIORITE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Granite
        createLearnRecipe("granite", Ingredient.of(Items.GRANITE), Items.GRANITE)
                .withEnergy(5);
        createReplicateRecipe("granite", getItemHolder(registries, BlockItemIds.GRANITE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Sand
        createLearnRecipe("sand", Ingredient.of(Items.SAND), Items.SAND)
                .withEnergy(5);
        createReplicateRecipe("sand", getItemHolder(registries, BlockItemIds.SAND.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Clay
        createLearnRecipe("clay", Ingredient.of(Items.CLAY), Items.CLAY)
                .withEnergy(5);
        createReplicateRecipe("clay", getItemHolder(registries, BlockItemIds.CLAY.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Mud
        createLearnRecipe("mud", Ingredient.of(Items.MUD), Items.MUD)
                .withEnergy(5);
        createReplicateRecipe("mud", getItemHolder(registries, BlockItemIds.MUD.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Netherrack
        createLearnRecipe("netherrack", Ingredient.of(Items.NETHERRACK), Items.NETHERRACK)
                .withEnergy(5);
        createReplicateRecipe("netherrack", getItemHolder(registries, BlockItemIds.NETHERRACK.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Endstone
        createLearnRecipe("endstone", Ingredient.of(Items.END_STONE), Items.END_STONE)
                .withEnergy(5);
        createReplicateRecipe("endstone", getItemHolder(registries, BlockItemIds.END_STONE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Purpur
        createLearnRecipe("purpur", Ingredient.of(Items.PURPUR_BLOCK), Items.PURPUR_BLOCK)
                .withEnergy(5);
        createReplicateRecipe("purpur", getItemHolder(registries, BlockItemIds.PURPUR_BLOCK.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Basalt
        createLearnRecipe("basalt", Ingredient.of(Items.BASALT), Items.BASALT)
                .withEnergy(5);
        createReplicateRecipe("basalt", getItemHolder(registries, BlockItemIds.BASALT.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Soul Sand
        createLearnRecipe("soul_sand", Ingredient.of(Items.SOUL_SAND), Items.SOUL_SAND)
                .withEnergy(5);
        createReplicateRecipe("soul_sand", getItemHolder(registries, BlockItemIds.SOUL_SAND.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Soul Soil
        createLearnRecipe("soul_soil", Ingredient.of(Items.SOUL_SOIL), Items.SOUL_SOIL)
                .withEnergy(5);
        createReplicateRecipe("soul_soil", getItemHolder(registries, BlockItemIds.SOUL_SOIL.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Blackstone
        createLearnRecipe("blackstone", Ingredient.of(Items.BLACKSTONE), Items.BLACKSTONE)
                .withEnergy(5);
        createReplicateRecipe("blackstone", getItemHolder(registries, BlockItemIds.BLACKSTONE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Gilded Blackstone
        createLearnRecipe("gilded_blackstone", Ingredient.of(Items.GILDED_BLACKSTONE), Items.GILDED_BLACKSTONE)
                .withEnergy(5);
        createReplicateRecipe("gilded_blackstone", getItemHolder(registries, BlockItemIds.GILDED_BLACKSTONE.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Spore Blossom
        createLearnRecipe("spore_blossom", Ingredient.of(Items.SPORE_BLOSSOM), Items.SPORE_BLOSSOM)
                .withEnergy(5);
        createReplicateRecipe("spore_blossom", getItemHolder(registries, BlockItemIds.SPORE_BLOSSOM.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Obsidian
        createLearnRecipe("obsidian", Ingredient.of(Items.OBSIDIAN), Items.OBSIDIAN);
        createReplicateRecipe("obsidian", getItemHolder(registries, BlockItemIds.OBSIDIAN.item()));

        // Crying Obsidian
        createLearnRecipe("crying_obsidian", Ingredient.of(Items.CRYING_OBSIDIAN), Items.CRYING_OBSIDIAN);
        createReplicateRecipe("crying_obsidian", getItemHolder(registries, BlockItemIds.CRYING_OBSIDIAN.item()));

        // Stone
        createLearnRecipe("stone", Ingredient.of(Items.STONE), Items.STONE);
        createReplicateRecipe("stone", getItemHolder(registries, BlockItemIds.STONE_BRICKS.item()));

        // Ice
        createLearnRecipe("ice", Ingredient.of(Items.ICE), Items.ICE);
        createReplicateRecipe("ice", getItemHolder(registries, BlockItemIds.ICE.item()));

        // Prismarine Shard
        int prismarineEnergy = DRLearnRecipeBuilder.ENERGY_MEDIUM;
        int prismarineTime = DRLearnRecipeBuilder.TIME_MEDIUM;
        createLearnRecipe("prismarine_shard", Ingredient.of(Items.PRISMARINE_SHARD), Items.PRISMARINE_SHARD)
                .withEnergy(prismarineEnergy);
        createLearnRecipe("prismarine", Ingredient.of(Items.PRISMARINE), Items.PRISMARINE_SHARD)
                .withEnergy(prismarineEnergy)
                .withTime(4 * prismarineTime)
                .withProgress(4);
        createLearnRecipe("dark_prismarine", Ingredient.of(Items.DARK_PRISMARINE), Items.PRISMARINE_SHARD)
                .withEnergy(prismarineEnergy)
                .withTime(8 * prismarineTime)
                .withProgress(8);
        createReplicateRecipe("prismarine_shard", getItemHolder(registries, ItemIds.PRISMARINE_SHARD))
                .withEnergy(80);

        // Prismarine Crystals
        createLearnRecipe("prismarine_crystals", Ingredient.of(Items.PRISMARINE_CRYSTALS), Items.PRISMARINE_CRYSTALS);
        createReplicateRecipe("prismarine_crystals", getItemHolder(registries, ItemIds.PRISMARINE_CRYSTALS));

        // Glowstone
        createLearnRecipe("glowstone", Ingredient.of(Items.GLOWSTONE), Items.GLOWSTONE);
        createReplicateRecipe("glowstone", getItemHolder(registries, BlockItemIds.GLOWSTONE.item()));

        // Shroomlight
        createLearnRecipe("shroomlight", Ingredient.of(Items.SHROOMLIGHT), Items.SHROOMLIGHT);
        createReplicateRecipe("shroomlight", getItemHolder(registries, BlockItemIds.SHROOMLIGHT.item()));

        // Magma Block
        createLearnRecipe("magma_block", Ingredient.of(Items.MAGMA_BLOCK), Items.MAGMA_BLOCK);
        createReplicateRecipe("magma_block", getItemHolder(registries, BlockItemIds.MAGMA_BLOCK.item()));

        // Sponge
        createLearnRecipe("sponge", Ingredient.of(Items.SPONGE), Items.SPONGE);
        createReplicateRecipe("sponge", getItemHolder(registries, BlockItemIds.SPONGE.item()));

        // Froglights
        createLearnRecipe("p_froglight", Ingredient.of(Items.PEARLESCENT_FROGLIGHT), Items.PEARLESCENT_FROGLIGHT)
                .withEnergy(40)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("p_froglight", getItemHolder(registries, BlockItemIds.PEARLESCENT_FROGLIGHT.item()))
                .withEnergy(160)
                .withTime(500)
                .withLearnFactor(8);

        createLearnRecipe("o_froglight", Ingredient.of(Items.OCHRE_FROGLIGHT), Items.OCHRE_FROGLIGHT)
                .withEnergy(40)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("o_froglight", getItemHolder(registries, BlockItemIds.OCHRE_FROGLIGHT.item()))
                .withEnergy(160)
                .withTime(500)
                .withLearnFactor(8);

        createLearnRecipe("v_froglight", Ingredient.of(Items.VERDANT_FROGLIGHT), Items.VERDANT_FROGLIGHT)
                .withEnergy(40)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("v_froglight", getItemHolder(registries, BlockItemIds.VERDANT_FROGLIGHT.item()))
                .withEnergy(160)
                .withTime(500)
                .withLearnFactor(8);

        // Dragon Head
        createLearnRecipe("dragon_head", Ingredient.of(Items.DRAGON_HEAD), Items.DRAGON_HEAD)
                .withEnergy(60);
        createReplicateRecipe("dragon_head", getItemHolder(registries, BlockItemIds.DRAGON_HEAD.item()))
                .withEnergy(240)
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH)
                .withLearnFactor(2);

        // Dragon Egg
        createLearnRecipe("dragon_egg", Ingredient.of(Items.DRAGON_EGG), Items.DRAGON_EGG)
                .withEnergy(60);
        createReplicateRecipe("dragon_egg", getItemHolder(registries, BlockItemIds.DRAGON_EGG.item()))
                .withEnergy(240)
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH)
                .withLearnFactor(2);

        // Elytra
        createLearnRecipe("elytra", Ingredient.of(Items.ELYTRA), Items.ELYTRA)
                .withEnergy(60);
        createReplicateRecipe("elytra", getItemHolder(registries, ItemIds.ELYTRA))
                .withEnergy(240)
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH)
                .withLearnFactor(2);

        // Heart of the Sea
        createLearnRecipe("heart_of_the_sea", Ingredient.of(Items.HEART_OF_THE_SEA), Items.HEART_OF_THE_SEA)
                .withEnergy(60);
        createReplicateRecipe("heart_of_the_sea", getItemHolder(registries, ItemIds.HEART_OF_THE_SEA))
                .withEnergy(240)
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH)
                .withLearnFactor(2);

        // Heavy Core
        createLearnRecipe("heavy_core", Ingredient.of(Items.HEAVY_CORE), Items.HEAVY_CORE)
                .withEnergy(200);
        createReplicateRecipe("heavy_core", getItemHolder(registries, BlockItemIds.HEAVY_CORE.item()))
                .withEnergy(800)
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH)
                .withLearnFactor(2);

        // Bucket of Lava
        createLearnRecipe("bucket_of_lava", Ingredient.of(Items.LAVA_BUCKET), Items.LAVA_BUCKET)
                .withEnergy(20);
        createReplicateRecipe("bucket_of_lava", getItemHolder(registries, ItemIds.LAVA_BUCKET))
                .withEnergy(100)
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH)
                .withLearnFactor(2);

        // Dirt
        createLearnRecipe("dirt", getIngredientFromTag(registries, ItemTags.DIRT), Items.DIRT)
                .withEnergy(5);
        createReplicateRecipe("dirt", getItemHolder(registries, BlockItemIds.DIRT.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_LOW);

        // Logs
        createLearnRecipe("acacia_logs", getIngredientFromTag(registries, ItemTags.ACACIA_LOGS), Items.ACACIA_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("acacia_logs", getItemHolder(registries, BlockItemIds.ACACIA_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);

        createLearnRecipe("birch_logs", getIngredientFromTag(registries, ItemTags.BIRCH_LOGS), Items.BIRCH_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("birch_logs", getItemHolder(registries, BlockItemIds.BIRCH_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("cherry_logs", getIngredientFromTag(registries, ItemTags.CHERRY_LOGS), Items.CHERRY_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("cherry_logs", getItemHolder(registries, BlockItemIds.CHERRY_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("dark_oak_logs", getIngredientFromTag(registries, ItemTags.DARK_OAK_LOGS), Items.DARK_OAK_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("dark_oak_logs", getItemHolder(registries, BlockItemIds.DARK_OAK_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("jungle_logs", getIngredientFromTag(registries, ItemTags.JUNGLE_LOGS), Items.JUNGLE_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("jungle_logs", getItemHolder(registries, BlockItemIds.JUNGLE_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("mangrove_logs", getIngredientFromTag(registries, ItemTags.MANGROVE_LOGS), Items.MANGROVE_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("mangrove_logs", getItemHolder(registries, BlockItemIds.MANGROVE_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("oak_logs", getIngredientFromTag(registries, ItemTags.OAK_LOGS), Items.OAK_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("oak_logs", getItemHolder(registries, BlockItemIds.OAK_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("pale_oak_logs", getIngredientFromTag(registries, ItemTags.PALE_OAK_LOGS), Items.PALE_OAK_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("pale_oak_logs", getItemHolder(registries, BlockItemIds.PALE_OAK_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("spruce_logs", getIngredientFromTag(registries, ItemTags.SPRUCE_LOGS), Items.SPRUCE_LOG)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("spruce_logs", getItemHolder(registries, BlockItemIds.SPRUCE_LOG.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("crimson_stems", getIngredientFromTag(registries, ItemTags.CRIMSON_STEMS), Items.CRIMSON_STEM)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("crimson_stems", getItemHolder(registries, BlockItemIds.CRIMSON_STEM.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);
        
        createLearnRecipe("warped_stems", getIngredientFromTag(registries, ItemTags.BIRCH_LOGS), Items.WARPED_STEM)
                .withTime(DRLearnRecipeBuilder.TIME_HIGH);
        createReplicateRecipe("warped_stems", getItemHolder(registries, BlockItemIds.WARPED_STEM.item()))
                .withTime(DRReplicateRecipeBuilder.TIME_HIGH);

        // Dyes
        createLearnRecipe("white", Ingredient.of(Items.DYE.white()), Items.DYE.white());
        createReplicateRecipe("white", getItemHolder(registries, ItemIds.DYE.white()));

        createLearnRecipe("light_gray", Ingredient.of(Items.DYE.lightGray()), Items.DYE.lightGray());
        createReplicateRecipe("light_gray", getItemHolder(registries, ItemIds.DYE.lightGray()));

        createLearnRecipe("gray", Ingredient.of(Items.DYE.gray()), Items.DYE.gray());
        createReplicateRecipe("gray", getItemHolder(registries, ItemIds.DYE.gray()));

        createLearnRecipe("black", Ingredient.of(Items.DYE.black()), Items.DYE.black());
        createReplicateRecipe("black", getItemHolder(registries, ItemIds.DYE.black()));

        createLearnRecipe("brown", Ingredient.of(Items.DYE.brown()), Items.DYE.brown());
        createReplicateRecipe("brown", getItemHolder(registries, ItemIds.DYE.brown()));

        createLearnRecipe("red", Ingredient.of(Items.DYE.red()), Items.DYE.red());
        createReplicateRecipe("red", getItemHolder(registries, ItemIds.DYE.red()));

        createLearnRecipe("orange", Ingredient.of(Items.DYE.orange()), Items.DYE.orange());
        createReplicateRecipe("orange", getItemHolder(registries, ItemIds.DYE.orange()));

        createLearnRecipe("yellow", Ingredient.of(Items.DYE.yellow()), Items.DYE.yellow());
        createReplicateRecipe("yellow", getItemHolder(registries, ItemIds.DYE.yellow()));

        createLearnRecipe("lime", Ingredient.of(Items.DYE.lime()), Items.DYE.lime());
        createReplicateRecipe("lime", getItemHolder(registries, ItemIds.DYE.lime()));

        createLearnRecipe("green", Ingredient.of(Items.DYE.green()), Items.DYE.green());
        createReplicateRecipe("green", getItemHolder(registries, ItemIds.DYE.green()));

        createLearnRecipe("cyan", Ingredient.of(Items.DYE.cyan()), Items.DYE.cyan());
        createReplicateRecipe("cyan", getItemHolder(registries, ItemIds.DYE.cyan()));

        createLearnRecipe("light_blue", Ingredient.of(Items.DYE.lightBlue()), Items.DYE.lightBlue());
        createReplicateRecipe("light_blue", getItemHolder(registries, ItemIds.DYE.lightBlue()));

        createLearnRecipe("blue", Ingredient.of(Items.DYE.blue()), Items.DYE.blue());
        createReplicateRecipe("blue", getItemHolder(registries, ItemIds.DYE.blue()));

        createLearnRecipe("purple", Ingredient.of(Items.DYE.purple()), Items.DYE.purple());
        createReplicateRecipe("purple", getItemHolder(registries, ItemIds.DYE.purple()));

        createLearnRecipe("magenta", Ingredient.of(Items.DYE.magenta()), Items.DYE.magenta());
        createReplicateRecipe("magenta", getItemHolder(registries, ItemIds.DYE.magenta()));

        createLearnRecipe("pink", Ingredient.of(Items.DYE.pink()), Items.DYE.pink());
        createReplicateRecipe("pink", getItemHolder(registries, ItemIds.DYE.pink()));

        // Ores
    }

    protected Holder.Reference<Item> getItemHolder(HolderLookup.Provider registries, ResourceKey<Item> key) {
        return registries.lookupOrThrow(Registries.ITEM).getOrThrow(key);
    }

    protected Ingredient getIngredientFromTag(HolderLookup.Provider registries, TagKey<Item> tagKey) {
        return Ingredient.of(registries.lookupOrThrow(Registries.ITEM).getOrThrow(tagKey));
    }

    protected DRLearnRecipeBuilder createLearnRecipe(String recipeID, Ingredient input, ItemLike output) {
        DRLearnRecipeBuilder builder = DRLearnRecipeBuilder.build(recipeID, input, output);
        this.recipeBuilders.put(builder.getRecipeID(), builder);
        return builder;
    }

    protected DRReplicateRecipeBuilder createReplicateRecipe(String recipeID, Holder<Item> input) {
        DRReplicateRecipeBuilder builder = DRReplicateRecipeBuilder.build(recipeID, input);
        this.recipeBuilders.put(builder.getRecipeID(), builder);
        return builder;
    }

    @Override
    public String getName() {
        return "DRRecipes";
    }
}
