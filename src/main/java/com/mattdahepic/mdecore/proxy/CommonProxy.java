package com.mattdahepic.mdecore.proxy;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.EnvironmentHelper;
import com.mattdahepic.mdecore.recipe.NBTRespectingShapedOreRecipe;
import com.mattdahepic.mdecore.recipe.NBTRespectingShapelessOreRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;

public class CommonProxy {
    public void setupTextures () {}
    public void setupItems () {
        if (EnvironmentHelper.isDeobf) {
            GameRegistry.register(MDECore.debugItem.setRegistryName("debug_item"));
        }
    }
    public void registerRecipeTypes () {
        RecipeSorter.register("mdecore:nbtshapedore", NBTRespectingShapedOreRecipe.class, RecipeSorter.Category.SHAPED,"after:minecraft:shaped");
        RecipeSorter.register("mdecore:nbtshapelessore", NBTRespectingShapelessOreRecipe.class, RecipeSorter.Category.SHAPELESS,"after:minecraft:shapeless");
    }
}
