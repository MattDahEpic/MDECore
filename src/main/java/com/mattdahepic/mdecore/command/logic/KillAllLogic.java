package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class KillAllLogic implements ICommandLogic {
    public static final String USAGE = "/mde killall [partial name]";
    public static KillAllLogic instance = new KillAllLogic();

    @Override
    public String getCommandName () {
        return "killall";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return USAGE;
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) {
        int killCount = 0;
        Map<String,Integer> names = new HashMap<String,Integer>();
        String target = null;
        boolean all = false;
        if (args.length > 1) {
            target = args[1].toLowerCase();
            all = "*".equals(target);
        }
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            synchronized (world) {
                for (Entity entity : (List<Entity>)world.loadedEntityList) {
                    if (entity != null && !(entity instanceof EntityPlayer)) { //does it exist and is it not a player?
                        String entityName = EntityList.getEntityString(entity);
                        if (target != null || all) {
                            if (all || entityName != null && entityName.toLowerCase(Locale.US).contains(target)) {
                                names.put(entityName,names.get(entityName) != null ? names.get(entityName)+1 : 1);
                                killCount++;
                                world.removeEntity(entity);
                            }
                        } else if (entity instanceof IMob) { //is hostile?
                            if (entityName == null) entityName = entity.getClass().getName();
                            names.put(entityName,names.get(entityName) != null ? names.get(entityName)+1 : 1);
                            killCount++;
                            world.removeEntity(entity);
                        }
                    }
                }
            }
        }
        if (killCount > 0) {
            String finalNames = "";
            for (String name : names.keySet()) {
                finalNames = finalNames + EnumChatFormatting.RED + names.get(name) + EnumChatFormatting.WHITE + "x" + EnumChatFormatting.YELLOW + name + EnumChatFormatting.WHITE + ", ";
            }
            finalNames = finalNames.substring(0, finalNames.length() - 2);
            sender.addChatMessage(new ChatComponentText(String.format((target != null ? "Removed %d entities. (%s)" : "Removed %d hostile mobs. (%s)"), killCount, finalNames)));
        } else {
            sender.addChatMessage(new ChatComponentText(target != null ? "No matching entities found!" : "No hostile mobs found!"));
        }
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}