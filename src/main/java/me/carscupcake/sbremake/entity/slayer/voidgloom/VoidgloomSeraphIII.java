package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Line;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.item.ItemBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class VoidgloomSeraphIII extends VoidgloomSeraphII {
    public VoidgloomSeraphIII(SkyblockPlayer owner) {
        super(owner);
    }

    @Override
    public int getTier() {
        return 3;
    }

    @Override
    public double getDamage() {
        return 12_000;
    }

    @Override
    public float getMaxHealth() {
        return 50_000_000;
    }

    @Override
    public int getMaxHits() {
        return 60;
    }

    @Override
    protected void onHitPhase() {
        super.onHitPhase();
        if (getPhase() == 4) {
            scheduleHead();
        }
    }

    private final Set<LivingEntity> entities = new HashSet<>();

    private void scheduleHead() {
        var scheduler = new TaskScheduler() {
            @Override
            public void run() {
                if (entities.size() >= 6) return;
                var entity = new NukekubiFixation();
                entities.add(entity);
                entity.setInstance(instance, getPosition().add(0, 1, 0));
            }
        };
        assignTask(scheduler);
        scheduler.repeatTask(20*6, 20*6);
    }

    @Override
    public void remove(boolean force) {
        super.remove(force);
        entities.forEach(Entity::remove);
    }

    @Override
    protected double getDissonanceDamage() {
        var h = entities.size();
        return super.getDissonanceDamage() + (nukekubiFixationsDamage() * (h * Math.pow(2, h - 1)));
    }

    public class NukekubiFixation extends LivingEntity {
        private static final BoundingBox boundingBox = new BoundingBox(new Vec(-1, -1, -1), Vec.ONE);
        public NukekubiFixation() {
            super(EntityType.ARMOR_STAND);
            setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(
                    "eyJ0aW1lc3RhbXAiOjE1NjgwMzY3MjYwNjYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2VkYjBiMTNkZTYxMzkxMzBjYjVkMzUyM2U4YWNjNGZhZmNhMTBhMTIxNmE0OTUzNzhiYzllMWQyOThmZjlhZjkifX19").build());
            setNoGravity(true);
            setInvisible(true);
            setGlowing(true);
            var meta = (ArmorStandMeta) getEntityMeta();
            meta.setHasNoBasePlate(true);
            meta.setHasArms(false);
            setGlowColor(NamedTextColor.DARK_PURPLE);
        }

        private int ticks = 10;

        @Override
        public void spawn() {
            super.spawn();
            var dir = getPosition().withYaw(new Random().nextInt(361)).direction().normalize();
            var movement = new TaskScheduler() {
                double movement = 0.4;
                @Override
                public void run() {
                    movement -= 0.01;

                    if(movement <= 0){
                        cancel();
                        return;
                    }
                    var pos = getPosition().add(dir.mul(movement));
                    if (instance.getBlock(pos).isAir())
                        teleport(pos);
                    else
                        cancel();
                }
            };
            assignTask(movement);
            movement.repeatTask(1, 1);
            var task = new TaskScheduler() {
                @Override
                public void run() {
                    if (ticks <= 0) {
                        if (movement.isRunning())
                            movement.cancel();
                        NukekubiFixation.this.remove();
                        cancel();
                        return;
                    }
                    var line = new Line(owner.getPosition().add(0, owner.getEyeHeight(), 0).asVec(), owner.getPosition().direction());
                    if (!line.getCollidePoints(boundingBox.withOffset(getPosition()), true).isEmpty())
                        ticks--;
                    else ticks = 10;
                    teleport(getPosition().withDirection(owner.getPosition().sub(getPosition())));
                }
            };
            assignTask(task);
            task.repeatTask(1, 1);
        }

        @Override
        public void remove(boolean force) {
            super.remove(force);
            if (VoidgloomSeraphIII.this.isRemoved()) return;
            entities.remove(this);
            ParticleUtils.spawnParticle(owner.getInstance(), getPosition().add(0, getEyeHeight(), 0), Particle.SMOKE, 5);
        }
    }

    public int nukekubiFixationsDamage() {
        return 800;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 200;
    }
}
