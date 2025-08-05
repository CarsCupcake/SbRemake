package me.carscupcake.sbremake.entity.impl.end;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.monster.EndermanMeta;
import net.minestom.server.instance.block.Block;

public class SpecialZealot extends SkyblockEntity {
    private final SkyblockPlayer owner;

    public SpecialZealot(SkyblockPlayer owner) {
        super(EntityType.ENDERMAN, MobType.Ender);
        this.owner = owner;
        ((EndermanMeta) this.entityMeta).setCarriedBlockID(Block.END_PORTAL_FRAME.id());
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
        return "Special Zealot";
    }
}
