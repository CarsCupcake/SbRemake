package me.carscupcake.sbremake.entity.impl.test;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.minestom.server.entity.EntityType;

import java.util.function.Function;

@SuppressWarnings("preview")
public class DummyEntity extends SkyblockEntity {
    private float damage = 0f;

    public DummyEntity() {
        super(EntityType.ZOMBIE);
    }

    public DummyEntity(EntityType entityType) {
        super(entityType);
    }

    @Override
    public float getMaxHealth() {
        return 1;
    }

    @Override
    public String getName() {
        return "Dummy";
    }

    @Override
    protected float onDamage(SkyblockPlayer player, float amount) {
        damage = amount;
        return amount;
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(1);
    }

    @Override
    public Function<SkyblockEntity, String> nameTag() {
        return _ -> "§aLast Damage: §c" + (StringUtils.cleanDouble(damage));
    }

    @Override
    public boolean canTakeKnockback() {
        return false;
    }
}
