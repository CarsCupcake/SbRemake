package me.carscupcake.sbremake.entity.impl.deepCaverns;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.worlds.impl.DeepCaverns;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.Material;

public class MinerSkeleton20 extends SkyblockEntity implements SkillXpDropper {
    public MinerSkeleton20() {
        super(EntityType.SKELETON);
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.15f);
        setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.DIAMOND_BLOCK).setGlint(true).build());
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setEquipment(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.DIAMOND_LEGGINGS).setGlint(true).build());
        setEquipment(EquipmentSlot.BOOTS, new ItemBuilder(Material.DIAMOND_BOOTS).setGlint(true).build());
        setEquipment(EquipmentSlot.MAIN_HAND, new ItemBuilder(Material.BOW).setGlint(true).build());
    }

    @Override
    public void spawn() {
        this.addAIGroup(skeletonAiGroup(this, DeepCaverns.Region.ObsidianSanctuary));
    }

    @Override
    public float getMaxHealth() {
        return 300;
    }

    @Override
    public String getName() {
        return "Miner Skeleton";
    }

    @Override
    public double getDamage() {
        return 200;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 24;
    }

    @Override
    public int getLevel() {
        return 20;
    }
}
