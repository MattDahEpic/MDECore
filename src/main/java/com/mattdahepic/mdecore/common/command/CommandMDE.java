package com.mattdahepic.mdecore.common.command;

import com.mattdahepic.mdecore.common.registries.CommandRegistry;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.block.EnderChestBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.WorldWorkerManager;

public class CommandMDE {
    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> builder = Commands.literal("mattdahepic")
                .requires(s -> s.hasPermissionLevel(2))
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
                                .then(Commands.argument("size", IntegerArgumentType.integer(1))
                                        .then(Commands.argument("dimension", DimensionArgument.getDimension())
                                                .executes(CommandMDE::generate)))))
                .then(Commands.literal("tpx")
                        .executes(ctx -> { ctx.getSource().sendFeedback(new StringTextComponent("Vanilla now lets you teleport across dimensions with /execute in <dimension> run tp @p <location>"),false); return Command.SINGLE_SUCCESS; }));
        LiteralCommandNode<CommandSource> command = dispatcher.register(builder);
        dispatcher.register(Commands.literal("mde").redirect(command));
    }

    public static int help (CommandContext<CommandSource> ctx) {
        for (int i = 0; i < 5; i++) {
            ctx.getSource().sendFeedback(new TranslationTextComponent("mdecore.command.mde.help."+i),false);
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int pos (CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity player = EntityArgument.getPlayer(ctx,"player");
        if (player == null) throw EntityArgument.PLAYER_NOT_FOUND.create();
        ctx.getSource().sendFeedback(new TranslationTextComponent("mdecore.command.mde.pos",player.getDisplayName(),Math.floor(player.getPosX()),Math.floor(player.getPosY()),Math.floor(player.getPosZ()),player.world.dimension.field_240900_c_.toString()), false);
        return Command.SINGLE_SUCCESS;
    }
    public static int enderchest (CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity looker = ctx.getSource().asPlayer();
        PlayerEntity lookee = EntityArgument.getPlayer(ctx,"player");
        if (lookee == null) throw EntityArgument.PLAYER_NOT_FOUND.create();
        if (!looker.world.isRemote) {
            looker.closeScreen();
            EnderChestInventory ec = lookee.getInventoryEnderChest();
            ITextComponent title =  ((TextComponent)lookee.getDisplayName()).func_230529_a_(new StringTextComponent("'s ")).func_230529_a_(new TranslationTextComponent("container.enderchest"));
            looker.openContainer(new SimpleNamedContainerProvider((id,player,entity) -> ChestContainer.createGeneric9X3(id,player,ec),title));
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int invsee (CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        PlayerEntity looker = ctx.getSource().asPlayer();
        PlayerEntity lookee = EntityArgument.getPlayer(ctx,"player");
        if (lookee == null) throw EntityArgument.PLAYER_NOT_FOUND.create();
        if (!looker.world.isRemote) {
            looker.closeScreen();
            PlayerInventory pi = lookee.inventory;
            ITextComponent title = ((TextComponent)lookee.getDisplayName()).func_230529_a_(new StringTextComponent("'s ")).func_230529_a_(new TranslationTextComponent("container.inventory"));
            looker.openContainer(new SimpleNamedContainerProvider((id,player,entity) -> new ChestContainer(ContainerType.GENERIC_9X4,id,player,lookee.inventory,4),title));
        }
        return Command.SINGLE_SUCCESS;
    }
    public static int generate (CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        BlockPos center = BlockPosArgument.getBlockPos(ctx,"center");
        int size = IntegerArgumentType.getInteger(ctx,"size");
        ServerWorld dimension = DimensionArgument.getDimensionArgument(ctx,"dimension");

        BlockPos chunkcenter = new BlockPos(center.getX() >> 4, 0, center.getZ() >> 4);
        int chunksize = size >> 4;

        UpgradedChunkGenWorker worker = new UpgradedChunkGenWorker(ctx.getSource(),chunkcenter,chunksize,dimension);
        ctx.getSource().sendFeedback(worker.getStartMessage(ctx.getSource()),true);
        WorldWorkerManager.addWorker(worker);

        return Command.SINGLE_SUCCESS;
    }
}
