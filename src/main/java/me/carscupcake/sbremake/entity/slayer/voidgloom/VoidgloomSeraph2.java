package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;

public class VoidgloomSeraph2 extends SkyblockEntity {
    public VoidgloomSeraph2() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 12_0000;
    }

    @Override
    public String getName() {
        return "Voidgloom Seraph II";
    }
}
