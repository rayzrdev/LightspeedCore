package me.rayzr522.lightspeedcore.commands.moderation;


import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.ICommandHandler;
import org.bukkit.entity.Player;

public class CommandKick implements ICommandHandler {
    @Override
    public String getCommandName() {
        return "kick";
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        if (!ctx.hasArgs()) {
            return CommandResult.SHOW_USAGE;
        }

        Player target = ctx.shiftPlayer();

        if (ctx.hasArgs()) {
            target.kickPlayer(getPlugin().trRaw("command.kick.reason", ctx.remainder()));
        } else {
            target.kickPlayer(null);
        }

        ctx.tell("command.kick.success", target.getName());

        return CommandResult.SUCCESS;
    }
}
