package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.accessories.slayer.enderman.HandyBloodChalice;
import me.carscupcake.sbremake.item.impl.accessories.slayer.enderman.PocketEspressoMachine;
import me.carscupcake.sbremake.item.impl.other.slayer.enderman.EtherwarpMerger;
import me.carscupcake.sbremake.item.impl.other.slayer.enderman.ExceedinglyRareEnderArtifactUpgrade;
import me.carscupcake.sbremake.item.impl.other.slayer.enderman.JudgementCore;
import me.carscupcake.sbremake.item.impl.other.slayer.enderman.SinfulDice;
import me.carscupcake.sbremake.item.impl.rune.Rune;
import me.carscupcake.sbremake.item.impl.rune.impl.EndRune;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.RuneModifier;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Laser;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class VoidgloomSeraphIV extends VoidgloomSeraphIII {
    public static final RngMeterEntry POCKET_ESPRESSO_MACHINE = new RngMeterEntry("POCKET_ESPRESSO_MACHINE",
                                                                                   new RngMeterItemLoot(PocketEspressoMachine.class,
                                                                                                        SlayerLootTable.LootTableType.Main,
                                                                                                        Slayers.Enderman,
                                                                                                        55));
    public static final RngMeterEntry SMARTY_PANTS
            = new RngMeterEntry("SMARTY_PANTS",
                                new RngMeterItemLoot(SbItemStack.base(Material.ENCHANTED_BOOK).withModifier(Modifier.ENCHANTMENTS,
                                                                                                            Map.of(NormalEnchantments.SmartyPants, 1)),
                                                     SlayerLootTable.LootTableType.Main
                                        , Slayers.Enderman, 250));
    public static final RngMeterEntry END_RUNE
            = new RngMeterEntry("END_RUNE",
                                new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE,
                                                                                               new RuneModifier(new EndRune(), 1)),
                                                     SlayerLootTable.LootTableType.Extra
                                        , Slayers.Enderman, 100));
    public static final RngMeterEntry HANDY_BLOOD_CHALICE = new RngMeterEntry("HANDY_BLOOD_CHALICE",
                                                                               new RngMeterItemLoot(HandyBloodChalice.class,
                                                                                                    SlayerLootTable.LootTableType.Main,
                                                                                                    Slayers.Enderman,
                                                                                                    25));
    public static final RngMeterEntry SINFUL_DICE = new RngMeterEntry("SINFUL_DICE",
                                                                       new RngMeterItemLoot(SinfulDice.class,
                                                                                            SlayerLootTable.LootTableType.Main,
                                                                                            Slayers.Enderman,
                                                                                            65));
    public static final RngMeterEntry UPGRADER = new RngMeterEntry("UPGRADER",
                                                                    new RngMeterItemLoot(ExceedinglyRareEnderArtifactUpgrade.class,
                                                                                         SlayerLootTable.LootTableType.Main,
                                                                                         Slayers.Enderman,
                                                                                         4));
    public static final RngMeterEntry ETHERWARP_MERGER = new RngMeterEntry("ETHERWARP_MERGER",
                                                                            new RngMeterItemLoot(EtherwarpMerger.class,
                                                                                                 SlayerLootTable.LootTableType.Main,
                                                                                                 Slayers.Enderman,
                                                                                                 60));
    public static final RngMeterEntry JUDGEMENT_CORE = new RngMeterEntry("JUDGEMENT_CORE",
                                                                            new RngMeterItemLoot(JudgementCore.class,
                                                                                                 SlayerLootTable.LootTableType.Main,
                                                                                                 Slayers.Enderman,
                                                                                                 8));
    public static final RngMeterEntry ENCHANT_RUNE
            = new RngMeterEntry("ENCHANT_RUNE",
                                new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE,
                                                                                               new RuneModifier(new EndRune(), 1)),
                                                     SlayerLootTable.LootTableType.Extra
                                        , Slayers.Enderman, 7));
    public static final RngMeterEntry ENDER_SLAYER
            = new RngMeterEntry("ENDER_SLAYER",
                                new RngMeterItemLoot(SbItemStack.base(Material.ENCHANTED_BOOK).withModifier(Modifier.ENCHANTMENTS,
                                                                                                            Map.of(NormalEnchantments.EnderSlayer,
                                                                                                                   7)),
                                                     SlayerLootTable.LootTableType.Main
                                        , Slayers.Enderman, 2));
    public static final RngMeterEntry VOID_CONQUEROR_ENDERMAN_SKIN
            = new RngMeterEntry("VOID_CONQUEROR_ENDERMAN_SKIN",
                                new RngMeterItemLoot(SbItemStack.base(Material.PLAYER_HEAD),
                                                     SlayerLootTable.LootTableType.Extra
                                        , Slayers.Enderman, 25));
    public VoidgloomSeraphIV(SkyblockPlayer owner) {
        super(owner, getSlayerLootTable(), 4);
    }

    public static SlayerLootTable getSlayerLootTable() {
        return new SlayerLootTable().addLoot(POCKET_ESPRESSO_MACHINE.loots()[0]).addLoot(SMARTY_PANTS.loots()[0])
            .addLoot(END_RUNE.loots()[0]).addLoot(HANDY_BLOOD_CHALICE.loots()[0]).addLoot(SINFUL_DICE.loots()[0]).addLoot(UPGRADER.loots()[0])
            .addLoot(ETHERWARP_MERGER.loots()[0]).addLoot(JUDGEMENT_CORE.loots()[0]).addLoot(ENCHANT_RUNE.loots()[0]).addLoot(ENDER_SLAYER.loots()[0])
            .addLoot(VOID_CONQUEROR_ENDERMAN_SKIN.loots()[0]);
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
        var max = getPosition().y() + getEyeHeight() + 0.1;

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
                    if (dist <= 2.5 || dist >= 10.5) {
                        hitCooldown = 20;
                        owner.damage(VoidgloomSeraphIV.this, 0, owner.getStat(Stat.Health) * 0.1);
                    }
                }

                for (var entry : items.keySet()) {
                    var vec = entry.getSquid().getPosition().sub(entry.getGuardian().getPosition()).asVec();
                    entry.rotateAroundStartY(-0.025);
                    if (hitCooldown == 0) {
                        if (owner.getPosition().y() <= max) {
                            var vec2 = entry.getSquid().getPosition().sub(entry.getGuardian().getPosition()).asVec();
                            var playerVec = owner.getPosition().sub(entry.getGuardian().getPosition()).asVec();
                            var dot1 = playerVec.dot(vec);
                            var dot2 = playerVec.dot(vec2);
                            if (dot1 >= 0 && dot2 <= 0 || dot1 <= 0 && dot2 >= 0) {
                                hitCooldown = 20;
                                owner.damage(VoidgloomSeraphIV.this, 0, owner.getStat(Stat.Health) * 0.25);
                            }
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
