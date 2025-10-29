package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.enderman.NullAtom;
import me.carscupcake.sbremake.item.impl.other.slayer.enderman.TransmitionTuner;
import me.carscupcake.sbremake.item.impl.reforgeStone.HazmatEnderman;
import me.carscupcake.sbremake.item.impl.rune.Rune;
import me.carscupcake.sbremake.item.impl.rune.impl.EndersnakeRune;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.RuneModifier;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Line;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
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
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class VoidgloomSeraphIII extends VoidgloomSeraphII {
    public static final RngMeterEntry ENDERSNAKE_RUNE
            = new RngMeterEntry("ENDERSNAKE_RUNE",
                                new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE, new RuneModifier(new EndersnakeRune()
                                        , 1)), SlayerLootTable.LootTableType.Extra, Slayers.Enderman, 333),
                                new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE, new RuneModifier(new EndersnakeRune()
                                        , 1)), SlayerLootTable.LootTableType.Extra, Slayers.Enderman, 800));
    public static final RngMeterEntry MANA_STEAL
            = new RngMeterEntry("MANA_STEAL",
                                new RngMeterItemLoot(SbItemStack.base(Material.ENCHANTED_BOOK).withModifier(Modifier.ENCHANTMENTS,
                                                                                                            Map.of(NormalEnchantments.ManaSteal, 1)),
                                                     SlayerLootTable.LootTableType.Main
                                        , Slayers.Enderman, 600));
    public static final RngMeterEntry TRANSMITION_TUNER
            = new RngMeterEntry("TRANSMITION_TUNER",
                                new RngMeterItemLoot(TransmitionTuner.class,SlayerLootTable.LootTableType.Main, Slayers.Enderman, 300));
    public static final RngMeterEntry NULL_ATOM
            = new RngMeterEntry("NULL_ATOM",
                                new RngMeterItemLoot(NullAtom.class, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 500),
                                new RngMeterItemLoot(NullAtom.class, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 700));
    public static final RngMeterEntry HAZMAT_ENDERMAN
            = new RngMeterEntry("HAZMAT_ENDERMAN",
                                new RngMeterItemLoot(HazmatEnderman.class, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 140),
                                new RngMeterItemLoot(HazmatEnderman.class, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 220));

    public VoidgloomSeraphIII(SkyblockPlayer owner) {
        this(owner, new SlayerLootTable(), 3);
    }

    public VoidgloomSeraphIII(SkyblockPlayer owner, SlayerLootTable lootTable, int tier) {
        super(owner, getLootTable(lootTable, tier), tier);
    }

    public static SlayerLootTable getLootTable(SlayerLootTable lootTable, int tier) {
        return lootTable.addLoot(ENDERSNAKE_RUNE.loots()[tier - 3]).addLoot(MANA_STEAL.loots()[0]).addLoot(TRANSMITION_TUNER.loots()[0])
                .addLoot(NULL_ATOM.loots()[tier - 3]).addLoot(HAZMAT_ENDERMAN.loots()[tier - 3]);
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
