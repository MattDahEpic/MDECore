package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.AbstractSingleLogicCommand;
import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.config.MDEConfig;
import com.mattdahepic.mdecore.helpers.TeleportHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.List;
import java.util.TimerTask;

public class TPALogic extends AbstractSingleLogicCommand implements ICommandLogic {
    public static TPALogic instance = new TPALogic();
    
    private static HashMap<String,String> pendingConfirms = new HashMap<String, String>(); //target, sender
    private static HashMap<String,String> pendingTeleports = new HashMap<String, String>();
    
    @Override
    public String getCommandName () {
        return "tpa";
    }
    @Override
    public int getPermissionLevel () {
        return 0;
    }
    @Override
    public String getCommandSyntax () {
        return "/mde tpa <player>";
    }
    @Override
    public void handleCommand (final MinecraftServer server, final ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 1) AbstractCommand.throwUsages(instance);
        
        final EntityPlayerMP senderPlayer = AbstractCommand.getCommandSenderAsPlayer(sender);
        
        if (args[0].equals("confirm")) {
            if (pendingConfirms.containsKey(sender.getName())) {
                final String originalSender = pendingConfirms.get(sender.getName());
                pendingTeleports.put(sender.getName(),originalSender);
                pendingConfirms.remove(sender.getName(),originalSender);
                final EntityPlayerMP teleportee = server.getPlayerList().getPlayerByUsername(originalSender);
                teleportee.addChatMessage(new TextComponentString(TextFormatting.YELLOW+"Preparing to teleport, stand still!"));
                new java.util.Timer().schedule(new TimerTask() { //teleport after delay, if not moved
                    @Override
                    public void run() {
                        if (pendingTeleports.containsKey(sender.getName())) {
                            pendingTeleports.remove(sender.getName(),originalSender);
                            if (senderPlayer.dimension != teleportee.dimension) {
                                TeleportHelper.transferPlayerToDimension(teleportee,senderPlayer.dimension,server.getPlayerList());
                            }
                            teleportee.setPositionAndUpdate(senderPlayer.posX,senderPlayer.posY,senderPlayer.posZ);
                            sender.addChatMessage(new TextComponentString(TextFormatting.GREEN+"Teleport successful."));
                            teleportee.addChatMessage(new TextComponentString(TextFormatting.GREEN+"Teleport successful."));
                        } //if not, the teleportee moved
                    }
                }, MathHelper.floor_double(MDEConfig.tpaWaitTime*1000));
                (new Thread() { //cancel teleport if moved
                    @Override
                    public void run() {
                        BlockPos cachePosition = teleportee.getPosition();
                        while (cachePosition.equals(teleportee.getPosition()) || pendingTeleports.containsKey(originalSender)) {/*block execution until move or teleport*/}
                        if (pendingTeleports.containsKey(originalSender)) { //has not teleported
                            pendingTeleports.remove(sender.getName(), originalSender);
                            sender.addChatMessage(new TextComponentString(TextFormatting.RED + "Teleport canceled."));
                            teleportee.addChatMessage(new TextComponentString(TextFormatting.RED + "Teleport canceled."));
                        }
                    }
                }).start();
            } else {
                sender.addChatMessage(new TextComponentString(TextFormatting.RED+"There are no pending teleports to you."));
            }
        } else {
            final EntityPlayerMP target = AbstractCommand.getPlayer(server, sender, args[0]);
            if (target.getName().equals(sender.getName())) throw new CommandException("That's you silly!");
            if (!MDEConfig.tpaCrossDimension) {
                if (target.dimension != senderPlayer.dimension) throw new CommandException("Your server does not allow cross-dimension tpa. Try again when you and the target are in the same dimension.");
            }
            sender.addChatMessage(new TextComponentString(TextFormatting.DARK_GREEN+"Waiting for target to confirm, stand still"));
            target.addChatMessage(new TextComponentString(TextFormatting.AQUA+sender.getName()+TextFormatting.LIGHT_PURPLE+" wants to teleport to you.\n"+TextFormatting.LIGHT_PURPLE+"Type "+TextFormatting.GREEN+"/mde tpa confirm"+TextFormatting.LIGHT_PURPLE+" to confirm or "+TextFormatting.RED+"wait 10 seconds"+TextFormatting.LIGHT_PURPLE+" to deny."));
            pendingConfirms.put(target.getName(),sender.getName());
            new java.util.Timer().schedule(new TimerTask() { //10 seconds confirm time
                @Override
                public void run() {
                    try {
                        pendingConfirms.remove(target.getName(),sender.getName());
                    } catch (Exception ignored) {/*player has already confirmed*/}
                }
            },10000);
        }
    }
    @Override
    public List<String> getTabCompletionOptions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return args.length == 1 ? AbstractCommand.getPlayerNamesStartingWithLastArg(server,args) : null;
    }
}
