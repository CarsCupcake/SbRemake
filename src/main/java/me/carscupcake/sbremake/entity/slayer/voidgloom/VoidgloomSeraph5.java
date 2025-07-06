package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;

public class VoidgloomSeraph5 extends SkyblockEntity {
    public VoidgloomSeraph5() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 500_000_00;
    }

    @Override
    public String getName() {
        return "Voidgloom Seraph V";
    }
}
