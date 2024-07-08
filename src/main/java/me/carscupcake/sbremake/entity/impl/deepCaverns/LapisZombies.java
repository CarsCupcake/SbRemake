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
import net.minestom.server.item.component.DyedItemColor;
import org.jetbrains.annotations.NotNull;

public class LapisZombies extends SkyblockEntity implements SkillXpDropper {
    public LapisZombies() {
        super(EntityType.ZOMBIE);
        this.addAIGroup(zombieAiGroup(this, DeepCaverns.Region.LapisQuarry));
        setEquipment(EquipmentSlot.HELMET, ItemStack.of(Material.BLUE_STAINED_GLASS));
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(new DyedItemColor(0x0000ff)).build());
        setEquipment(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(new DyedItemColor(0x0000ff)).build());
        setEquipment(EquipmentSlot.BOOTS, new ItemBuilder(Material.LEATHER_BOOTS).setLeatherColor(new DyedItemColor(0x0000ff)).build());
        getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.15f);
    }

    @Override
    public float getMaxHealth() {
        return 200;
    }

    @Override
    public String getName() {
        return "Lapis Zombie";
    }

    @Override
    public double getDamage() {
        return 50;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 12;
    }

    @Override
    public int getLevel() {
        return 7;
    }
}
