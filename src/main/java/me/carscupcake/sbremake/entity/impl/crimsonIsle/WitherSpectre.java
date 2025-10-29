package me.carscupcake.sbremake.entity.impl.crimsonIsle;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.CoinLoot;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import net.minestom.server.utils.time.TimeUnit;

import java.util.Set;

public class WitherSpectre extends SkyblockEntity implements SkillXpDropper {
    private static final String HEAD_VALUE = "ewogICJ0aW1lc3RhbXAiIDogMTYzOTUxMjkwNTczNywKICAicHJvZmlsZUlkIiA6ICI3MzgyZGRmYmU0ODU0NTVjODI1ZjkwMGY4OGZkMzJmOCIsCiAgInByb2ZpbGVOYW1lIiA6ICJCdUlJZXQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjZhYTVhY2ZkOTczZjY3YmE4ZTI0OWFlMDdhMTFhZTdjYTcxM2M5MDBkOTBkMDljZTA5MDNjNzlkZGQ3ZGQ5ZSIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
    private static final Color CHESTPLATE_COLOR = new Color(921622);
    private static final Region WITHER_SPECTRE_REGION = new CuboidRegion("null", Set.of(new BoundingBox(new Vec(-432, 85, -533), new Vec(-384, 113, -483))));

    private final Entity nameTag = new Entity(EntityType.ARMOR_STAND);

    public WitherSpectre() {
        super(EntityType.WITHER_SKELETON, new LootTable<SbItemStack>().addLoot(new ItemLoot(Material.GUNPOWDER)).addLoot(new CoinLoot(20)),
                MobType.Wither, MobType.Arcane);
        setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(HEAD_VALUE).build());
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(CHESTPLATE_COLOR).build());
        setItemInHand(PlayerHand.MAIN, new ItemBuilder(Material.STONE_SWORD).build());
        scheduler().buildTask(() -> {
            if (instance != null)
                ParticleUtils.spawnParticle(getInstance(), getPosition(), Particle.WITCH, 5, Vec.ZERO, 0.2f);
        }).repeat(TimeUnit.SERVER_TICK.getDuration()).schedule();
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.15f);
        setInvisible(true);
    }

    @Override
    public void spawn() {
        addAIGroup(SkyblockEntity.zombieAiGroup(this, WITHER_SPECTRE_REGION, true));
    }

    @Override
    public float getMaxHealth() {
        return 300_000;
    }

    @Override
    public double getDamage() {
        return 2_000;
    }

    @Override
    public String getName() {
        return "Wither Spectre";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 70;
    }

    @Override
    public int getLevel() {
        return 70;
    }
}
