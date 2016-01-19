package com.mattdahepic.mdecore.helpers;

import net.minecraftforge.common.DimensionManager;

import java.util.Arrays;

public class DimensionHelper {
    public static boolean isDimIDUsed (int dimID) {
        return Arrays.asList(DimensionManager.getStaticDimensionIDs()).contains(dimID);
    }
}
