package com.mattdahepic.mdecore.tweaks;

import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.helpers.ItemHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.StringUtils;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnderdragonDrop {
    @SubscribeEvent
    public void entityDeath (LivingDeathEvent e) {
        if (e.getEntity() instanceof EntityDragon) {
            if (!StringUtils.isNullOrEmpty(MDEConfig.dragonDrop)) {
                e.getEntity().worldObj.spawnEntityInWorld(new EntityItem(e.getEntity().worldObj,e.getEntity().posX,e.getEntity().posY,e.getEntity().posZ, ItemHelper.getItemFromName(MDEConfig.dragonDrop)));
            }
        }
    }
}
