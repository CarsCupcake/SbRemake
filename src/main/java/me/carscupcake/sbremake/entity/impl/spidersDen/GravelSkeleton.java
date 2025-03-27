package me.carscupcake.sbremake.entity.impl.spidersDen;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class GravelSkeleton extends SkyblockEntity implements SkillXpDropper
{
    public GravelSkeleton() {
        super(EntityType.SKELETON);
        setEquipment(EquipmentSlot.MAIN_HAND, ItemStack.builder(Material.BOW).build());
        addAIGroup(skeletonAiGroup(this));
    }

    @Override
    public float getMaxHealth() {
        return 100;
    }

    @Override
    public String getName() {
        return "Gravel Skeleton";
    }

    @Override
    public double getDamage() {
        return 35;
    }

    @Override
    public int getLevel() {
        return 2;
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 2;
    }

    @Override
    public void kill() {
        super.kill();
        //TODO: add bone
    }
}
