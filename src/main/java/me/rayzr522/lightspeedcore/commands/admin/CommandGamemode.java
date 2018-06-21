package me.rayzr522.lightspeedcore.commands.admin;

import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.CommandTarget;
import me.rayzr522.lightspeedcore.api.commands.ICommandHandler;
import org.bukkit.GameMode;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CommandGamemode implements ICommandHandler {
    @Override
    public String getCommandName() {
        return "gamemode";
    }

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("gm");
    }

    @Override
    public List<CommandTarget> getTargets() {
        return CommandTarget.PLAYER.only();
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        if (!ctx.hasArgs()) {
            return CommandResult.SHOW_USAGE;
        }

        GameMode gameMode = ctx.shift(name -> matchGamemode(name)
                .orElseThrow(ctx.fail("command.gamemode.invalid-gamemode", name)));

        String permission = String.format("gamemode.%s", gameMode.name().toLowerCase());

        if (!getPlugin().checkPermission(ctx.getPlayer(), permission, true)) {
            return CommandResult.FAIL;
        }

        ctx.getPlayer().setGameMode(gameMode);
        ctx.tell("command.gamemode.set", gameMode.name().toLowerCase());

        return CommandResult.SUCCESS;
    }

    private Optional<GameMode> matchGamemode(String input) {
        switch (input.toLowerCase()) {
            case "survival":
            case "s":
            case "0":
                return Optional.of(GameMode.SURVIVAL);
            case "creative":
            case "c":
            case "1":
                return Optional.of(GameMode.CREATIVE);
            case "adventure":
            case "adv":
            case "a":
            case "2":
                return Optional.of(GameMode.ADVENTURE);
            case "spectator":
            case "spec":
            case "sp":
            case "3":
                return Optional.of(GameMode.SPECTATOR);
            default:
                return Optional.empty();
        }
    }
}
