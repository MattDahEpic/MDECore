package com.mattdahepic.mdecore.recipe.jei_handler;

import com.mattdahepic.mdecore.recipe.NBTRespectingShapedOreRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class NBTRespectingShapedOreRecipeHandler implements IRecipeHandler<NBTRespectingShapedOreRecipe> {
    @Override
    public Class<NBTRespectingShapedOreRecipe> getRecipeClass () {
        return NBTRespectingShapedOreRecipe.class;
    }
    @Override
    public String getRecipeCategoryUid (NBTRespectingShapedOreRecipe r) {
        return VanillaRecipeCategoryUid.CRAFTING;
    }
    @Override
    public IRecipeWrapper getRecipeWrapper (@Nonnull NBTRespectingShapedOreRecipe recipe) {
        return new NBTRespectingShapedOreRecipeWrapper(recipe.getInput(),recipe.getRecipeOutput());
    }
    @Override
    public boolean isRecipeValid (@Nonnull NBTRespectingShapedOreRecipe recipe) {
        return recipe.getInput().size() > 0;
    }

    public static class NBTRespectingShapedOreRecipeWrapper extends BlankRecipeWrapper {
        private final List<ItemStack> inputs;
        private final ItemStack output;
        public NBTRespectingShapedOreRecipeWrapper (List<ItemStack> input, ItemStack output) {
            this.inputs = input;
            this.output = output;
        }
        @Override public void getIngredients (IIngredients ings) {
            ings.setInputs(ItemStack.class,inputs);
            ings.setOutput(ItemStack.class,output);
        }
    }
}
