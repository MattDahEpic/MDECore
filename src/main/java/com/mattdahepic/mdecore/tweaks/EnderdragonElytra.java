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
        if (e.getEntity() instanceof EntityDragon) {
            if (MDEConfig.dragonDropsElytra) {
                e.getEntity().worldObj.spawnEntityInWorld(new EntityItem(e.getEntity().worldObj,e.getEntity().posX,e.getEntity().posY,e.getEntity().posZ,new ItemStack(Items.elytra,1,0)));
            }
        }
    }
}
