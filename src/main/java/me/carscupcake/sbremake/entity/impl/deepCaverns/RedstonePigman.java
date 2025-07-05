package me.carscupcake.sbremake.entity.impl.deepCaverns;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.worlds.impl.DeepCaverns;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.time.TimeUnit;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RedstonePigman extends SkyblockEntity {
    public static final Set<Entity> attacked = new HashSet<>();

    public RedstonePigman() {
        super(EntityType.ZOMBIFIED_PIGLIN);
    }

    @Override
    public void spawn() {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(this, 16), new ClosestEntityTarget(this, 16, entity1 -> {
            if (!(entity1 instanceof SkyblockPlayer player)) return false;
            if (player.getGameMode() == GameMode.SURVIVAL || player.isDead()) return false;
            if (player.getRegion() != DeepCaverns.Region.PigmensDen) return false;
            if (attacked.contains(entity1)) return true;
            int i = 0;
            for (ItemStack itemStack : player.getInventory().getItemStacks())
                if (itemStack.material() == Material.REDSTONE) {
                    i += itemStack.amount();
                    if (i >= 64) return true;
                }
            return false;
        })));
        aiGroup.getGoalSelectors().addAll(List.of(new MeleeAttackGoal(this, 1.6, 20, TimeUnit.SERVER_TICK), new RandomStrollInRegion(this, 10, DeepCaverns.Region.PigmensDen, true) // Walk around
        ));
        addAIGroup(aiGroup);
    }

    @Override
    protected float onDamage(SkyblockPlayer player, float amount) {
        if (player != null)
            attacked.add(player);
        return super.onDamage(player, amount);
    }

    @Override
    public float getMaxHealth() {
        return 250;
    }

    @Override
    public String getName() {
        return "Redstone Pigman";
    }

    @Override
    public double getDamage() {
        return 75;
    }
}
