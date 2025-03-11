package me.carscupcake.sbremake.util;

/**
 * A place to store special characters
 */
public enum Characters {
    Skull(Character.toChars(9760)[0]);

    private final char character;

    Characters(char character){
        this.character = character;
    }

    @Override
    public String toString() {
        return "%s".formatted(character);
    }
}
