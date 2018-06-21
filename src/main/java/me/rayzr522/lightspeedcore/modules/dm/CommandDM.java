package me.rayzr522.lightspeedcore.modules.dm;

import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.CommandTarget;
import me.rayzr522.lightspeedcore.api.commands.ModuleCommand;

import java.util.Arrays;
import java.util.List;

public class CommandDM extends ModuleCommand<DMModule> {
    public CommandDM(DMModule module) {
        super(module);
    }

    @Override
    public String getCommandName() {
        return "dm";
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("msg", "whisper", "message", "tell");
    }

    @Override
    public String getPermission() {
        return "dm";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return CommandTarget.PLAYER.only();
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        if (!ctx.hasArgs(2)) {
            return CommandResult.SHOW_USAGE;
        }

        getModule().sendDM(ctx.getPlayer(), ctx.shiftPlayer(), ctx.remainder());

        return CommandResult.SUCCESS;
    }
}
