package me.carscupcake.sbremake.util;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.monster.GuardianMeta;
import net.minestom.server.instance.Instance;

@Getter
public class Laser {
    private final LivingEntity guardian;
    private final LivingEntity squid;
    public Laser(Instance instance, Pos start, Pos end) {
        guardian = new LivingEntity(EntityType.GUARDIAN);
        guardian.setNoGravity(true);
        guardian.setInvisible(true);
        guardian.setInstance(instance, start);
        squid = new LivingEntity(EntityType.SQUID);
        squid.setNoGravity(true);
        squid.setInvisible(true);
        squid.setInstance(instance, end);

        var guardianMeta = (GuardianMeta) guardian.getEntityMeta();
        guardianMeta.setTarget(squid);
    }

    public void stop() {
        guardian.remove();
        squid.remove();
    }

    public void rotateAroundStartY(double degree) {
        var dir = squid.getPosition().sub(guardian.getPosition())
                .asVec().rotateAroundY(degree);
        squid.teleport(guardian.getPosition().add(dir));
    }
}
