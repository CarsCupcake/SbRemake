package me.carscupcake.sbremake.entity.impl.test;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.text.Component;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class DummyEntity extends SkyblockEntity {
    private float damage = 0f;
    private LivingEntity armorStand;

    public DummyEntity() {
        super(EntityType.ZOMBIE);
    }

    public DummyEntity(EntityType entityType, MobType... mobTypes) {
        super(entityType, mobTypes);
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
    public int getLevel() {
        return 999;
    }

    @Override
    public boolean canTakeKnockback() {
        return false;
    }

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos spawnPosition) {
        return super.setInstance(instance, spawnPosition).thenAccept(ignored -> {
            armorStand = new LivingEntity(EntityType.ARMOR_STAND);
            ((ArmorStandMeta) armorStand.getEntityMeta()).setMarker(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setInstance(instance, getPosition().add(0, getEyeHeight(), 0));
            armorStand.setCustomNameVisible(true);
            armorStand.setInvisible(true);
            armorStand.set(DataComponents.CUSTOM_NAME, Component.text("§aLast Damage: §c0"));
            armorStand.setNoGravity(true);
        });
    }

    @Override
    public void update() {
        super.update();
        if (armorStand != null) {
            armorStand.set(DataComponents.CUSTOM_NAME, Component.text("§aLast Damage: §c" + (StringUtils.cleanDouble(damage))));
        }
    }

    @Override
    protected void remove(boolean permanent) {
        super.remove(permanent);
        armorStand.remove();
        armorStand = null;
    }
}
