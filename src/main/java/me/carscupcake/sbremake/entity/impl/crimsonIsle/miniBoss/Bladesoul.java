package me.carscupcake.sbremake.entity.impl.crimsonIsle.miniBoss;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Bladesoul extends CrimonIsleBossEntity implements SkillXpDropper {
    private final LivingEntity blazeHelmet;
    public Bladesoul() {
        super(EntityType.WITHER_SKELETON,
                MobType.Wither, MobType.Skeletal, MobType.Arcane);
        setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(Material.GOLDEN_AXE));
        setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD)
                .setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTYzMDA5NGYyN2IxMWEyZTkwMmUyNmMxNmZlNWRmZTM1ZmI0OTMxYWI1Zjk0N2JiYzlhY2NhYjJlZTUwMTcwNCJ9fX0=")
                .build());
        blazeHelmet = new LivingEntity(EntityType.BLAZE);
        blazeHelmet.setCustomName(Component.text("Dinnerbone"));
    }

    @Override
    public float getMaxHealth() {
        return 50_000_000;
    }

    @Override
    public double getDamage() {
        return 4_000;
    }

    @Override
    public String getName() {
        return "Bladesoul";
    }

    @Override
    public Function<SkyblockEntity, String> nameTag() {
        return NameTagType.Slayer;
    }

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos spawnPosition) {
        return super.setInstance(instance, spawnPosition).thenRun(() -> blazeHelmet.setInstance(instance, spawnPosition)
                .thenRun(() -> addPassenger(blazeHelmet)));
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 4_000;
    }

    @Override
    protected boolean showCustomNameTag() {
        return super.showCustomNameTag();
    }

    @Override
    public void remove() {
        super.remove();
        blazeHelmet.remove();
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
        if (health <= 0)
            blazeHelmet.setHealth(Math.max(0, health));
    }
}
