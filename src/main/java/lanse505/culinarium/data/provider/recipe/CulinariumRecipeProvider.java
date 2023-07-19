package lanse505.culinarium.data.provider.recipe;

import lanse505.culinarium.Culinarium;
import lanse505.culinarium.common.register.CulinariumBlockRegistry;
import lanse505.culinarium.common.register.CulinariumItemRegistry;
import lanse505.culinarium.common.register.CulinariumRecipeRegistry;
import lanse505.culinarium.data.provider.recipe.builder.CulinariumChoppingRecipeBuilder;
import lanse505.culinarium.data.provider.recipe.builder.CulinariumHarvestRecipeBuilder;
import lanse505.culinarium.data.provider.recipe.builder.CulinariumMillingRecipeBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Consumer;

public class CulinariumRecipeProvider extends RecipeProvider {

    public CulinariumRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    protected static void milling(Consumer<FinishedRecipe> consumer, String id, Ingredient input, ItemStack... output) {
        ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "milling/" + id);
        CulinariumMillingRecipeBuilder.milling(rl, output).input(input).save(consumer, rl);
    }

    protected static void milling(Consumer<FinishedRecipe> consumer, String id, Ingredient input, int duration, ItemStack... output) {
        ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "milling/" + id);
        CulinariumMillingRecipeBuilder.milling(rl, output).input(input).duration(duration).save(consumer, rl);
    }

    protected static void chopping(Consumer<FinishedRecipe> consumer, String id, Ingredient input, ItemStack output) {
        ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "chopping/" + id);
        CulinariumChoppingRecipeBuilder.chopping(rl, output).input(input).save(consumer, rl);
    }

    protected static void chopping(Consumer<FinishedRecipe> consumer, String id, Ingredient input, int chops, ItemStack output) {
        ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "chopping/" + id);
        CulinariumChoppingRecipeBuilder.chopping(rl, output).input(input).chops(chops).save(consumer, rl);
    }

    protected static void harvest(Consumer<FinishedRecipe> consumer, String id, BlockState target, BlockState result, boolean isFuzzy) {
        ResourceLocation rl = new ResourceLocation(Culinarium.MODID, "harvest/" + id);
        if (isFuzzy)
            CulinariumHarvestRecipeBuilder.harvest(rl, result).target(target).fuzzy().save(consumer, rl);
        else
            CulinariumHarvestRecipeBuilder.harvest(rl, result).target(target).save(consumer, rl);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        // Boilerplate
        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(CulinariumItemRegistry.SLICE_OF_BREAD.get()),
                        RecipeCategory.FOOD, CulinariumItemRegistry.TOAST.get(), 0.35F, 200)
                .unlockedBy("has_slice_of_toast", has(CulinariumItemRegistry.SLICE_OF_BREAD.get()))
                .save(consumer, new ResourceLocation(Culinarium.MODID, "smelting/slice_to_toast"));
        SpecialRecipeBuilder
                .special((RecipeSerializer<? extends CraftingRecipe>) CulinariumRecipeRegistry.DOUGH_MAKING.getSerializer())
                .save(consumer, new ResourceLocation(Culinarium.MODID, "dough_making").toString());
        // Milling
        milling(consumer,
                "wheat_berries_to_flour",
                Ingredient.of(CulinariumItemRegistry.WHEAT_BERRIES.get()),
                5,
                new ItemStack(CulinariumItemRegistry.FLOUR.get(), 2));
        milling(consumer,
                "wheat_husks_to_flour",
                Ingredient.of(CulinariumItemRegistry.WHEAT_HUSKS.get()),
                new ItemStack(CulinariumItemRegistry.FLOUR.get(), 1));
        // Chopping
        chopping(consumer,
                "bread_to_slices",
                Ingredient.of(Items.BREAD),
                12,
                new ItemStack(CulinariumItemRegistry.SLICE_OF_BREAD.get(), 1));
        chopping(consumer,
                "carrot_to_chunks",
                Ingredient.of(Items.CARROT),
                new ItemStack(CulinariumItemRegistry.CARROT_CHUNKS.get(), 1));
        // Vanilla Harvest
        harvest(consumer, "harvest_wheat",
                Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 7),
                Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 3),
                true);
        harvest(consumer, "harvest_carrots",
                Blocks.CARROTS.defaultBlockState().setValue(CropBlock.AGE, 7),
                Blocks.CARROTS.defaultBlockState().setValue(CropBlock.AGE, 3),
                true);
        harvest(consumer, "harvest_potatoes",
                Blocks.POTATOES.defaultBlockState().setValue(CropBlock.AGE, 7),
                Blocks.POTATOES.defaultBlockState().setValue(CropBlock.AGE, 3),
                true);
        harvest(consumer, "harvest_beetroots",
                Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, 3),
                Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, 1),
                true);
        // Culinarium Harvest
        harvest(consumer, "harvest_rye",
                CulinariumBlockRegistry.RYE.get().defaultBlockState().setValue(CropBlock.AGE, 7),
                CulinariumBlockRegistry.RYE.get().defaultBlockState().setValue(CropBlock.AGE, 3),
                true);
    }

}
