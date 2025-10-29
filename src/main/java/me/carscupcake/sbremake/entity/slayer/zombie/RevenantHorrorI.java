package me.carscupcake.sbremake.entity.slayer.zombie;

import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.SlayerEntity;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.RevenantFlesh;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import org.jetbrains.annotations.NotNull;

public class RevenantHorrorI extends SlayerEntity implements SkillXpDropper {
    public static final RngMeterEntry REVENANT_FLESH = new RngMeterEntry("REVENANT_FLESH", new RngMeterItemLoot(SbItemStack.from(RevenantFlesh.class), 1, 3, SlayerLootTable.LootTableType.Token, Slayers.Zombie, 10_000), new RngMeterItemLoot(SbItemStack.from(RevenantFlesh.class), 9, 18, SlayerLootTable.LootTableType.Token, Slayers.Zombie, 10_000), new RngMeterItemLoot(SbItemStack.from(RevenantFlesh.class), 30, 50, SlayerLootTable.LootTableType.Token, Slayers.Zombie, 10_000), new RngMeterItemLoot(SbItemStack.from(RevenantFlesh.class), 50, 64, SlayerLootTable.LootTableType.Token, Slayers.Zombie, 10_000), new RngMeterItemLoot(SbItemStack.from(RevenantFlesh.class), 63, 64, SlayerLootTable.LootTableType.Token, Slayers.Zombie, 10_000));
    public static final SlayerLootTable lootTable;

    static {
        lootTable = new SlayerLootTable();
        lootTable.addLoot(REVENANT_FLESH.loots()[0]);
    }

    public RevenantHorrorI(SkyblockPlayer owner) {
        super(EntityType.ZOMBIE, lootTable, owner, MobType.Undead);
        setEquipment(EquipmentSlot.HELMET, new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0aW1lc3RhbXAiOjE1NjgwMzY3MjYwNjYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFmYzAxODQ0NzNmZTg4MmQyODk1Y2U3Y2JjODE5N2JkNDBmZjcwYmYxMGQzNzQ1ZGU5N2I2YzJhOWM1ZmM3OGYifX19").build());
        setEquipment(EquipmentSlot.CHESTPLATE, new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setEquipment(EquipmentSlot.LEGGINGS, new ItemBuilder(Material.CHAINMAIL_LEGGINGS).setGlint(true).build());
        setEquipment(EquipmentSlot.BOOTS, new ItemBuilder(Material.DIAMOND_BOOTS).build());
        setEquipment(EquipmentSlot.MAIN_HAND, new ItemBuilder(Material.DIAMOND_HOE).setGlint(true).build());
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.368);
    }

    @Override
    public void spawn() {
        addAIGroup(slayerTarget());
    }

    @Override
    public float getMaxHealth() {
        return 500;
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
        return 50;
    }

    @Override
    public int getTier() {
        return 1;
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
