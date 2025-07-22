package me.carscupcake.sbremake.util;

import net.minestom.server.coordinate.Pos;

public record Pos2d(int x, int z) {

    public Pos asPos() {
        return new Pos(x * 32, 0, z * 32);
    }

    public int getMapX() {
        return x * 32;
    }

    public int getMapZ() {
        return z * 32;
    }

    public Pos2d add(int x, int z) {
        return new Pos2d(this.x + x, this.z + z);
    }
}
