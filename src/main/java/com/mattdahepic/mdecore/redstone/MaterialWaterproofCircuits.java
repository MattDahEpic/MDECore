package com.mattdahepic.mdecore.redstone;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class MaterialWaterproofCircuits extends Material {
    public MaterialWaterproofCircuits(MapColor color) {
        super(color);
        this.setAdventureModeExempt();
        this.setNoPushMobility();
    }
    public boolean isSolid() {
        return false;
    }
    /**
     * Will prevent grass from growing on dirt underneath and kill any grass below it if it returns true
     */
    public boolean blocksLight()
    {
        return false;
    }

    /**
     * Returns if this material is considered solid or not
     */
    public boolean blocksMovement()
    {
        return true;
    }
}
