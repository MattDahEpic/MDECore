package com.mattdahepic.mdecore.command.logic;

import com.mattdahepic.mdecore.command.AbstractCommand;
import com.mattdahepic.mdecore.command.AbstractHelpLogic;
import com.mattdahepic.mdecore.command.CommandMDE;

public class MDEHelpLogic extends AbstractHelpLogic {
    public static MDEHelpLogic instance = new MDEHelpLogic();

    @Override
    public AbstractCommand getBaseCommand () {
        return CommandMDE.instance;
    }
}
