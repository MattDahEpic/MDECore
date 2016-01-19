package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import com.mattdahepic.mdecore.helpers.TranslationHelper;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;

public class RegenLogic implements ICommandLogic {
    public static RegenLogic instance = new RegenLogic();

    @Override
    public String getCommandName () {
        return "regen";
    }
    @Override
    public int getPermissionLevel () {
        return 2;
    }
    @Override
    public String getCommandSyntax () {
        return TranslationHelper.getTranslatedString("mdecore.command.regen.usage");
    }
    @Override
    public void handleCommand (ICommandSender sender, String[] args) throws CommandException {
        BlockPos pos = sender.getPosition();
        if (!sender.getEntityWorld().isRemote) {
            try {
                Chunk oldChunk = sender.getEntityWorld().getChunkFromBlockCoords(pos);

                if (sender.getEntityWorld() instanceof WorldServer) {
                    WorldServer worldServer = (WorldServer) sender.getEntityWorld();
                    IChunkProvider chunkProviderGenerate = worldServer.theChunkProviderServer.serverChunkGenerator;

                    Chunk newChunk = chunkProviderGenerate.provideChunk(oldChunk.xPosition, oldChunk.zPosition);

                    for (int x = 0; x < 16; x++) {
                        for (int z = 0; z < 16; z++) {
                            for (int y = 0; y < sender.getEntityWorld().getHeight(); y++) {
                                IBlockState blockState = newChunk.getBlockState(new BlockPos(x,y,z));
                                
                                worldServer.setBlockState(new BlockPos(x + oldChunk.xPosition * 16,y,z + oldChunk.zPosition * 16),blockState);

                                TileEntity tileEntity = newChunk.getTileEntity(new BlockPos(x,y,z),null);

                                if (tileEntity != null) worldServer.setTileEntity(new BlockPos(x + oldChunk.xPosition * 16,y,z + oldChunk.zPosition * 16),tileEntity);
                            }
                        }
                    }
                    oldChunk.setTerrainPopulated(false);
                    chunkProviderGenerate.populate(chunkProviderGenerate, oldChunk.xPosition, oldChunk.zPosition);
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.YELLOW+TranslationHelper.getTranslatedString("mdecore.command.regen.success")));
                }
            } catch (Exception e) {
                throw new CommandException(TranslationHelper.getTranslatedString("mdecore.command.regen.failure"));
            }
        }
    }
    @Override
    public List<String> addTabCompletionOptions (ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}
