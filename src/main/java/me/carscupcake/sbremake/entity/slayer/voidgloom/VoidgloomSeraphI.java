package me.carscupcake.sbremake.entity.slayer.voidgloom;

import net.minestom.server.item.Material;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import net.minestom.server.entity.EntityType;

public class VoidgloomSeraph1 extends SkyblockEntity {
    public VoidgloomSeraph1() {
        super(EntityType.ENDERMAN);
    }
    
    @Overide
    public void spawn() {
        super.spawn();
        add AiGroup(zombieAiGroup(this, true));
    }

    
    @Override
    public float getMaxHealth() {
        return 300_000;
    }

    @Override
    public float getDamage() {
        return 1_200;
    }
    
    @Override
    public String getName() {
        return "Voidgloom Seraph I";
    }
}
