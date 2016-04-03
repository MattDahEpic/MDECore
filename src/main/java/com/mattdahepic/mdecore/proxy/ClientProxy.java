package com.mattdahepic.mdecore.proxy;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.EnvironmentHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void setupTextures () {
        if (EnvironmentHelper.isDeobf) {
            ModelLoader.setCustomModelResourceLocation(MDECore.debugItem,0,new ModelResourceLocation("mdecore:debug_item","inventory"));
        }
    }
}
