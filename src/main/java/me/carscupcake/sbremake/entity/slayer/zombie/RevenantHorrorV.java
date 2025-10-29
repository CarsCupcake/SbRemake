package me.carscupcake.sbremake.entity.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.ISlayer;
import me.carscupcake.sbremake.entity.slayer.SlayerEntity;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.RevenantViscera;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.ShardOfTheShredded;
import me.carscupcake.sbremake.item.impl.other.slayer.zombie.WardenHeart;
import me.carscupcake.sbremake.item.modifiers.Modifier;
import me.carscupcake.sbremake.item.modifiers.enchantment.NormalEnchantments;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.*;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.instance.block.Block;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.TaskSchedule;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static me.carscupcake.sbremake.entity.slayer.zombie.RevenantHorrorI.REVENANT_FLESH;

public class RevenantHorrorV extends SlayerEntity {
    public static final RngMeterEntry SMITE_VII = new RngMeterEntry("SMITE_VII", new RngMeterItemLoot(SbItemStack.base(Material.ENCHANTED_BOOK).withModifier(Modifier.ENCHANTMENTS, Map.of(NormalEnchantments.Smite, 7)), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 7));
    public static final RngMeterEntry SHARD_OF_THE_SHREDDED = new RngMeterEntry("SHARD_OF_THE_SHREDDED", new RngMeterItemLoot(SbItemStack.from(ShardOfTheShredded.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 8));
    public static final RngMeterEntry WARDEN_HEART = new RngMeterEntry("WARDEN_HEART", new RngMeterItemLoot(SbItemStack.from(WardenHeart.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 2));
    public static final RngMeterEntry REVENANT_VISCERA = new RngMeterEntry("REVENANT_VISCERA", new RngMeterItemLoot(SbItemStack.from(RevenantViscera.class), SlayerLootTable.LootTableType.Main, Slayers.Zombie, 2_000));

    public static final SlayerLootTable lootTable;

    static {
        lootTable = new SlayerLootTable();
        lootTable.addLoot(REVENANT_FLESH.loots()[4]);
        lootTable.addLoot(RevenantHorrorII.FOUL_FLESH.loots()[3]);
        lootTable.addLoot(RevenantHorrorII.PESTILENCE_RUNE.loots()[2]);
        lootTable.addLoot(RevenantHorrorII.UNDEAD_CATALYST.loots()[0]);
        lootTable.addLoot(RevenantHorrorII.REVENANT_CATALYST.loots()[0]);
        lootTable.addLoot(RevenantHorrorIII.BEHEADED_HORROR.loots()[1]);
        lootTable.addLoot(RevenantHorrorIII.SMITE_VI.loots()[1]);
        lootTable.addLoot(RevenantHorrorIV.SNAKE_RUNE.loots()[0]);
        lootTable.addLoot(RevenantHorrorIV.SCYTHE_BLADE.loots()[1]);
        lootTable.addLoot(RevenantHorrorV.SMITE_VII.loots()[0]);
        lootTable.addLoot(RevenantHorrorV.SHARD_OF_THE_SHREDDED.loots()[0]);
        lootTable.addLoot(RevenantHorrorV.WARDEN_HEART.loots()[0]);
        lootTable.addLoot(RevenantHorrorV.REVENANT_VISCERA.loots()[0]);
    }

    private int bossHits = 0;

    private boolean enraged = false;

    public boolean thunder = false;

    public RevenantHorrorV(SkyblockPlayer owner) {
        super(EntityType.ZOMBIE, lootTable, owner);
        setBoots(new ItemBuilder(Material.LEATHER_BOOTS).setLeatherColor(new Color(0xFFFFFF)).setGlint(true).build());
        setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).setLeatherColor(new Color(0xFFFFFF)).setGlint(true).build());
        setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).setGlint(true).build());
        setHelmet(new ItemBuilder(Material.PLAYER_HEAD).setHeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjg0Njk5MzU1NDNmYzY5ODY0MTcyYzZhZDk1Yzg5YjI4NTVjZDYwNDNkNzAyOGY2NmViZmE4NDRiNTllYTJlOSJ9fX0=").build());
        setItemInHand(PlayerHand.MAIN, new ItemBuilder(Material.IRON_SWORD).setGlint(true).build());
        new TaskScheduler() {
            boolean second = false;
            int i = 0;
            Set<FakeBlock> blocks = new HashSet<>();
            Set<BlockVec> checkingBlocks = new HashSet<>();


            @Override
            public void run() {
                if (isDead || isRemoved()) {
                    cancel();
                    return;
                }
                if ((second || enraged) && !thunder) {
                    Set<FakeBlock> fakeBlocks = new HashSet<>();
                    FakeBlock f = findBlock(getOwner().getPosition(), Block.RED_TERRACOTTA);
                    BlockVec middle = f.getBlock();
                    fakeBlocks.add(f);
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(1, 0, 0), Block.RED_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-1, 0, 0), Block.RED_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(0, 0, 1), Block.RED_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(0, 0, -1), Block.RED_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(2, 0, 0), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-2, 0, 0), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(0, 0, 2), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(0, 0, -2), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(1, 0, 1), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-1, 0, 1), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(1, 0, -1), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-1, 0, -1), Block.WHITE_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(2, 0, 1), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-2, 0, 1), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(2, 0, -1), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-2, 0, -1), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(1, 0, 2), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-1, 0, -2), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(1, 0, -2), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.add(findBlock(getOwner().getPosition().add(-1, 0, 2), Block.LIGHT_GRAY_TERRACOTTA));
                    fakeBlocks.forEach(FakeBlock::show);
                    LivingEntity entity = new LivingEntity(EntityType.ARMOR_STAND);
                    entity.setHelmet(ItemStack.of(Material.TNT));
                    entity.setInstance(instance, getPosition());
                    entity.setInvisible(true);
                    new EntitySinusMovement(1, 3, 0).move(entity, () -> {
                        entity.remove();
                        fakeBlocks.forEach(FakeBlock::release);
                        double percent = 1 - (middle.add(0.5, 1, 0.5).distance(owner.getPosition()) / 2.5);
                        if (percent > 0) {
                            setHealth(Math.min(getMaxHealth(), getHealth() + 500_000));
                            ParticleUtils.spawnParticle(getInstance(), getPosition().add(0, getEyeHeight() / 2, 0), Particle.HAPPY_VILLAGER, 10, new Vec(0.5, getEyeHeight() / 2, 0.5), 0);
                            double damage = owner.getStat(Stat.Health) * (0.1 + (bossHits * 0.02)) * percent;
                            bossHits++;
                            owner.damage(damage, 0);
                        }
                    }, 20, 0, entity.getPosition(), middle.add(0.5, 5, 0.5), () -> {
                        entity.teleport(entity.getPosition().withYaw(entity.getPosition().yaw() + 20));
                    });
                    owner.playSound(SoundType.ENTITY_HORSE_SADDLE, Sound.Source.AMBIENT, 1, 2, getPosition());


                }
                if (second) {
                    if (!thunder) {
                        second = false;
                        if (i == 30) {
                            i = 0;
                            thunder = true;
                            BlockVec surface = surface(getPosition());
                            teleport(Pos.fromPoint(surface.add(0.5, 1, 0.5)));
                            checkingBlocks.add(surface);
                            blocks.add(new FakeBlock(surface, instance, Block.BEDROCK).show());
                        } else
                            i++;
                        if (!enraged && getHealth() < getMaxHealth() / 3f) enraged = true;
                        setHealth(Math.min(getMaxHealth(), getHealth() + ((1f / 1000f) * getMaxHealth())));
                        ParticleUtils.spawnParticle(getInstance(), getPosition().add(0, getEyeHeight() / 2, 0), Particle.HAPPY_VILLAGER, 10, new Vec(0.5, getEyeHeight() / 2, 0.5), 0);
                    } else {
                        if (i == 7) {
                            for (BlockVec b : checkingBlocks) {
                                Entity lightning = new Entity(EntityType.LIGHTNING_BOLT);
                                lightning.setInstance(instance, b.add(0.5, 1, 0.5));
                                lightning.scheduleRemove(Duration.ofSeconds(1));
                            }
                            checkingBlocks.clear();
                            if (owner.getDistance(RevenantHorrorV.this) <= 7) {
                                owner.damage(4800 * Math.max(1, bossHits), 0);
                            }
                            MinecraftServer.getSchedulerManager().buildTask(() -> {
                                blocks.forEach(FakeBlock::release);
                                blocks.clear();
                                thunder = false;
                                i = 0;
                            }).delay(TaskSchedule.seconds(1)).schedule();
                        } else if (i < 7) {
                            Set<BlockVec> clone = new HashSet<>(checkingBlocks);
                            checkingBlocks.clear();
                            for (BlockVec b : clone) {
                                check(b.add(1, 0, 0));
                                check(b.add(-1, 0, 0));
                                check(b.add(0, 0, 1));
                                check(b.add(0, 0, -1));
                            }
                        }
                        i++;
                    }
                } else second = true;
            }

            private void check(BlockVec block) {
                for (FakeBlock b : blocks) {
                    if (b.getBlock().x() == block.x() && block.z() == b.getBlock().z()) return;
                }
                BlockVec surface = surface(block);
                checkingBlocks.add(surface);
                blocks.add(new FakeBlock(surface, instance, Block.BEDROCK).show());
                checkingBlocks.add(block);
                ParticleUtils.spawnParticle(instance, surface.add(0.5, 1, 0.5), Particle.SMOKE, 1);
            }

            @Override
            public synchronized void cancel() {
                blocks.forEach(FakeBlock::release);
                super.cancel();
            }
        }.repeatTask(10, 10);
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
    public void tick(long time) {
        if (thunder) return;
        super.tick(time);
    }

    private BlockVec surface(Point blockVec) {
        Block b = getInstance().getBlock(blockVec);
        if (b.isSolid()) {
            while (b.isSolid()) {
                blockVec = blockVec.add(0, 1, 0);
                b = instance.getBlock(blockVec);
            }
            return new BlockVec(blockVec.sub(0, 1, 0));
        }
        while (b.isAir()) {

            blockVec = blockVec.sub(0, 1, 0);
            b = instance.getBlock(blockVec);
        }
        return new BlockVec(blockVec);
    }

    private FakeBlock findBlock(Point blockVec, Block material) {
        Block b = getInstance().getBlock(blockVec);
        if (b.isSolid()) {
            while (b.isSolid()) {
                blockVec = blockVec.add(0, 1, 0);
                b = instance.getBlock(blockVec);
            }
            return new FakeBlock(new BlockVec(blockVec.sub(0, 1, 0)), instance, material);
        }
        while (b.isAir()) {

            blockVec = blockVec.sub(0, 1, 0);
            b = instance.getBlock(blockVec);
        }
        return new FakeBlock(new BlockVec(blockVec), instance, material);
    }

    @Override
    public int getTier() {
        return 5;
    }

    @Override
    public ISlayer getSlayer() {
        return Slayers.Zombie;
    }

    @Override
    public float getMaxHealth() {
        return 10_000_000;
    }

    @Override
    public String getName() {
        return "Atoned Horror";
    }

    @Override
    public double getDamage() {
        return 2_400;
    }

    @Override
    public void attack(@NotNull Entity target, boolean swingHand) {
        super.attack(target, swingHand);
        bossHits++;
    }

    @Override
    public boolean canTakeKnockback() {
        return super.canTakeKnockback();
    }
}
