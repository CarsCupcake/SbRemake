package me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.base;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.event.PlayerDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.Listener;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorAbilityCounter;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.KuudraArmorTier;
import me.carscupcake.sbremake.item.impl.armor.crimsonIsle.crimson.CrimsonBootsBaseline;
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
    public KuudraArmorTier armorTier() {
        return KuudraArmorTier.Base;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of(Stat.Health, 130, Stat.Defense, 40, Stat.Strength, 30, Stat.Intelligence, 5, Stat.CritDamage, 20);
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("ability.dominus").addListener(PlayerStatEvent.class, event -> {
                    if (event.stat() == Stat.SwingRange || event.stat() == Stat.Ferocity || event.stat() == Stat.Damage) {
                        KuudraArmorAbilityCounter counter = DominusAbility.task.get(event.player());
                        if (counter != null) {
                            DominusAbility.applyStatBonus(event.player(), counter.getTier(), event);
                        }
                    }
                })
                .addListener(PlayerMeleeDamageEntityEvent.class, event -> {
                    int pieces = event.getPlayer().getFullSetBonusPieceAmount(DominusAbility.INSTANCE);
                    if (pieces < 2) return;
                    SkyblockPlayer player = event.getPlayer();
                    var ability = DominusAbility.task.get(player);
                    ability.add();
                    var counter = DominusAbility.task.get(player);
                    if (counter.getStacks() == 10) {
                        player.playSound(SoundType.BLOCK_PISTON_EXTEND, Sound.Source.PLAYER, 1, 1);
                        if (counter.cooldown == 0) {
                            counter.cooldown = 10;
                            Vec v = new Vec(3, 0, 0).rotateAroundY(Math.toRadians(new Random().nextInt(360)));
                            Vec supportVec = event.getTarget().getPosition().asVec().add(v);
                            Vec dir = v.mul(-1).mul(2).withY(2);
                            Set<Entity> es = EntityUtils.getEntitiesInLine(supportVec.asPosition(), supportVec.add(dir).asPosition(), player.getInstance(), 2);
                            double swipeStrength = switch (ability.getTier()) {
                                case Base -> switch (pieces) {
                                    case 3 -> 1;
                                    case 4 -> 1.5;
                                    default -> 0.5;
                                };
                                case Hot -> switch (pieces) {
                                    case 3 -> 1.25;
                                    case 4 -> 1.875;
                                    default -> 0.625;
                                };
                                case Burning -> switch (pieces) {
                                    case 3 -> 1.5;
                                    case 4 -> 2.25;
                                    default -> 0.75;
                                };
                                case Fiery -> switch (pieces) {
                                    case 3 -> 1.75;
                                    case 4 -> 2.625;
                                    default -> 0.875;
                                };
                                case Infernal -> switch (pieces) {
                                    case 3 -> 2;
                                    case 4 -> 3;
                                    default -> 1;
                                };
                            };
                            MinecraftServer.getSchedulerManager().buildTask(() -> es.forEach(e -> {
                                if (!(e instanceof SkyblockEntity entity)) return;
                                double damage = event.isCrit() ? event.calculateCritHit() : event.calculateHit();
                                double swipeDamage = damage * (swipeStrength / event.getAdditiveMultiplier());
                                PlayerDamageEntityEvent entityEvent = new PlayerDamageEntityEvent(player, event.getTarget(), swipeDamage);
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
