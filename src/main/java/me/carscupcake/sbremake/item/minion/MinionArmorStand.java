package me.carscupcake.sbremake.item.minion;

import lombok.Getter;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;

@Getter
public class MinionArmorStand extends LivingEntity {
    private final Minion minion;
    public MinionArmorStand(Minion minion) {
        super(EntityType.ARMOR_STAND);
        this.minion = minion;
        var meta = (ArmorStandMeta) getEntityMeta();
        meta.setSmall(true);
        meta.setHasNoGravity(true);
        meta.setHasNoBasePlate(true);
        meta.setHasArms(true);
    }
}
