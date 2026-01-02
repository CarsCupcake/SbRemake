package me.carscupcake.sbremake.worlds.impl.dungeon;

/***
 * An Exception that signals that there was a minor generation issue but a retry should be safe
 */
public class DungeonException extends RuntimeException {
    public DungeonException(String message) {
        super(message);
    }
}
