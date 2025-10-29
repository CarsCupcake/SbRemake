package me.carscupcake.sbremake.entity.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.SlayerEntity;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.FoulFlesh;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.RevenantCatalyst;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.UndeadCatalyst;
import me.carscupcake.sbremake.item.impl.rune.Rune;
import me.carscupcake.sbremake.item.impl.rune.impl.PestilenceRune;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.RuneModifier;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.List;

import static me.carscupcake.sbremake.entity.slayer.zombie.RevenantHorrorI.REVENANT_FLESH;

public class RevenantHorrorII extends SlayerEntity implements SkillXpDropper {
    public static final SlayerLootTable lootTable;
    public static final RngMeterEntry FOUL_FLESH = new RngMeterEntry("FOUL_FLESH", new RngMeterItemLoot(SbItemStack.from(FoulFlesh.class), 1, SlayerLootTable.LootTableType.Main, Slayers.Zombie, 2_000), new RngMeterItemLoot(SbItemStack.from(FoulFlesh.class), 1, 2, SlayerLootTable.LootTableType.Main, Slayers.Zombie, 2_000), new RngMeterItemLoot(SbItemStack.from(FoulFlesh.class), 2, 3, SlayerLootTable.LootTableType.Main, Slayers.Zombie, 2_000), new RngMeterItemLoot(SbItemStack.from(FoulFlesh.class), 3, 4, SlayerLootTable.LootTableType.Main, Slayers.Zombie, 2_000));
    public static final RngMeterEntry PESTILENCE_RUNE = new RngMeterEntry("PESTILENCE_RUNE", new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE, new RuneModifier(new PestilenceRune(), 1)), SlayerLootTable.LootTableType.Extra, Slayers.Zombie, 83), new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE, new RuneModifier(new PestilenceRune(), 1)), SlayerLootTable.LootTableType.Extra, Slayers.Zombie, 333), new RngMeterItemLoot(SbItemStack.from(Rune.class).withModifier(Modifier.RUNE, new RuneModifier(new PestilenceRune(), 1)), SlayerLootTable.LootTableType.Extra, Slayers.Zombie, 833));
    public static final RngMeterEntry UNDEAD_CATALYST = new RngMeterEntry("UNDEAD_CATALYST", new RngMeterItemLoot(SbItemStack.from(UndeadCatalyst.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 250), new RngMeterItemLoot(SbItemStack.from(UndeadCatalyst.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 75));
    public static final RngMeterEntry REVENANT_CATALYST = new RngMeterEntry("REVENANT_CATALYST", new RngMeterItemLoot(SbItemStack.from(RevenantCatalyst.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 125));

    static {
        lootTable = new SlayerLootTable();
        lootTable.addLoot(REVENANT_FLESH.loots()[1]);
        lootTable.addLoot(FOUL_FLESH.loots()[0]);
        lootTable.addLoot(PESTILENCE_RUNE.loots()[0]);
        lootTable.addLoot(UNDEAD_CATALYST.loots()[0]);
        lootTable.addLoot(REVENANT_CATALYST.loots()[0]);
    }

    public RevenantHorrorII(SkyblockPlayer owner) {
        super(EntityType.ZOMBIE, lootTable, owner);
        setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0aW1lc3RhbXAiOjE1NjgwMzY3MjYwNjYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFmYzAxODQ0NzNmZTg4MmQyODk1Y2U3Y2JjODE5N2JkNDBmZjcwYmYxMGQzNzQ1ZGU5N2I2YzJhOWM1ZmM3OGYifX19").build());
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setEquipment(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setGlint(true).build());
        setEquipment(EquipmentSlot.BOOTS, new ItemBuilder(Material.DIAMOND_BOOTS).build());
        setEquipment(EquipmentSlot.MAIN_HAND, new ItemBuilder(Material.DIAMOND_HOE).setGlint(true).build());
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.368);
        /*8.5^2*/
        TaskScheduler aoeTask = new TaskScheduler() {
            @Override
            public void run() {
                if (isDead || isRemoved()) {
                    cancel();
                    return;
                }
                if (owner.getPosition().distanceSquared(getPosition()) <= 72.25 /*8.5^2*/) {
                    owner.getTemporaryModifiers().add(Stat.Defense,
                            new PlayerStatEvent.BasicModifier("Revenant Horror", 0.75, PlayerStatEvent.Type.MultiplicativeMultiplier,
                                    PlayerStatEvent.StatsCategory.Ability), Duration.ofSeconds(1));
                    owner.damage(RevenantHorrorII.this, false);
                }
            }
        };
        aoeTask.repeatTask(20, 20);
    }

    @Override
    public void spawn() {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().addAll(List.of(new RevenantAttackGoal(this, 1.3, 3.2, Duration.ofMillis(3200), Duration.ofSeconds(1)), new RandomStrollGoal(this, 5) // Walk around
        ));
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(this, 35), new ClosestEntityTarget(this, 128, entity1 -> entity1 instanceof Player p && !p.isDead() && p.getGameMode() == GameMode.SURVIVAL && p == owner)));
        addAIGroup(aiGroup);
    }

    @Override
    public float getMaxHealth() {
        return 20_000;
    }

    @Override
    public double getDamage() {
        return 15;
    }

    @Override
    public String getName() {
        return "Revenant Horror";
    }

    @Override
    public Skill type() {
        return Skill.Combat;
    }

    @Override
    public double amount(SkyblockPlayer target) {
        return 100;
    }

    @Override
    public int getTier() {
        return 2;
    }

    @Override
    public ISlayer getSlayer() {
        return Slayers.Zombie;
    }

    private long lastAttack = System.currentTimeMillis();

    public void attack(@NotNull Entity target, boolean swingHand) {
        super.attack(target, swingHand);
        if (target == owner && System.currentTimeMillis() - lastAttack >= 2_500) {
            double newHealth = getHealth() + owner.calculateDamage(getDamage(), 0);
            setHealth(newHealth > getMaxHealth() ? getMaxHealth() : (float) newHealth);
            lastAttack = System.currentTimeMillis();
            ParticleUtils.spawnParticle(getInstance(), this.getPosition().add(0, getEyeHeight() / 2, 0), Particle.HAPPY_VILLAGER, 25, new Vec(0.5, getEyeHeight() / 2, 0.5), 0);
        }
    }

}
