package me.rayzr522.lightspeedcore.modules.deathlog;

import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.CommandTarget;
import me.rayzr522.lightspeedcore.api.commands.ModuleCommand;

import java.util.*;

public class CommandDeathLog extends ModuleCommand<DeathLogModule> {
    public CommandDeathLog(DeathLogModule module) {
        super(module);
    }

    @Override
    public String getCommandName() {
        return "deathlog";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return Collections.singletonList(CommandTarget.PLAYER);
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        Collection<DeathLogEntry> deathLogs = getModule().getDeathLogsFor(ctx.getPlayer());

        if (deathLogs.size() < 1) {
            ctx.tell("command.deathlog.none");
        } else {
            deathLogs.stream()
                    .sorted(Comparator.comparingLong(DeathLogEntry::getTime))
                    .forEach(entry -> {
                        if (entry.getLocation().getWorld() == null) {
                            return;
                        }

                        ctx.tell(
                                "command.deathlog.entry",
                                new Date(entry.getTime()),
                                entry.getLocation().getBlockX(),
                                entry.getLocation().getBlockY(),
                                entry.getLocation().getBlockZ(),
                                entry.getLocation().getWorld().getName()
                        );
                    });
        }

        return CommandResult.SUCCESS;
    }
}
