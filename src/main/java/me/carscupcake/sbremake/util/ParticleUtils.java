package me.carscupcake.sbremake.util;

import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

public class ParticleUtils {
    public static void spawnParticle(Instance instance, Point point, Particle particle, int count) {
        ParticlePacket packet = new ParticlePacket(particle, point, Pos.ZERO, 0, count);
        for (Player player : instance.getPlayers()) {
            if (player.getPosition().distance(point) > 16 * 8) continue;
            player.sendPacket(packet);
        }
    }

    public static void spawnParticle(Instance instance, Point point, Particle particle, int count, Vec velocity, float speed) {
        ParticlePacket packet = new ParticlePacket(particle, point, velocity, speed, count);
        for (Player player : instance.getPlayers()) {
            if (player.getPosition().distance(point) > 16 * 8) continue;
            player.sendPacket(packet);
        }
    }
}
