package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;

public class VoidgloomSeraph4 extends SkyblockEntity {
    public VoidgloomSeraph4() {
        super(EntityType.ENDERMAN);
    }

    @Override
    public float getMaxHealth() {
        return 410_0000;
    }

    @Override
    public String getName() {
        return "Voidgloom Seraph IV";
    }
}
