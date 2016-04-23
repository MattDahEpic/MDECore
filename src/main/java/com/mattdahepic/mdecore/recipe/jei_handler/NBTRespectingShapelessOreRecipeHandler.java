package com.mattdahepic.mdecore.recipe.jei_handler;

import com.mattdahepic.mdecore.recipe.NBTRespectingShapelessOreRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class NBTRespectingShapelessOreRecipeHandler implements IRecipeHandler<NBTRespectingShapelessOreRecipe> {
    @Override
    public Class<NBTRespectingShapelessOreRecipe> getRecipeClass () {
        return NBTRespectingShapelessOreRecipe.class;
    }
    @Override
    public String getRecipeCategoryUid () {
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

    public static class NBTRespectingShapelessOreRecipeWrapper implements ICraftingRecipeWrapper {
        private final List inputs;
        private final ItemStack output;
        public NBTRespectingShapelessOreRecipeWrapper (List input, ItemStack output) {
            this.inputs = input;
            this.output = output;
        }
        @Override public List getInputs () {return inputs;}
        @Override public List<ItemStack> getOutputs () {return Collections.singletonList(output);}
        @Override public void drawInfo (@Nonnull Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {}
        @Override public List<FluidStack> getFluidInputs() {return null;}
        @Override public List<FluidStack> getFluidOutputs() {return null;}
        @Override public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {}
        @Override public List<String> getTooltipStrings(int mouseX, int mouseY) {return null;}
        @Override public boolean handleClick(@Nonnull Minecraft mc, int mouseX, int mouseY, int mouseButton) {return false;}
    }
}
