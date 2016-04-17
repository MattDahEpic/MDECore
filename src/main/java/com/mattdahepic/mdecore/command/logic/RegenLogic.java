package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
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
import net.minecraft.world.World;
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
        /*
        Tested Strategies:
        ChunkProviderServer.dropChunk
        IChunkProvider.provideChunk with chunk.setTerrainPopulated(false)
        set every block using new chunk
         */
        World world = sender.getEntityWorld();
        Chunk chunk;
        switch (args.length) {
            case 3:
                chunk = world.getChunkFromChunkCoords(Integer.parseInt(args[1]),Integer.parseInt(args[2]));
                break;
            case 1:
                chunk = world.getChunkFromBlockCoords(sender.getPosition());
                break;
            default:
                AbstractCommand.throwUsages(this);
                return;
        }
        if (!world.isRemote) {
            try {
                if (world instanceof WorldServer) {
                    IChunkProvider chunkProvider = world.getChunkProvider();

                    Chunk newChunk = chunkProvider.provideChunk(chunk.xPosition,chunk.zPosition);
                    for (int x = 0; x < 16; x++) {
                        for (int y = 0; y < world.getHeight(); y++) {
                            for (int z = 0; z < 16; z++) {
                                BlockPos chunkPos = new BlockPos(x,y,z);
                                BlockPos worldPos = new BlockPos(x + chunk.xPosition * 16,y,z + chunk.zPosition * 16);

                                IBlockState block = newChunk.getBlockState(chunkPos);
                                TileEntity tile = newChunk.getTileEntity(chunkPos,null);

                                world.setBlockState(worldPos,block);
                                if (tile != null) {
                                    world.setTileEntity(worldPos,tile);
                                }
                            }
                        }
                    }
                    chunk.setModified(true);
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
