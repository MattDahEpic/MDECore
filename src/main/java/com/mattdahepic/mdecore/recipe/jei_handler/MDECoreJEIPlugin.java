package com.mattdahepic.mdecore.recipe.jei_handler;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

import javax.annotation.Nonnull;

@JEIPlugin
public class MDECoreJEIPlugin extends BlankModPlugin {
    public static IJeiHelpers helpers;

    @Override
    public void register (@Nonnull IModRegistry registry) {
        helpers = registry.getJeiHelpers();
        registry.addRecipeHandlers(new NBTRespectingShapelessOreRecipeHandler(),new NBTRespectingShapedOreRecipeHandler());
    }
}
