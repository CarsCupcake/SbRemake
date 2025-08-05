package me.carscupcake.sbremake.entity.slayer.zombie.minibosses;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.RevenantFlesh;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class RevenantSycophant extends SkyblockEntity implements SkillXpDropper {
    public RevenantSycophant() {
        super(EntityType.ZOMBIE, new LootTable<SbItemStack>().addLoot(new ItemLoot(ISbItem.get(RevenantFlesh.class), 1, 1)), MobType.Undead);
        setBoots(ItemStack.of(Material.IRON_BOOTS));
        setLeggings(new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setGlint(true).build());
        setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setItemInHand(PlayerHand.MAIN, new ItemBuilder(Material.DIAMOND_SWORD).setGlint(true).build());
        addAIGroup(zombieAiGroup(this, false));
    }

    @Override
    public float getMaxHealth() {
        return 24_000;
    }

    @Override
    public double getDamage() {
        return 320;
    }

    @Override
    public int getLevel() {
        return 80;
    }

    @Override
    public String getName() {
        return "Revenant Sycophant";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 360;
    }

    @Override
    public void damage(SkyblockPlayer player) {
        super.damage(player);
    }
}
