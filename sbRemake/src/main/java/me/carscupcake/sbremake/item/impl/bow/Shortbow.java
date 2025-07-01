package me.carscupcake.sbremake.item.impl.bow;

public interface Shortbow extends BowItem {
    /**
     * Shortbow cooldown
     *
     * @return the shortbow cooldown in ms, default is 0.5s -> 500 ms
     */
    default long getShortbowCooldown() {
        return 500;
    }

    default long getShortbowCooldown(double attackspeed) {
        if (getShortbowCooldown() == -1) return -1;
        if (attackspeed >= 100) return getShortbowCooldown() / 2;
        double d = attackspeed / 100;
        d = 1 - d;
        return (long) ((((double) getShortbowCooldown()) / 2d) + ((((double) getShortbowCooldown()) / 2d) * d));
    }
}
