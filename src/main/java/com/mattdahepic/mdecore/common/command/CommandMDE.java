package com.mattdahepic.mdecore.common.command;

import com.mattdahepic.mdecore.common.registries.CommandRegistry;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.WorldWorkerManager;

public class CommandMDE {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("mattdahepic")
                .requires(s -> s.hasPermission(2))
                .executes(CommandRegistry::missingArgument)
                .then(Commands.literal("help")
                        .executes(CommandMDE::help))
                .then(Commands.literal("pos")
                        .executes(CommandRegistry::missingArgument)
                        .then(Commands.argument("player",EntityArgument.player())
                                .executes(CommandMDE::pos)))
                .then(Commands.literal("enderchest")
                        .executes(CommandRegistry::missingArgument)
                        .then(Commands.argument("player",EntityArgument.player())
                                .executes(CommandMDE::enderchest)))
                .then(Commands.literal("invsee")
                        .executes(CommandRegistry::missingArgument)
                        .then(Commands.argument("player",EntityArgument.player())
                                .executes(CommandMDE::invsee)))
                .then(Commands.literal("generate")
                        .executes(CommandRegistry::missingArgument)
                        .then(Commands.argument("center", BlockPosArgument.blockPos())
                                .executes(CommandRegistry::missingArgument)
                                .then(Commands.argument("size", IntegerArgumentType.integer(1))
                                        .executes(CommandRegistry::missingArgument)
                                        .then(Commands.argument("dimension", DimensionArgument.dimension())
                                                .executes(CommandMDE::generate)))))
                .then(Commands.literal("tpx")
                        .executes(ctx -> { ctx.getSource().sendSuccess(Component.literal("Vanilla now lets you teleport across dimensions with /execute in <dimension> run tp @p <location>"),false); return Command.SINGLE_SUCCESS; }));
        LiteralCommandNode<CommandSourceStack> command = dispatcher.register(builder);
        dispatcher.register(Commands.literal("mde").redirect(command));
    }

    public static int help (CommandContext<CommandSourceStack> ctx) {
        for (int i = 0; i < 5; i++) {
            ctx.getSource().sendSuccess(Component.translatable("mdecore.command.mde.help."+i),false);
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int pos (CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player player = EntityArgument.getPlayer(ctx,"player");
        if (player == null) throw EntityArgument.NO_PLAYERS_FOUND.create();
        ctx.getSource().sendSuccess(Component.translatable("mdecore.command.mde.pos",player.getDisplayName(),Math.floor(player.getX()),Math.floor(player.getY()),Math.floor(player.getZ()),player.level.dimension.location.toString()), false);
        return Command.SINGLE_SUCCESS;
    }
    public static int enderchest (CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player looker = ctx.getSource().getPlayerOrException();
        Player lookee = EntityArgument.getPlayer(ctx,"player");
        if (lookee == null) throw EntityArgument.NO_PLAYERS_FOUND.create();
        if (!looker.level.isClientSide) {
            looker.closeContainer();
            PlayerEnderChestContainer ec = lookee.getEnderChestInventory();
            Component title =  ((MutableComponent)lookee.getDisplayName()).append(Component.literal("'s ")).append(Component.translatable("container.enderchest"));
            looker.openMenu(new SimpleMenuProvider((id,player,entity) -> ChestMenu.threeRows(id,player,ec),title));
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int invsee (CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Player looker = ctx.getSource().getPlayerOrException();
        Player lookee = EntityArgument.getPlayer(ctx,"player");
        if (lookee == null) throw EntityArgument.NO_PLAYERS_FOUND.create();
        if (!looker.level.isClientSide) {
            looker.closeContainer();
            Inventory pi = lookee.getInventory();
            Component title = ((MutableComponent)lookee.getDisplayName()).append(Component.literal("'s ")).append(Component.translatable("container.inventory"));
            looker.openMenu(new SimpleMenuProvider((id,player,entity) -> new ChestMenu(MenuType.GENERIC_9x4,id,player,lookee.getInventory(),4),title));
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int generate (CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        BlockPos center = BlockPosArgument.getLoadedBlockPos(ctx,"center");
        int size = IntegerArgumentType.getInteger(ctx,"size");
        ServerLevel dimension = DimensionArgument.getDimension(ctx,"dimension");

        BlockPos chunkcenter = new BlockPos(center.getX() >> 4, 0, center.getZ() >> 4);
        int chunksize = size >> 4;

        UpgradedChunkGenWorker worker = new UpgradedChunkGenWorker(ctx.getSource(),chunkcenter,chunksize,dimension);
        ctx.getSource().sendSuccess(worker.getStartMessage(ctx.getSource()),true);
        WorldWorkerManager.addWorker(worker);

        return Command.SINGLE_SUCCESS;
    }
}
