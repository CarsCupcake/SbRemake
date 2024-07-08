package me.carscupcake.sbremake.entity.impl.deepCaverns;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.ItemBuilder;
import me.carscupcake.sbremake.worlds.impl.DeepCaverns;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class MinerZombie20 extends SkyblockEntity implements SkillXpDropper {
    public MinerZombie20() {
        super(EntityType.ZOMBIE);
        this.addAIGroup(zombieAiGroup(this, DeepCaverns.Region.ObsidianSanctuary));
        getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15f);
        setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.DIAMOND_BLOCK).setGlint(true).build());
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setEquipment(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.DIAMOND_LEGGINGS).setGlint(true).build());
        setEquipment(EquipmentSlot.BOOTS, new ItemBuilder(Material.DIAMOND_BOOTS).setGlint(true).build());
        setEquipment(EquipmentSlot.MAIN_HAND, new ItemBuilder(Material.DIAMOND_SWORD).setGlint(true).build());
    }

    @Override
    public float getMaxHealth() {
        return 300;
    }

    @Override
    public String getName() {
        return "Miner Zombie";
    }

    @Override
    public double getDamage() {
        return 250;
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
