package com.mattdahepic.mdecore.asm;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.8")
public class MDECorePlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"com.mattdahepic.mdecore.asm.transformer.TransformerDelegate"};
    }
    @Override
    public String getModContainerClass() {
        return null;
    }
    @Override
    public String getSetupClass() {
        return null;
    }
    @Override
    public void injectData(Map<String, Object> data) {}
    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
