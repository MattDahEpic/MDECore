package com.mattdahepic.mdecore.tweaks;

import com.mattdahepic.mdecore.config.MDEConfig;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WaterBottleCauldron {
    @SubscribeEvent
    public void playerInteract (PlayerInteractEvent e) {
        if (MDEConfig.waterBottlesFillCauldrons) {
            if (e.getAction() == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK && e.getWorld().getBlockState(e.getPos()).getBlock() == Blocks.cauldron) { //if right click on cauldron
                if (e.getEntityPlayer().inventory.getCurrentItem().getItem() == Items.potionitem && e.getEntityPlayer().inventory.getCurrentItem().getMetadata() == 0) { //if water bottle
                    int currentLevel = e.getWorld().getBlockState(e.getPos()).getValue(BlockCauldron.LEVEL);
                    if (currentLevel < 3) { //if not full
                        e.getWorld().setBlockState(e.getPos(), e.getWorld().getBlockState(e.getPos()).withProperty(BlockCauldron.LEVEL, currentLevel + 1)); //fill cauldron
                        if (!e.getEntityPlayer().capabilities.isCreativeMode) {
                            e.getEntityPlayer().inventory.getCurrentItem().stackSize--; //remove one water bottle
                            if (!e.getEntityPlayer().inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle))) { //give the bottle back, but if they cant fit it...
                                e.getWorld().spawnEntityInWorld(new EntityItem(e.getWorld(), e.getPos().getX() + 0.5D, e.getPos().getY() + 0.5D, e.getPos().getZ() + 0.5D, new ItemStack(Items.glass_bottle))); //...drop it!
                            }
                        }
                    }
                }
            }
        }
    }
}
