package me.carscupcake.sbremake.entity.impl.end;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.EntityType;

import java.util.Random;

public class Zealot extends SkyblockEntity {
    private static final int BASE_SPAWN_RATE = 420;
    private static final Random RANDOM = new Random();

    public Zealot() {
        super(EntityType.ENDERMAN, MobType.Ender);
    }

    @Override
    public float getMaxHealth() {
        return 13_000;
    }

    @Override
    public double getDamage() {
        return 1250;
    }

    @Override
    public String getName() {
        return "Zealot";
    }

    @Override
    public void kill() {
        super.kill();
        if (getLastDamager() != null) {
            SkyblockPlayer player = getLastDamager();
            player.setZealotPity(player.getZealotPity() + 1);
            var spawnRate = BASE_SPAWN_RATE;
            if (player.getZealotPity() > BASE_SPAWN_RATE * 2) {
                spawnRate /= 4;
            } else if (player.getZealotPity() > BASE_SPAWN_RATE * 1.50) {
                spawnRate /= 3;
            } else if (player.getZealotPity() > BASE_SPAWN_RATE) {
                spawnRate /= 2;
            }
            if (RANDOM.nextDouble() <= 1d / spawnRate) {
                new SpecialZealot(player).setInstance(this.instance, position);
            }
        }
    }
}
