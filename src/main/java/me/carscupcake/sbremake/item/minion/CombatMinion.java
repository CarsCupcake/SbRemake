package me.carscupcake.sbremake.item.minion;


import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CombatMinion extends AbstractMinion {
    private static final int maxEntityAmount = 6;
    private final AbstractCombatMinionData minion;

    /**
     * This constructor provides the basic values
     *
     * @param level            is the level of the minion
     * @param base             is the base item of the minion
     * @param instance         the instance
     * @param location         is the location where the minion is placed
     * @param minionIdentifier is a string for the minion. This is a random UUID from the method {@link UUID#randomUUID()} and is also used to load the minion from the file
     * @param placer           is the player who owns the isle
     */
    public CombatMinion(int level, AbstractCombatMinionData base, Instance instance, Pos location, String minionIdentifier, UUID placer) {
        super(level, base, instance, location, minionIdentifier, placer);
        minion = base;
    }

    @Override
    void startGetAnimation() {
        List<Entity> missing =
                instance.getNearbyEntities(location, 5).stream().filter(entity -> entity instanceof MinionEntity minionEntity
                && minionEntity.minionId().equals(minionId)).collect(Collectors.toList());
        if (missing.isEmpty())
            return;

        Collections.shuffle(missing);
        Entity target = missing.getFirst();
        missing.remove(target);
        if(!(target instanceof LivingEntity entity))
            return;

        var lo =
                stand.getPosition().withDirection(target.getPosition().add(0, entity.getEyeHeight()/2,0).sub(stand.getPosition()));
        var angle = new Vec(Math.toRadians(lo.pitch()), 0, 0);
        stand.teleport(lo);
        var meta = (ArmorStandMeta) stand.getEntityMeta();
        meta.setHeadRotation(angle);
        entity.kill();
        generateLoot();
        new TaskScheduler() {
            double rotation = -90;
            int i = 0;

            @Override
            public void run() {
                if (!isRunning) {
                    cancel();
                    return;
                }
                if (rotation >= 0) {
                    cancel();

                    resetPos();
                    return;
                }

                rotation += 90d / 4d;
                if (rotation >= 0)
                    rotation = 0;
                var meta = (ArmorStandMeta) stand.getEntityMeta();
                meta.setRightArmRotation(new Vec(Math.toRadians(rotation), 0, 0));

                i++;
            }
        }.repeatTask(10, 2);
    }

    @Override
    void startGenerateAnimation() {
        List<BlockVec> missing = getPos();
        var target = lookAtTarget(missing);

        new TaskScheduler() {
            double rotation = -90;
            int i = 0;

            @Override
            public void run() {
                if (!isRunning) {
                    cancel();
                    return;
                }

                if (rotation >= 0) {
                    cancel();
                    SkyblockEntity entity = minion.getEntity().makeNew();
                    entity.setInstance(instance, target.add(0, 2, 0)).join();
                    ((MinionEntity) entity).setMinionId(minionId);
                    resetPos();
                    return;
                }

                rotation += 90d / 4d;

                var meta = (ArmorStandMeta) stand.getEntityMeta();
                meta.setRightArmRotation(new Vec(Math.toRadians(rotation), 0, 0));
                i++;
            }
        }.repeatTask(10, 2);
        isMaxGenerated();
    }

    @Override
    boolean isMaxGenerated() {
        return settableSpace() <= 0;
    }

    @Override
    int settableSpace() {
        int i =
                instance.getNearbyEntities(location, 5).stream().filter(entity -> entity  instanceof MinionEntity minionEntity
                        && minionEntity.minionId().equals(minionId)).toList().size();
        if (i > maxEntityAmount) return 0;
        else return maxEntityAmount - i;
    }

    private List<BlockVec> getPos() {
        List<BlockVec> blocks = new ArrayList<>();
        for (var block : getBlockGrit()) {
            for (var y = -1 ; y <= 1; y++) {
                var b = getNextIfPossible(block);
                if (b == null)
                    continue;
                blocks.add(b);
            }
        }

        Collections.shuffle(blocks);
        return blocks;
    }

    private BlockVec getNextIfPossible(BlockVec b) {
        var same = instance.getBlock(b);
        var up = instance.getBlock(b.add(0, 1, 0));
        var upup = instance.getBlock(b.add(0, 2, 0));
        if (same.isSolid() && up == Block.AIR && upup == Block.AIR)
            return b.sub(0, 1, 0);
        if (instance.ensureGetBlockAsync(b.add(0, -1, 0)).join().isSolid()
                && !same.isSolid()
                && !up.isSolid())
            return b;
        return null;
    }
}
