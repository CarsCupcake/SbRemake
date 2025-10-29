package me.carscupcake.sbremake.blocks;

import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.server.play.ParticlePacket;
import net.minestom.server.particle.Particle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public record FarmingCrystal(Pos location, TaskScheduler task, HashMap<BlockVec, Block> blocks) {
    public FarmingCrystal(String headTexture, Pos location, Instance instance) {
        this(location, new CrystalTask(headTexture, location, instance), new HashMap<>());
        CrystalTask crystalTask = (CrystalTask) task;
        crystalTask.farmingCrystal = this;
        crystalTask.repeatTask(1);

    }

    public static class CrystalTask extends TaskScheduler {
        private final ParticlePacket packet;
        private final LivingEntity entity;
        private final Pos base;
        private int i = new Random().nextInt(40);
        private FarmingCrystal farmingCrystal;
        private final Instance instance;

        public CrystalTask(String headTexture, Pos pos, Instance instance) {
            this.instance = instance;
            this.base = pos;
            LivingEntity e = new LivingEntity(EntityType.ARMOR_STAND);
            e.setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture(headTexture).build());
            e.setInvisible(true);
            e.setNoGravity(true);
            e.setInstance(instance, pos);
            entity = e;
            packet = new ParticlePacket(Particle.FIREWORK, base.add(0, 2, 0), new Pos(0.5, 0.5, 0.5), 0f, 1);
        }

        private static final int RENDER_DISTANCE = (8 * 16) * (8 * 16);

        @Override
        public void run() {
            if (entity.getInstance() == null) {
                //Instance was probably shutdown
                cancel();
                return;
            }
            if (entity.getChunk() != null && entity.getChunk().isLoaded()) {
                entity.teleport(base.add(0, 0.5 * Math.sin(Math.PI * ((double) i / 20d)), 0).withYaw(i * 18));
                Audiences.players(player -> player.getInstance() == instance && player.getPosition().distanceSquared(base) < RENDER_DISTANCE).forEachAudience(audience -> {
                    ((SkyblockPlayer) audience).sendPacket(packet);
                });
            }
            if (i == 40) {
                i = 0;
                Optional<Map.Entry<BlockVec, Block>> optional = farmingCrystal.blocks.entrySet().stream().findFirst();
                if (optional.isPresent()) {
                    Map.Entry<BlockVec, Block> entry = optional.get();
                    instance.setBlock(entry.getKey(), entry.getValue());
                    Vec dis = entry.getKey().asVec().add(0.5).sub(base.add(0, 2, 0));
                    Vec dir = dis.normalize().mul(1);
                    int iterations = (int) (dis.length());
                    Pos p = base.add(0, 2, 0);
                    for (int i = 0; i < iterations; i++) {
                        p = p.add(dir);
                        ParticleUtils.spawnParticle(instance, p, Particle.FIREWORK, 1);
                    }
                    farmingCrystal.blocks.remove(entry.getKey());
                }
            } else
                i++;
        }
    }
}
