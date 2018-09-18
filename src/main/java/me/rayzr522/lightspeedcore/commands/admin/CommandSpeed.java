package me.rayzr522.lightspeedcore.commands.admin;

import me.rayzr522.lightspeedcore.api.commands.CommandContext;
import me.rayzr522.lightspeedcore.api.commands.CommandResult;
import me.rayzr522.lightspeedcore.api.commands.CommandTarget;
import me.rayzr522.lightspeedcore.api.commands.ICommandHandler;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class CommandSpeed implements ICommandHandler {
    @Override
    public String getCommandName() {
        return "speed";
    }

    @Override
    public List<CommandTarget> getTargets() {
        return Collections.singletonList(CommandTarget.PLAYER);
    }

    @Override
    public CommandResult onCommand(CommandContext ctx) {
        SpeedType type = ctx.getPlayer().isFlying() ? SpeedType.FLYING : SpeedType.WALKING;
        int speed = 1;

        if (ctx.hasArgs(2)) {
            type = matchType(ctx.shift())
                    .orElseThrow(ctx.fail("command.speed.invalid-type"));
        }

        if (ctx.hasArgs()) {
            speed = ctx.shift(Integer::parseInt);
        }

        if (speed < 1 || speed > 10) {
            ctx.tell("command.speed.invalid-speed");
            return CommandResult.FAIL;
        }

        type.apply(ctx.getPlayer(), speed / 10.0f);

        String typeName = getPlugin().trRaw(String.format("command.speed.type.%s", type.name().toLowerCase()));
        if (speed == 1) {
            ctx.tell("command.speed.speed-reset", typeName);
        } else {
            ctx.tell("command.speed.speed-set", typeName, speed);
        }

        return CommandResult.SUCCESS;
    }

    private Optional<SpeedType> matchType(String input) {
        switch (input.toLowerCase()) {
            case "walking":
            case "walk":
            case "w":
            case "run":
                return Optional.of(SpeedType.WALKING);
            case "flying":
            case "fly":
            case "f":
                return Optional.of(SpeedType.FLYING);
            default:
                return Optional.empty();
        }
    }

    enum SpeedType {
        WALKING(Player::setWalkSpeed),
        FLYING(Player::setFlySpeed);

        private final BiConsumer<Player, Float> applierFunction;

        SpeedType(BiConsumer<Player, Float> applierFunction) {
            this.applierFunction = applierFunction;
        }

        public void apply(Player player, float speed) {
            applierFunction.accept(player, speed);
        }
    }
}
