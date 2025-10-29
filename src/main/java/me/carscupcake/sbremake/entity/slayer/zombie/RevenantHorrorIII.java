package me.carscupcake.sbremake.entity.slayer.zombie;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.SlayerEntity;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.BeheadedHorror;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
import net.minestom.server.color.Color;
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
import java.util.Map;

import static me.carscupcake.sbremake.entity.slayer.zombie.RevenantHorrorI.REVENANT_FLESH;

@Getter
public class RevenantHorrorIII extends SlayerEntity implements SkillXpDropper {
    public static final RngMeterEntry BEHEADED_HORROR = new RngMeterEntry("BEHEADED_HORROR", new RngMeterItemLoot(BeheadedHorror.class, SlayerLootTable.LootTableType.Main, Slayers.Zombie, 10), new RngMeterItemLoot(BeheadedHorror.class, SlayerLootTable.LootTableType.Main, Slayers.Zombie, 20));
    public static final RngMeterEntry SMITE_VI = new RngMeterEntry("SMITE_VI",
            new RngMeterItemLoot(SbItemStack.base(Material.ENCHANTED_BOOK).withModifier(Modifier.ENCHANTMENTS, Map.of(NormalEnchantments.Smite, 6)), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 50),
            new RngMeterItemLoot(SbItemStack.base(Material.ENCHANTED_BOOK).withModifier(Modifier.ENCHANTMENTS, Map.of(NormalEnchantments.Smite, 6)), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 100));
    public static final SlayerLootTable lootTable;

    static {
        lootTable = new SlayerLootTable();
        lootTable.addLoot(REVENANT_FLESH.loots()[2]);
        lootTable.addLoot(RevenantHorrorII.FOUL_FLESH.loots()[1]);
        lootTable.addLoot(RevenantHorrorII.PESTILENCE_RUNE.loots()[1]);
        lootTable.addLoot(RevenantHorrorII.UNDEAD_CATALYST.loots()[0]);
        lootTable.addLoot(RevenantHorrorII.REVENANT_CATALYST.loots()[0]);
        lootTable.addLoot(RevenantHorrorIII.BEHEADED_HORROR.loots()[0]);
        lootTable.addLoot(RevenantHorrorIII.SMITE_VI.loots()[0]);
    }

    private boolean enraged = false;

    public RevenantHorrorIII(SkyblockPlayer owner) {
        this(owner, lootTable);
    }

    public RevenantHorrorIII(SkyblockPlayer owner, SlayerLootTable lootTable) {
        super(EntityType.ZOMBIE, lootTable, owner);
        setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0aW1lc3RhbXAiOjE1NjgwMzY3MjYwNjYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFmYzAxODQ0NzNmZTg4MmQyODk1Y2U3Y2JjODE5N2JkNDBmZjcwYmYxMGQzNzQ1ZGU5N2I2YzJhOWM1ZmM3OGYifX19").build());
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setEquipment(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setGlint(true).build());
        setEquipment(EquipmentSlot.BOOTS, new ItemBuilder(Material.DIAMOND_BOOTS).build());
        setEquipment(EquipmentSlot.MAIN_HAND, new ItemBuilder(Material.DIAMOND_HOE).setGlint(true).build());
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.368);
        /*8.5^2*/
        TaskScheduler aoeTask = new TaskScheduler() {
            int i = 0;

            @Override
            public void run() {
                if (isDead || isRemoved()) {
                    cancel();
                    return;
                }
                i++;
                if (enraged && i >= 12) {
                    enraged = false;
                    i = 0;
                    setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
                }
                if (!enraged && i >= 40) {
                    enraged = true;
                    i = 0;
                    setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherColor(new Color(0xFF0000)).setGlint(true).build());
                }
                if (owner.getPosition().distanceSquared(getPosition()) <= 72.25 /*8.5^2*/) {
                    owner.getTemporaryModifiers().add(Stat.Defense, new PlayerStatEvent.BasicModifier("Revenant Horror", 0.75, PlayerStatEvent.Type.MultiplicativeMultiplier, PlayerStatEvent.StatsCategory.Ability), Duration.ofSeconds(1));
                    owner.damage(RevenantHorrorIII.this, false);
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
        return 400_000;
    }

    @Override
    public double getDamage() {
        return (enraged ? 5 : 1) * 120;
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
        return 200;
    }

    @Override
    public int getTier() {
        return 3;
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
