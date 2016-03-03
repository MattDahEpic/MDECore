package com.mattdahepic.mdecore.tweaks;

import com.mattdahepic.mdecore.config.MDEConfig;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EnderdragonElytra {
    @SubscribeEvent
    public void entityDeath (LivingDeathEvent e) {
        if (e.entity instanceof EntityDragon) {
            if (MDEConfig.dragonDropsElytra) {
                e.entity.worldObj.spawnEntityInWorld(new EntityItem(e.entity.worldObj,e.entity.posX,e.entity.posY,e.entity.posZ,new ItemStack(Items.elytra,1,0)));
            }
        }
    }
}
