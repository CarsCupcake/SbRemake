package me.carscupcake.sbremake.item.impl.wand;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.eventBinding.DamagingPlayerEventBinding;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.ability.*;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.metadata.other.FallingBlockMeta;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;

import java.time.Duration;
import java.util.*;

public class GyrokineticWand implements ISbItem, Listener {
    public static final Map<AlignmentListWrapper, Double> ALIGNMENT_DAMAGE_BUFFER = new HashMap<>();
    private final static Block[] BLOCK_TYPES = {Block.PURPLE_STAINED_GLASS, Block.OBSIDIAN};
    private final List<Ability> abilities = List.of(
            new ItemAbility<>("Gravity Storm", AbilityType.LEFT_CLICK, event -> {
                System.out.println("Start");
                var middle = event.block() == null ? event.getPlayer().getPosition().add(0, -0.5, 0) : event.block().add(0.5, 0.5, 0.5);
                var pulling = Vec.fromPoint(middle.add(0, 1, 0));
                var instance = event.getPlayer().getInstance();
                new TaskScheduler() {
                    private final Random random = new Random();
                    private int ticks = 20;
                    private int tot = 0;

                    @Override
                    public void run() {
                        if (tot % 2 == 0) {
                            instance.playSound(SoundType.ENTITY_ENDERMAN_TELEPORT.create(1, 1.5f - ((float) ticks / 20)), middle);
                            var unit = new Vec((double) ticks / 2, 0, 0);
                            for (int i = 0; i < 10; i++) {
                                var pos = unit.rotateAroundY(random.nextDouble() * 360);
                                var entity = new Entity(EntityType.FALLING_BLOCK) {
                                    @Override
                                    public void tick(long time) {
                                        super.tick(time);
                                        if (isOnGround())
                                            remove();
                                    }
                                };
                                var meta = (FallingBlockMeta) entity.getEntityMeta();
                                meta.setBlock(BLOCK_TYPES[random.nextInt(BLOCK_TYPES.length)]);
                                entity.setInstance(instance, middle.add(pos));
                                entity.setVelocity(new Vec((random.nextDouble() - 0.5) * 10, 1, (random.nextDouble() - 0.5) * 10));
                                entity.setNoGravity(false);
                                entity.scheduleRemove(Duration.ofSeconds(2));
                            }

                            if (ticks == 0) {
                                cancel();
                                return;
                            }
                            ticks--;
                        }
                        for (int i = 0; i < 360; i += 10) {
                            var pos = new Vec((double) ticks / 2, 1, 0).rotateAroundY(i + (tot % 2 == 0 ? 5 : 0));
                            ParticleUtils.spawnParticle(instance, middle.add(pos), Particle.WITCH, 1);
                        }
                        for (var entity : instance.getNearbyEntities(pulling, 10)) {
                            if (!(entity instanceof SkyblockEntity)) continue;
                            var dir = pulling.sub(entity.getPosition());
                            if (dir.length() < 1)
                                entity.teleport(pulling.asPosition());
                            else {
                                dir = dir.normalize().mul(1);
                                entity.teleport(entity.getPosition().add(dir));
                            }
                        }
                        tot++;
                    }
                }.repeatTask(0, 1);
            }, new Lore("§7Create a large §5rift §7at the aimed location, pulling all mobs together.\n§8Regen mana 10x slower for 3s after cast."),
                    new CooldownRequirement<>(30), new ManaRequirement<>(1200), new SoulflowRequirement<>(10)),
            new ItemAbility<>("Cells Alignment", AbilityType.RIGHT_CLICK, event -> {
                var other = event.player().getInstance().getPlayers().stream().filter(player -> player.getPosition().distanceSquared(event.player().getPosition()) <= 400).limit(4).toList();
                var all = new HashSet<SkyblockPlayer>();
                all.add(event.player());
                for (var player : other) {
                    all.add((SkyblockPlayer) player);
                }
                var wrapper = new AlignmentListWrapper(all);
                ALIGNMENT_DAMAGE_BUFFER.put(wrapper, 0d);
                new TaskScheduler() {

                    @Override
                    public void run() {
                        var dmg = (ALIGNMENT_DAMAGE_BUFFER.remove(wrapper) / all.size()) / 3;
                        if (dmg < 1) return;
                        new TaskScheduler() {
                            int i = 0;

                            @Override
                            public void run() {
                                for (var player : all) {
                                    player.damage(dmg, 0);
                                }
                                i++;
                                if (i == 3) {
                                    cancel();
                                }
                            }
                        }.repeatTask(0, 20);

                    }
                }.delayTask(6 * 20);
            }, new Lore("""
                    §7Apply §aAligned §7to 4 nearby player and yourself for §a6s§7.
                     \s
                    §a||| Aligned
                    §7Splits incoming damage and apply it over §a3s§7.
                    """), new SoulflowRequirement<>(2), new ManaRequirement<>(220), new CooldownRequirement<>(10))
    );
    private final List<Requirement> requirements = List.of(new SlayerRequirement(Slayers.Enderman, 6));

    @Override
    public String getId() {
        return "GYROKINETIC_WAND";
    }

    @Override
    public String getName() {
        return "Gyrokinetic Wand";
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Wand;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.EPIC;
    }

    @Override
    public ItemStackModifiers modifierBuilder() {
        return ItemStackModifiers.ENCHANT_GLINT_MODIFIERS;
    }

    @Override
    public boolean isUnstackable() {
        return true;
    }

    @Override
    public List<Ability> getDefaultAbilities() {
        return abilities;
    }

    @Override
    public List<Requirement> requirements() {
        return requirements;
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("gyrokinetic_wand_ability").register(new DamagingPlayerEventBinding(event -> {
            for (var entry : ALIGNMENT_DAMAGE_BUFFER.keySet()) {
                if (entry.players.contains((SkyblockPlayer) event.getTarget())) {
                    event.onDamageFinalize(aDouble -> {
                        ALIGNMENT_DAMAGE_BUFFER.put(entry, ALIGNMENT_DAMAGE_BUFFER.get(entry) + aDouble);
                    });
                }
            }
        }));
    }

    public static class AlignmentListWrapper {
        public Set<SkyblockPlayer> players;

        public AlignmentListWrapper(Set<SkyblockPlayer> players) {
            this.players = players;
        }
    }
}
