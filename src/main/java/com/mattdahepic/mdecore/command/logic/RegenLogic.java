package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.ICommandLogic;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
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
        return I18n.translateToLocal("mdecore.command.regen.usage");
    }
    @Override
    public void handleCommand (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BlockPos pos = sender.getPosition();
        if (!sender.getEntityWorld().isRemote) {
            try {
                Chunk oldChunk = sender.getEntityWorld().getChunkFromBlockCoords(pos);

                if (sender.getEntityWorld() instanceof WorldServer) {
                    WorldServer worldServer = (WorldServer) sender.getEntityWorld();
                    IChunkProvider chunkProviderGenerate = worldServer.getChunkProvider();

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
                    chunkProviderGenerate.provideChunk(oldChunk.xPosition,oldChunk.zPosition);
                    sender.addChatMessage(new TextComponentString(TextFormatting.YELLOW+I18n.translateToLocal("mdecore.command.regen.success")));
                }
            } catch (Exception e) {
                throw new CommandException(I18n.translateToLocal("mdecore.command.regen.failure"));
            }
        }
    }
    @Override
    public List<String> getTabCompletionOptions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }
}
