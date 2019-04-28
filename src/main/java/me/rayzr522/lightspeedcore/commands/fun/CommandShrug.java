package me.rayzr522.lightspeedcore.commands.fun;

import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.CommandTarget;
import me.rayzr522.lightspeedcore.api.commands.ICommandHandler;

import java.util.Collections;
import java.util.List;

public class CommandShrug implements ICommandHandler {
    @Override
    public String getCommandName() {
        return "shrug";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return Collections.singletonList(CommandTarget.PLAYER);
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        ctx.getPlayer().chat(String.format("%s \u00af\\_(\u30c4)_/\u00af", ctx.remainder()).trim());
        return CommandResult.SUCCESS;
    }
}
