package me.rayzr522.lightspeedcore.commands.moderation;

import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.ModuleCommand;
import me.rayzr522.lightspeedcore.modules.whois.WhoisModule;
import me.rayzr522.lightspeedcore.utils.Utils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Date;

public class CommandWhois extends ModuleCommand<WhoisModule> {
    public CommandWhois(WhoisModule module) {
        super(module);
    }

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        if (!ctx.hasArgs()) {
            return CommandResult.SHOW_USAGE;
        }

        OfflinePlayer target = Utils.getOfflinePlayer(ctx.shift());

        if (!target.hasPlayedBefore()) {
            // TODO: That player hasn't played before!
            return CommandResult.SUCCESS;
        }

        if (target.isOnline() && target.getPlayer() != null) {
            Player onlinePlayer = target.getPlayer();

            ctx.tell(
                    "command.whois.info-online",
                    onlinePlayer.getName(),
                    onlinePlayer.getWorld(),
                    onlinePlayer.getLocation().getBlockX(),
                    onlinePlayer.getLocation().getBlockY(),
                    onlinePlayer.getLocation().getBlockZ(),
                    onlinePlayer.getAddress().getAddress().toString().replace("/", "")
            );
        } else {
            Location location = getModule().getLastKnownLocation(target.getUniqueId()).orElseThrow(ctx.fail("command.whois.missing-info"));
            String ip = getModule().getLastKnownIp(target.getUniqueId()).orElseThrow(ctx.fail("command.whois.missing-info"));

            ctx.tell(
                    "command.whois.info-offline",
                    target.getName(),
                    new Date(target.getLastPlayed()),
                    location.getWorld(),
                    location.getBlockX(),
                    location.getBlockY(),
                    location.getBlockZ(),
                    ip
            );
        }

        return CommandResult.SUCCESS;
    }
}
