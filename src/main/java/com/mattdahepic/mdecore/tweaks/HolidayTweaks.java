package com.mattdahepic.mdecore.tweaks;

import com.mattdahepic.mdecore.MDECore;
import com.mattdahepic.mdecore.helpers.RandomHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class HolidayTweaks {
    private static ArrayList<String> alreadyDone = new ArrayList<>();
    public static void doooooo (EntityPlayer player) {
        if (alreadyDone.contains(player.getDisplayNameString())) return;
        alreadyDone.add(player.getDisplayNameString());
        
        Calendar cal = GregorianCalendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
        int week_in_month = cal.get(Calendar.WEEK_OF_MONTH);
        int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

        if (month == Calendar.DECEMBER && day_of_month == 25) { //christmas
            player.sendMessage(new TextComponentString(TextFormatting.RED+"Merry "+TextFormatting.WHITE+"Christmas!"));
            if (RandomHelper.randomChance(15)) {
                player.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE+"You get coal."));
                player.inventory.addItemStackToInventory(new ItemStack(Items.COAL,1,0));
            } else {
                player.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE+"Have a gift!"));
                player.inventory.addItemStackToInventory(new ItemStack(Items.GOLD_NUGGET,3,0));
            }
        } else if (month == Calendar.MARCH && day_of_month >= 22 && day_of_week == Calendar.SUNDAY) { //easter
            player.sendMessage(new TextComponentString(TextFormatting.GRAY+"Happy Easter!\n"+TextFormatting.RED+"Have an egg."));
            player.inventory.addItemStackToInventory(new ItemStack(Items.EGG,1,0));
        } else if (month == Calendar.NOVEMBER && week_in_month == 4 && day_of_week == Calendar.THURSDAY) { //thanksgiving
            player.sendMessage(new TextComponentString(TextFormatting.GOLD+"Happy Thanksgiving!\n"+TextFormatting.GREEN+"Have a turkey!"));
            try {
                player.inventory.addItemStackToInventory(new ItemStack(Items.COOKED_CHICKEN, 1, 0, JsonToNBT.getTagFromJson("{display:{Name:\"Turkey\"}}")));
            } catch (NBTException ex) {MDECore.logger.warn("Error creating nbt.");}
        } else if (month == Calendar.MARCH && day_of_month == 21) { //first day of spring
            player.sendMessage(new TextComponentString(TextFormatting.LIGHT_PURPLE+"Have a happy spring!\n"+TextFormatting.GREEN+"Have a flower!"));
            player.inventory.addItemStackToInventory(new ItemStack(Blocks.RED_FLOWER,1,0));
        } else if (month == Calendar.JUNE && day_of_month == 20) { //first day of summer
            player.sendMessage(new TextComponentString(TextFormatting.GOLD+"Have a great summer!\n"+TextFormatting.RED+"Have some sand"));
            player.inventory.addItemStackToInventory(new ItemStack(Blocks.SAND,1,0));
        } else if (month == Calendar.SEPTEMBER && day_of_month == 22) { //first day of fall
            player.sendMessage(new TextComponentString("The leaves are "+TextFormatting.GOLD+"fall"+TextFormatting.WHITE+"ing!"));
            player.inventory.addItemStackToInventory(new ItemStack(Blocks.LEAVES,1,0));
        } else if (month == Calendar.DECEMBER && day_of_month == 21) { //first day of winter
            player.sendMessage(new TextComponentString(TextFormatting.WHITE+"Have a happy winter!\nHave some snow."));
            player.inventory.addItemStackToInventory(new ItemStack(Items.SNOWBALL,1,0));
        }
    }
}
