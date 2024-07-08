package me.carscupcake.sbremake.entity.impl.deepCaverns;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.worlds.impl.DeepCaverns;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class MinerSkeleton15 extends SkyblockEntity implements SkillXpDropper {
    public MinerSkeleton15() {
        super(EntityType.SKELETON);
        this.addAIGroup(skeletonAiGroup(this, DeepCaverns.Region.DiamondReserve));
        getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15f);
        setEquipment(EquipmentSlot.HELMET, ItemStack.of(Material.DIAMOND_HELMET));
        setEquipment(EquipmentSlot.CHESTPLATE, ItemStack.of(Material.DIAMOND_CHESTPLATE));
        setEquipment(EquipmentSlot.LEGGINGS, ItemStack.of(Material.DIAMOND_LEGGINGS));
        setEquipment(EquipmentSlot.BOOTS, ItemStack.of(Material.DIAMOND_BOOTS));
        setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.of(Material.BOW));
    }

    @Override
    public float getMaxHealth() {
        return 250;
    }

    @Override
    public String getName() {
        return "Miner Skeleton";
    }

    @Override
    public double getDamage() {
        return 150;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 20;
    }

    @Override
    public int getLevel() {
        return 15;
    }
}
