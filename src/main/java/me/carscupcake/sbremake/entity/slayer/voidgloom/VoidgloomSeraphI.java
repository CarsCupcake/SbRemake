package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;

public class VoidgloomSeraph1 extends SkyblockEntity {
    public VoidgloomSeraph1() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 300_000;
    }

    @Override
    public String getName() {
        return "Voidgloom Seraph I";
    }
}
