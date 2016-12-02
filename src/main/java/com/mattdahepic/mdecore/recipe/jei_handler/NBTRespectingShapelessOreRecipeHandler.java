package com.mattdahepic.mdecore.recipe.jei_handler;

import com.mattdahepic.mdecore.recipe.NBTRespectingShapelessOreRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class NBTRespectingShapelessOreRecipeHandler implements IRecipeHandler<NBTRespectingShapelessOreRecipe> {
    @Override
    public Class<NBTRespectingShapelessOreRecipe> getRecipeClass () {
        return NBTRespectingShapelessOreRecipe.class;
    }
    @Override
    public String getRecipeCategoryUid (NBTRespectingShapelessOreRecipe r) {
        return VanillaRecipeCategoryUid.CRAFTING;
    }
    @Override
    public IRecipeWrapper getRecipeWrapper (@Nonnull NBTRespectingShapelessOreRecipe recipe) {
        return new NBTRespectingShapelessOreRecipeWrapper(recipe.getInput(),recipe.getRecipeOutput());
    }
    @Override
    public boolean isRecipeValid (@Nonnull NBTRespectingShapelessOreRecipe recipe) {
        return recipe.getInput().size() > 0;
    }

    public static class NBTRespectingShapelessOreRecipeWrapper extends BlankRecipeWrapper {
        private final List<ItemStack> inputs;
        private final ItemStack output;
        public NBTRespectingShapelessOreRecipeWrapper (List<ItemStack> input, ItemStack output) {
            this.inputs = input;
            this.output = output;
        }
        @Override public void getIngredients (IIngredients ings) {
            ings.setInputs(ItemStack.class,inputs);
            ings.setOutput(ItemStack.class,output);
        }
    }
}
