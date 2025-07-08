package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Laser;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class VoidgloomSeraphIV extends VoidgloomSeraphIII {
    public VoidgloomSeraphIV(SkyblockPlayer owner) {
        super(owner);
    }

    @Override
    public int getTier() {
        return 4;
    }

    @Override
    public double getDamage() {
        return isInRotationPhase ? 0 : 21_000;
    }

    @Override
    public float getMaxHealth() {
        return 210_000_000;
    }

    @Override
    public int getMaxHits() {
        return 100;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 500;
    }

    @Override
    protected float onDamage(SkyblockPlayer player, float amount) {
        if (imune) return 0;
        if (toNextRotationPhase(getHealth() - amount)) doHeartRotation();
        return super.onDamage(player, amount);
    }

    private boolean didPhase = false;

    @Override
    protected void onHitsLost() {
        super.onHitsLost();
        didPhase = false;
    }

    private boolean toNextRotationPhase(float health) {
        if (didPhase) return false;
        if (getPhase() == 1 && health <= 175000000) {
            return true;
        }
        if (getPhase() == 3 && health <= 105000000) {
            return true;
        }
        return getPhase() == 5 && health <= 35000000;
    }

    private boolean isInRotationPhase = false;
    private boolean imune = false;

    public void doHeartRotation() {
        didPhase = true;
        ParticleUtils.spawnParticle(instance, getPosition(), Particle.EXPLOSION, 1);
        isInRotationPhase = true;
        var pos = getPosition().withPitch(0);
        var stand = new LivingEntity(EntityType.ARMOR_STAND);
        stand.setInvisible(true);
        stand.setNoGravity(true);
        stand.setInstance(instance, pos.add(0, -2, 0));
        stand.addPassenger(this);

        var items = getLaserVecHashMap(pos);
        var task = new TaskScheduler() {
            int hitCooldown = 0;
            int i = 0;
            @Override
            public void run() {
                if (hitCooldown > 0) hitCooldown--;

                if (i == 20 * 7) {
                    cancel();
                    return;
                }

                if (hitCooldown == 0) {
                    var dist = owner.getDistance(getPosition().withY(owner.getPosition().y()));
                    if (dist <= 2.5 || dist >= 25) {
                        hitCooldown = 20;
                        owner.damage(0, owner.getStat(Stat.Health) * 0.1);
                    }
                }

                for (var entry : items.keySet()) {
                    var vec = entry.getSquid().getPosition().sub(entry.getGuardian().getPosition()).asVec();
                    entry.rotateAroundStartY(-0.025);
                    if (hitCooldown == 0) {
                        var vec2 = entry.getSquid().getPosition().sub(entry.getGuardian().getPosition()).asVec();
                        var playerVec = owner.getPosition().sub(entry.getGuardian().getPosition()).asVec();
                        var dot1 = playerVec.dot(vec);
                        var dot2 = playerVec.dot(vec2);
                        if (dot1 >= 0 && dot2 <= 0 || dot1 <= 0 && dot2 >= 0) {
                            hitCooldown = 20;
                            owner.damage(0, owner.getStat(Stat.Health) * 0.25);
                        }
                    }
                }
                if (i == 7) imune = true;
                i++;
            }

            @Override
            public synchronized void cancel() {
                super.cancel();
                isInRotationPhase = false;
                items.keySet().forEach(Laser::stop);
                imune = false;
                stand.removePassenger(VoidgloomSeraphIV.this);
                stand.remove();
            }
        };
        assignTask(task);
        task.repeatTask(1, 1);
    }

    private @NotNull HashMap<Laser, Vec> getLaserVecHashMap(Pos pos) {
        var l1 = new Laser(instance, pos, pos.add(10, 0, 0));
        var ve = new Vec(10, 0, 0);
        var items = new HashMap<Laser, Vec>();
        items.put(l1, ve);
        l1 = new Laser(instance, pos.add(0, 1.45, 0), pos.add(10, 1.45, 0));
        items.put(l1, ve);
        l1 = new Laser(instance, pos.add(0, getEyeHeight(), 0), pos.add(10, getEyeHeight(), 0));
        items.put(l1, ve);
        var clone = new HashMap<>(items);
        for (Laser l : clone.keySet()) {
            var v = new Vec(-10, 0, 0);
            var start = l.getGuardian().getPosition();
            Laser nL = new Laser(instance, start, start.add(v));
            items.put(nL, v);

            v = new Vec(0, 0, 10);
            nL = new Laser(instance, start, start.add(v));
            items.put(nL, v);

            v = new Vec(0, 0, -10);
            nL = new Laser(instance, start, start.add(v));
            items.put(nL, v);
        }
        return items;
    }

    @Override
    public int nukekubiFixationsDamage() {
        return 2_000;
    }
}
