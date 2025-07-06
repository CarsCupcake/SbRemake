package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;

public class VoidgloomSeraph3 extends SkyblockEntity {
    public VoidgloomSeraph3() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 5000_000;
    }

    @Override
    public String getName() {
        return "Voidgloom Seraph III";
    }
}

