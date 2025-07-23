package me.carscupcake.sbremake.worlds.impl.dungeon;

import lombok.Getter;

@Getter
public enum FacingDirection {
    North("north"),
    East("east"),
    South("south"),
    West("west"),
    Up("up"),
    Down("down");

    private final String id;

    FacingDirection(String id) {
        this.id = id;
    }

    public static FacingDirection fromId(String id) {
        for (FacingDirection direction : FacingDirection.values()) {
            if (direction.id.equals(id)) {
                return direction;
            }
        }
        return null;
    }

    public FacingDirection next() {
        return switch (this) {
            case North -> East;
            case East -> South;
            case South -> West;
            case West -> North;
            default -> this;
        };
    }
}
