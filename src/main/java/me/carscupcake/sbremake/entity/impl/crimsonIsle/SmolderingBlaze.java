package me.carscupcake.sbremake.entity.impl.crimsonIsle;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.goal.BlazeGoal;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.accessories.crimsonIsle.NetherrackLookingSunshade;
import me.carscupcake.sbremake.item.impl.other.crimsonIsle.BlazeAshes;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.lootTable.ItemLoot;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.region.Region;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SmolderingBlaze extends SkyblockEntity implements SkillXpDropper {
    public SmolderingBlaze(Region guardingRegion) {
        super(EntityType.BLAZE, new LootTable<SbItemStack>().addLoot(new ItemLoot(Material.BLAZE_ROD, 2, 3, 1))
                .addLoot(new ItemLoot(BlazeAshes.class, 1, 0.2))
                .addLoot(new ItemLoot(NetherrackLookingSunshade.class, 1, 0.01)));
    }

    @Override
    public void spawn() {
        var group = new EntityAIGroup();
        group.getGoalSelectors().addAll(List.of(new BlazeGoal(this, 16)));
        group.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(this, 20),
                new ClosestEntityTarget(this, 20, entity1 -> entity1 instanceof Player p && !p.isDead() && p.getGameMode() == GameMode.SURVIVAL && !this.isDead)));

        addAIGroup(group);
    }

    @Override
    public double getDamage() {
        return 3_900;
    }

    @Override
    public float getMaxHealth() {
        return 2_000_000;
    }

    @Override
    public String getName() {
        return "Smoldering Blaze";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 320;
    }
}
