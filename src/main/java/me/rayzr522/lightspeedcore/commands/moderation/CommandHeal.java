package me.rayzr522.lightspeedcore.commands.moderation;

import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.CommandTarget;
import me.rayzr522.lightspeedcore.api.commands.ICommandHandler;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class CommandHeal implements ICommandHandler {
    @Override
    public String getCommandName() {
        return "heal";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return Collections.singletonList(CommandTarget.PLAYER);
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        Player target = ctx.getPlayer();
        boolean other = false;

        if (ctx.hasArgs()) {
            if (!getPlugin().checkPermission(ctx.getSender(), "heal.others", true)) {
                return CommandResult.SUCCESS;
            }

            target = ctx.shiftPlayer();
            other = true;
        }

        ctx.tell(target, "command.heal.success");
        target.setHealth(target.getHealthScale());

        if (other) {
            ctx.tell("command.heal.other", target.getName());
        }

        return CommandResult.SUCCESS;
    }
}
