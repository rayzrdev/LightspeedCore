package me.rayzr522.lightspeedcore.commands;


import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.CommandTarget;
import me.rayzr522.lightspeedcore.api.commands.ICommandHandler;
import me.rayzr522.lightspeedcore.utils.Utils;

import java.util.List;

public class CommandSpawn implements ICommandHandler {
    @Override
    public String getCommandName() {
        return "spawn";
    }

    @Override
    public String getPermission() {
        return "spawn";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return CommandTarget.PLAYER.only();
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        ctx.getPlayer().teleport(Utils.centerLocation(ctx.getPlayer().getWorld().getSpawnLocation()));
        return CommandResult.SUCCESS;
    }

}
