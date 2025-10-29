package me.carscupcake.sbremake.entity.impl.deepCaverns;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.armor.lapisArmor.LapisBoots;
import me.carscupcake.sbremake.item.impl.armor.lapisArmor.LapisChestplate;
import me.carscupcake.sbremake.item.impl.armor.lapisArmor.LapisHelmet;
import me.carscupcake.sbremake.item.impl.armor.lapisArmor.LapisLeggings;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.impl.DeepCaverns;
import net.minestom.server.color.Color;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class LapisZombies extends SkyblockEntity implements SkillXpDropper {
    public LapisZombies() {
        super(EntityType.ZOMBIE, new LootTable<SbItemStack>()
                .addLoot(new ItemLoot(ISbItem.get(Material.ROTTEN_FLESH), 1, 1d)).addLootTable(new LootTable<SbItemStack>()
                                .addLoot(new ItemLoot(ISbItem.get(LapisBoots.class), 1, 0.01d))
                                .addLoot(new ItemLoot(ISbItem.get(LapisLeggings.class), 1, 0.01d))
                                .addLoot(new ItemLoot(ISbItem.get(LapisChestplate.class), 1, 0.01d))
                                .addLoot(new ItemLoot(ISbItem.get(LapisHelmet.class), 1, 0.01d))
                                .setSingleLoot()
                        , 0.01), MobType.Undead);
        setEquipment(EquipmentSlot.HELMET, ItemStack.of(Material.BLUE_STAINED_GLASS));
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(new Color(0x0000ff)).build());
        setEquipment(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(new Color(0x0000ff)).build());
        setEquipment(EquipmentSlot.BOOTS, new ItemBuilder(Material.LEATHER_BOOTS).setLeatherColor(new Color(0x0000ff)).build());
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.15f);
    }

    @Override
    public void spawn() {
        this.addAIGroup(zombieAiGroup(this, DeepCaverns.Region.LapisQuarry, true));
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
