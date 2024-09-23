package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.event.EntityDeathEvent;
import me.carscupcake.sbremake.event.PlayerDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Listener;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonBootsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonLeggingsBaseline;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.DominusAbility;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.EntityUtils;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.SoundType;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.particle.Particle;
import net.minestom.server.utils.time.TimeUnit;

import java.util.Map;
import java.util.Random;
import java.util.Set;

public class CrimsonBoots extends CrimsonBootsBaseline implements Listener {
    @Override
    protected KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 130, Stat.Defense, 40, Stat.Strength, 30, Stat.Intelligence, 5, Stat.CritDamage, 20);
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("ability.dominus").addListener(PlayerStatEvent.class, event -> {
            if (event.stat() == Stat.SwingRange) {
                int stacks = DominusAbility.DOMINUS_STACKS.getOrDefault(event.player(), 0);
                if (stacks > 0)
                    event.modifiers().add(new PlayerStatEvent.BasicModifier("Dominus", stacks * 0.1d, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Ability));
            }
        })
                .addListener(EntityDeathEvent.class, event -> {
                    if (event.entity().getLastDamager() != null && event.type() == EntityDeathEvent.Type.Melee) {
                        SkyblockPlayer player = event.entity().getLastDamager();
                        if (player.getFullSetBonusPieceAmount(DominusAbility.INSTANCE) < 2) return;
                        if (DominusAbility.DOMINUS_STACKS.get(player) < 10) DominusAbility.DOMINUS_STACKS.add(player, 1);
                        DominusAbility.task.get(player).resetTime();
                    }
                })
                .addListener(PlayerMeleeDamageEntityEvent.class, event -> {
                    if (event.getPlayer().getFullSetBonusPieceAmount(DominusAbility.INSTANCE) < 2) return;
                    SkyblockPlayer player = event.getPlayer();
                    if (DominusAbility.DOMINUS_STACKS.get(player) == 10) {
                        player.playSound(SoundType.BLOCK_PISTON_EXTEND, Sound.Source.PLAYER, 1, 1);
                        if (DominusAbility.task.get(player).cooldown == 0) {
                            DominusAbility.task.get(player).cooldown = 10;
                            Vec v = new Vec(3, 0 ,0).rotateAroundY(Math.toRadians(new Random().nextInt(360)));
                            Vec supportVec = event.getTarget().getPosition().asVec().add(v);
                            Vec dir = v.mul(-1).mul(2).withY(2);
                            Set<Entity> es = EntityUtils.getEntitiesInLine(supportVec.asPosition(), supportVec.add(dir).asPosition(), player.getInstance(), 2);
                            MinecraftServer.getSchedulerManager().buildTask(() -> es.forEach(e -> {
                                if (!(e instanceof SkyblockEntity entity)) return;
                                PlayerDamageEntityEvent entityEvent = new PlayerDamageEntityEvent(player, event.getTarget(), event.calculateCritHit());
                                entityEvent.setMultiplicativeMultiplier(0.4);
                                entityEvent.setCanDoFerocity(false);
                                MinecraftServer.getGlobalEventHandler().call(entityEvent);
                                entity.damage(entityEvent);
                            })).delay(1, TimeUnit.SERVER_TICK).schedule();
                            dir = dir.mul(0.05);
                            for (int i = 0; i < 20; i++) {
                                ParticleUtils.spawnParticle(player.getInstance(), supportVec, Particle.FLAME, 1, new Vec(0), 0);
                                supportVec = supportVec.add(dir);
                            }
                        }
                    }
                });
    }
}
