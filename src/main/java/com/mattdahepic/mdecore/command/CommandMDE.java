package com.mattdahepic.mdecore.command;

import com.mattdahepic.mdecore.command.logic.*;
import com.mattdahepic.mdecore.config.MDEConfig;

public class CommandMDE extends AbstractCommand {
    public static CommandMDE instance = new CommandMDE();

    public CommandMDE () {
        if (MDEConfig.enablePos) registerCommandLogic(PosLogic.instance);
        if (MDEConfig.enableTPS) registerCommandLogic(TPSLogic.instance);
        if (MDEConfig.enableTPX) registerCommandLogic(TPXLogic.instance);
        registerCommandLogic(MDEHelpLogic.instance);
        registerCommandLogic(VersionLogic.instance);
        if (MDEConfig.enableKillall) registerCommandLogic(KillAllLogic.instance);
        if (MDEConfig.enablePregen) registerCommandLogic(PregenLogic.instance);
        if (MDEConfig.enableTickrate) registerCommandLogic(TickrateLogic.instance);
        if (MDEConfig.enableInvsee) registerCommandLogic(InvseeLogic.instance);
        if (MDEConfig.enableEnderchest) registerCommandLogic(EnderchestLogic.instance);
        //registerCommandLogic(TrimLogic.instance);
        if (MDEConfig.enableTPA) registerCommandLogic(TPALogic.instance);
    }

    @Override
    public String getName () {
        return "mde";
    }
}
