package com.mattdahepic.mdecore.common.registries;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommandRegistry {
    public static final SimpleCommandExceptionType MISSING_ARGUMENT = new SimpleCommandExceptionType(new TranslationTextComponent("commands.missingargument"));
    public static int missingArgument (CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        throw MISSING_ARGUMENT.create();
    }

    private static List<Consumer<CommandDispatcher<CommandSource>>> commands = new ArrayList<>();

    public static void registerCommand (Consumer<CommandDispatcher<CommandSource>> command) {
        commands.add(command);
    }

    public static void register (RegisterCommandsEvent event) {
        for (Consumer<CommandDispatcher<CommandSource>> source : commands) {
            source.accept(event.getDispatcher());
        }
    }
}
