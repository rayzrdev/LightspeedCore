package me.rayzr522.lightspeedcore.api.commands.exceptions;


/**
 * Meant to be used for any problem that happened while executing a command that needs to be shown to the user.
 * <p>
 * Automatically handled in the {@link me.rayzr522.lightspeedcore.api.commands.InternalCommandExecutor internal command executor} and shown to the player.
 */
public class GenericCommandException extends RuntimeException {
    public GenericCommandException(String message) {
        super(message);
    }
}
