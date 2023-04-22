package com.mattdahepic.mdecore.common.registries;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandRegistry {
    public static final SimpleCommandExceptionType MISSING_ARGUMENT = new SimpleCommandExceptionType(Component.translatable("commands.missingargument"));
    public static int missingArgument (CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        throw MISSING_ARGUMENT.create();
    }

    private static List<Consumer<CommandDispatcher<CommandSourceStack>>> commands = new ArrayList<>();

    public static void registerCommand (Consumer<CommandDispatcher<CommandSourceStack>> command) {
        commands.add(command);
    }

    public static void register (RegisterCommandsEvent event) {
        for (Consumer<CommandDispatcher<CommandSourceStack>> source : commands) {
            source.accept(event.getDispatcher());
        }
    }
}
