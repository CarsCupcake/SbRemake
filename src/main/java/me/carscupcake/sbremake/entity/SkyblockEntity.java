package me.carscupcake.sbremake.entity;

import lombok.Getter;
import lombok.Setter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.SlayerQuest;
import me.carscupcake.sbremake.event.*;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.SkyblockPlayerArrow;
import me.carscupcake.sbremake.player.potion.Potion;
import me.carscupcake.sbremake.player.potion.PotionEffect;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.player.skill.SkillXpDropper;
import me.carscupcake.sbremake.util.*;
import me.carscupcake.sbremake.util.lootTable.ILootTable;
import me.carscupcake.sbremake.util.lootTable.LootTable;
import me.carscupcake.sbremake.worlds.region.Region;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.color.Color;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.*;
import net.minestom.server.entity.ai.EntityAIGroup;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.entity.ai.goal.DoNothingGoal;
import net.minestom.server.entity.ai.goal.RandomStrollGoal;
import net.minestom.server.entity.ai.goal.RangedAttackGoal;
import net.minestom.server.entity.ai.target.ClosestEntityTarget;
import net.minestom.server.entity.ai.target.LastEntityDamagerTarget;
import net.minestom.server.entity.attribute.Attribute;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.entity.pathfinding.Navigator;
import net.minestom.server.event.EventDispatcher;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.particle.Particle;
import net.minestom.server.timer.Task;
import net.minestom.server.timer.TaskSchedule;
import net.minestom.server.utils.time.Cooldown;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Getter
public abstract class SkyblockEntity extends EntityCreature {
    @Getter
    private final ILootTable<SbItemStack> lootTable;
    private final Set<TaskScheduler> assignedTask = new HashSet<>();
    private float health = 1;
    @Getter
    @Setter
    private SkyblockPlayer lastDamager;

    private final Object _lock = new Object();

    public SkyblockEntity(@NotNull EntityType entityType) {
        this(entityType, null);
    }

    public SkyblockEntity(@NotNull EntityType entityType, ILootTable<SbItemStack> lootTable) {
        super(entityType, UUID.randomUUID());
        this.lootTable = lootTable == null ? new LootTable<>() : lootTable;
        getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.15f);
        setCustomNameVisible(true);
    }

    public static void spawnDamageTag(SkyblockEntity entity, String tag) {
        //in case the entity got removed in the damage process
        if (entity.instance == null) return;
        EntityCreature creature = new EntityCreature(EntityType.ARMOR_STAND, UUID.randomUUID());
        creature.setCustomName(Component.text(tag));
        creature.setCustomNameVisible(true);
        creature.setInvisible(true);
        creature.setNoGravity(true);
        creature.setInvulnerable(true);
        ArmorStandMeta meta = (ArmorStandMeta) creature.getEntityMeta();
        meta.setHasNoBasePlate(true);
        meta.setMarker(true);
        BoundingBox bb = entity.getBoundingBox();
        int random = new Random().nextInt(4);
        double x = switch (random) {
            case 0 -> bb.maxX() + 0.5;
            case 2 -> bb.minX() - 0.5;
            default -> 0;
        };
        double z = switch (random) {
            case 1 -> bb.maxZ() + 0.5;
            case 3 -> bb.minZ() - 0.5;
            default -> 0;
        };
        Pos pos = entity.getPosition().add(x, 0.7d, z);
        creature.setInstance(entity.getInstance(), pos.add(0, new Random().nextDouble(0.5) - 0.25, 0));
        creature.scheduler().buildTask(creature::remove).delay(Duration.ofSeconds(1)).schedule();
    }

    public static void init() {

    }

    protected static EntityAIGroup randomStroll(SkyblockEntity entity, int range) {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().add(new RandomStrollGoal(entity, range));
        aiGroup.getGoalSelectors().add(new DoNothingGoal(entity, 3000, 0.5f));
        return aiGroup;
    }

    protected static EntityAIGroup randomStroll(SkyblockEntity entity, Region region, int range) {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().add(new RandomStrollInRegion(entity, range, region));
        return aiGroup;
    }

    protected static EntityAIGroup regionTarget(SkyblockEntity entity, Region region, int range) {
        return regionTarget(entity, List.of(region), range);
    }

    protected static EntityAIGroup regionTarget(SkyblockEntity entity, List<Region> region, int range, boolean isHidden) {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(entity, range), new ClosestEntityTarget(entity, range, entity1 -> entity1 instanceof SkyblockPlayer p && !p.isDead() && p.getGameMode() == GameMode.SURVIVAL &&
                ((!isHidden && p.getRegion() != null && region.contains(p.getRegion())) || (isHidden && region.stream().map(region1 -> region1.isInRegion(p.getPosition())).reduce(false, Boolean::logicalOr)))
                && !entity.isDead)));
        return aiGroup;
    }

    protected static EntityAIGroup regionTarget(SkyblockEntity entity, List<Region> region, int range) {
        return regionTarget(entity, region, range, false);
    }

    protected static EntityAIGroup zombieAiGroup(SkyblockEntity entity) {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().addAll(List.of(new MeleeAttackGoal(entity, 1.6, 20, TimeUnit.SERVER_TICK), new RandomStrollInRegion(entity, 16, new ArrayList<>(), true) // Walk around
        ));
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(entity, 10), new ClosestEntityTarget(entity, 6, entity1 -> entity1 instanceof Player p && !p.isDead() && p.getGameMode() == GameMode.SURVIVAL && !entity.isDead)));
        return aiGroup;
    }

    protected static EntityAIGroup zombieAiGroup(SkyblockEntity entity, Region region) {
        return zombieAiGroup(entity, region, false);
    }protected static EntityAIGroup zombieAiGroup(SkyblockEntity entity, Region region, boolean isHidden) {
        return zombieAiGroup(entity, List.of(region), isHidden);
    }

    protected static EntityAIGroup zombieAiGroup(SkyblockEntity entity, List<Region> region) {
        return zombieAiGroup(entity, region, false);
    }

    protected static EntityAIGroup zombieAiGroup(SkyblockEntity entity, List<Region> region, boolean isHidden) {
        EntityAIGroup aiGroup = regionTarget(entity, region, 8, isHidden);
        aiGroup.getGoalSelectors().addAll(List.of(new MeleeAttackGoal(entity, 1.6, 20, TimeUnit.SERVER_TICK), new RandomStrollInRegion(entity, 10, region, isHidden) // Walk around
        ));
        return aiGroup;
    }

    protected static RangedAttackGoal createRangedAttackGoal(SkyblockEntity entity) {
        RangedAttackGoal rangedAttackGoal = new RangedAttackGoal(entity, 80, 25, 15, false, 1, 0.2, TimeUnit.SERVER_TICK);
        rangedAttackGoal.setProjectileGenerator(_ -> new SkyblockEntityProjectile(entity, EntityType.ARROW));
        return rangedAttackGoal;
    }

    protected static EntityAIGroup skeletonAiGroup(SkyblockEntity entity) {
        EntityAIGroup aiGroup = new EntityAIGroup();
        aiGroup.getGoalSelectors().addAll(List.of(createRangedAttackGoal(entity), new RandomStrollInRegion(entity, 5, new ArrayList<>(), true) // Walk around
        ));
        aiGroup.getTargetSelectors().addAll(List.of(new LastEntityDamagerTarget(entity, 32), new ClosestEntityTarget(entity, 32, entity1 -> entity1 instanceof Player && !entity.isDead)));
        return aiGroup;
    }

    protected static EntityAIGroup skeletonAiGroup(SkyblockEntity entity, Region region) {
        EntityAIGroup aiGroup = regionTarget(entity, region, 20);
        aiGroup.getGoalSelectors().addAll(List.of(createRangedAttackGoal(entity), new RandomStrollInRegion(entity, 10, region) // Walk around
        ));
        return aiGroup;
    }

    public abstract float getMaxHealth();

    public double getDamage() {
        return 0;
    }

    public double getTrueDamage() {
        return 0;
    }

    public float getDefense() {
        return 0;
    }

    public boolean canTakeKnockback() {
        return true;
    }

    public Function<SkyblockEntity, String> nameTag() {
        return NameTagType.Basic;
    }

    private LivingEntity nameTag;

    @Override
    public CompletableFuture<Void> setInstance(@NotNull Instance instance, @NotNull Pos spawnPosition) {
        setHealth(getMaxHealth());
        health = getMaxHealth();
        return super.setInstance(instance, spawnPosition).thenRun(() -> {
            synchronized (_lock) {
                if (showCustomNameTag()) {
                    nameTag = new LivingEntity(EntityType.ARMOR_STAND);
                    ((ArmorStandMeta) nameTag.getEntityMeta()).setMarker(true);
                    nameTag.setCustomName(getCustomName());
                    nameTag.setCustomNameVisible(true);
                    nameTag.setInstance(instance, getPosition());
                    nameTag.setCustomNameVisible(true);
                    nameTag.setInvisible(true);
                    addPassenger(nameTag);
                }
            }
        });
    }

    protected boolean showCustomNameTag() {
        return isInvisible();
    }

    @Override
    public void remove() {
        super.remove();
        if (nameTag != null) {
            nameTag.remove();
        }
    }

    @Override
    public void setCustomName(@Nullable Component customName) {
        super.setCustomName(customName);
        if (showCustomNameTag() && nameTag != null) {
            nameTag.setCustomName(customName);
        }
    }

    public int getLevel() {
        return -1;
    }

    public double abilityDamageMultiplier() {
        return 1;
    }

    public abstract String getName();

    public void damage(SkyblockPlayer player) {
        damage(player, true);
    }

    public void damage(SkyblockPlayer player, boolean ferocity) {
        PlayerMeleeDamageEntityEvent event = new PlayerMeleeDamageEntityEvent(player, this, player.getStat(Stat.Damage), player.getStat(Stat.Strength), player.getStat(Stat.CritDamage), player.getStat(Stat.CritChance), player.getStat(Stat.Ferocity));
        event.setCanDoFerocity(ferocity);
        EventDispatcher.call(event);
        damage(event);
        if (event.isCanDoFerocity()) {
            doFerocity(player, event.getFerocity());
        }
    }
    public double mageDamage(SkyblockPlayer player, double intelligence, double baseAbilityDamage, double abilityScaling) {
        PlayerToEntityMageDamage event = new PlayerToEntityMageDamage(player, this, baseAbilityDamage, intelligence, abilityScaling);
        EventDispatcher.call(event);
        if (event.isCancelled()) return 0;
        float damage = (float) (event.calculateHit() * abilityDamageMultiplier());
        damage = onDamage(event.getPlayer(), damage);
        if (damage <= 0) return 9;
        spawnDamageTag(this, event.getDamageTag());
        var finalDamage = damage * (1 - (getDefense() / (getDefense() + 100)));
        damage(DamageType.PLAYER_ATTACK, finalDamage);
        if (isDead) {
            EventDispatcher.call(new EntityDeathEvent(this, EntityDeathEvent.Type.Magic));
        }
        return damage;
    }

    public double mageDamage(SkyblockPlayer player, double baseAbilityDamage, double abilityScaling) {
        return mageDamage(player, player.getStat(Stat.Intelligence), baseAbilityDamage, abilityScaling);
    }

    public void damage(SkyblockPlayerArrow projectile) {
        PlayerProjectileDamageEntityEvent event = new PlayerProjectileDamageEntityEvent(this, projectile);
        EventDispatcher.call(event);
        damage(event);
    }

    public void damage(PlayerToEntityDamageEvent event) {
        if (event.isCancelled()) return;
        float damage = (float) ((event.isCrit()) ? event.calculateCritHit() : event.calculateHit());
        damage = damage * (1 - (getDefense() / (getDefense() + 100)));
        damage = onDamage(event.getPlayer(), damage);
        if (damage <= 0) return;
        event.getPostEvent().forEach(c -> c.accept(event));
        spawnDamageTag(this, event.getDamageTag());
        lastDamager = event.getPlayer();
        damage(DamageType.PLAYER_ATTACK, damage * (1 - (getDefense() / (getDefense() + 100))));
        if (isDead) {
            EventDispatcher.call(new EntityDeathEvent(this, event.damageType()));
        }
        if (canTakeKnockback()) {
            double mult = 1;
            PotionEffect effect = event.getPlayer().getPotionEffect(Potion.Knockback);
            if (effect != null) mult = 1 + (0.2 * effect.amplifier());
            this.takeKnockback((float) (0.4 * mult), Math.sin(event.damagerPos().yaw() * 0.017453292), -Math.cos(event.damagerPos().yaw() * 0.017453292));
        }
    }

    @Override
    public void kill() {
        super.kill();
        if (lastDamager != null) {
            if (this instanceof SkillXpDropper dropper) {
                dropper.apply(lastDamager);
                if (dropper.type() == Skill.Combat && lastDamager.getSlayerQuest() != null && lastDamager.getSlayerQuest().getStage() == SlayerQuest.SlayerQuestStage.XpGathering) {
                    if (lastDamager.getSlayerQuest().getSlayer().getSlayer().addXp(this, lastDamager.getSlayerQuest().getTier()))
                        lastDamager.getSlayerQuest().addXp(lastDamager.getSkill(Skill.Combat).calculateXp(dropper.amount(lastDamager)), getPosition());
                }
            }
            Set<SbItemStack> items = lootTable.loot(lastDamager);
            for (SbItemStack item : items) {
                item.drop(lastDamager, getInstance(), getPosition());
            }
        }
        for (TaskScheduler scheduler : new HashSet<>(assignedTask))
            scheduler.cancel();
    }

    @Override
    protected void remove(boolean permanent) {
        super.remove(permanent);
        for (TaskScheduler scheduler : new HashSet<>(assignedTask))
            scheduler.cancel();
    }

    public void doFerocity(SkyblockPlayer player, double ferocity) {
        int ticks = (int) (ferocity * 0.01);
        double pers = (ferocity - (ticks * 100)) / 100d;
        if (new Random().nextDouble() <= pers) ticks++;
        if (ticks == 0) return;
        player.getInstance().playSound(Sound.sound(SoundType.ITEM_FLINTANDSTEEL_USE.getKey(), Sound.Source.AMBIENT, 1, 0f), getPosition());
        FerocityRunnable runnable = new FerocityRunnable(ticks, player);
        Task task = this.scheduler().scheduleTask(runnable, TaskSchedule.tick(10), TaskSchedule.tick(10));
        runnable.setSelf(task);
    }

    @Override
    public @NotNull Pos getPosition() {
        return super.position;
    }

    public void damage(double amount) {
        lastDamager = null;
        damage(DamageType.GENERIC, (float) amount);
    }

    @Override
    public float getHealth() {
        return health;
    }

    @Override
    public void setHealth(float health) {
        this.health = Math.max(0, Math.min(getMaxHealth(), health));
        super.setHealth((float) (getAttribute(Attribute.MAX_HEALTH).getValue() * (health / getMaxHealth())));
        update();
    }

    public void update() {
        if (isDead) return;
        String name = nameTag().apply(this);
        setCustomName(Component.text(name));
        setCustomNameVisible(true);
    }

    public void assignTask(TaskScheduler task) {
        assignedTask.add(task);
        task.setEntity(this);
    }

    public void unassignTask(TaskScheduler task) {
        assignedTask.remove(task);
    }

    /**
     * Is called to modifier and execute on damage
     *
     * @param amount the calcualted damaege amount
     * @return the new damage amount
     */
    protected float onDamage(SkyblockPlayer player, float amount) {
        return amount;
    }

    public enum NameTagType implements Function<SkyblockEntity, String> {
        Basic() {
            @Override
            public String apply(SkyblockEntity skyblockEntity) {
                return "§8[§7Lv" + (skyblockEntity.getLevel()) + "§8] §c" + (skyblockEntity.getName()) + " §a" + ((skyblockEntity.getHealth() >= 100_000 ?  StringUtils.toShortNumber(skyblockEntity.getHealth()) : StringUtils.toFormatedNumber(skyblockEntity.getHealth()))) + "§7/§a" + (skyblockEntity.getMaxHealth() >= 100_000 ?  StringUtils.toShortNumber(skyblockEntity.getMaxHealth()) : StringUtils.toFormatedNumber(skyblockEntity.getMaxHealth())) + "§c" + (Stat.Health.getSymbol());
            }
        }, Slayer() {
            @Override
            public String apply(SkyblockEntity entity) {
                return "§c" + (Characters.Skull) + " §f" + (entity.getName()) + " §a" + (StringUtils.toShortNumber(entity.getHealth())) + "§c" + (Stat.Health.getSymbol());
            }
        }
    }

    @Getter
    protected static class RandomStrollInRegion extends GoalSelector {
        private static final long DELAY = 2500L;
        private final int radius;
        private final List<Vec> closePositions;
        private final Random random = new Random();
        private final List<Region> regions;
        private final long randomDelay = new Random().nextLong(5000);
        private long lastStroll;
        private final boolean isHiddenRegion;
        private final SkyblockEntity entity;

        public RandomStrollInRegion(@NotNull SkyblockEntity skyblockEntity, int radius, Region region) {
            super(skyblockEntity);
            this.entity = skyblockEntity;
            this.regions = List.of(region);
            this.radius = radius;
            this.closePositions = getNearbyBlocks(radius);
            isHiddenRegion = false;
        }

        public RandomStrollInRegion(@NotNull SkyblockEntity skyblockEntity, int radius, List<Region> region, boolean isHidden) {
            super(skyblockEntity);
            this.entity = skyblockEntity;
            this.regions = region;
            this.radius = radius;
            this.closePositions = getNearbyBlocks(radius);
            this.isHiddenRegion = isHidden;
        }

        public boolean shouldStart() {
            return System.currentTimeMillis() - this.lastStroll >= DELAY + randomDelay && !entity.getInstance().getPlayers().isEmpty();
        }

        public void start() {
            if (entity.getPosition().equals(Pos.ZERO)) {
                return;
            }
            int remainingAttempt = this.closePositions.size();
            while (remainingAttempt-- > 0) {
                int index = this.random.nextInt(this.closePositions.size());
                Vec position = this.closePositions.get(index);
                Pos target = this.entity.getPosition().add(position);
                try {
                    var chunk = entity.getInstance().getChunk(target.chunkX(), target.chunkZ());
                    if (chunk == null || !chunk.isLoaded()) return;
                    boolean result = this.entity.getNavigator().setPathTo(target);
                    if (result) {
                        break;
                    }
                } catch (NullPointerException e) {
                    //Could not care less
                    if (e.getMessage().contains("Unloaded chunk at")) return;
                    throw new RuntimeException(e);
                }
            }
        }

        public void tick(long time) {
        }

        public boolean shouldEnd() {
            return true;
        }

        public void end() {
            this.lastStroll = System.currentTimeMillis();
        }

        private @NotNull List<Vec> getNearbyBlocks(int radius) {
            List<Vec> blocks = new ArrayList<>();
            for (int x = -radius; x <= radius; ++x) {
                for (int y = -radius; y <= radius; ++y) {
                    for (int z = -radius; z <= radius; ++z) {
                        Vec vec = new Vec(x, y, z);
                        var relative = this.entity.getPosition().add(vec);
                        var chunk = entity.getInstance().getChunkAt(relative.chunkX(), relative.chunkZ());
                        if (chunk == null || !chunk.isLoaded()) chunk = entity.getInstance().loadChunk(relative).join();
                        if (chunk.getBlock(relative.blockX(), relative.blockY(), relative.blockZ()).isSolid()) continue;
                        if (!chunk.getBlock(relative.blockX(), relative.blockY() - 1, relative.blockZ()).isSolid()) continue;
                        for (Region region : regions) {
                            if (region.isInRegion(relative)) {
                                blocks.add(vec);
                                break;
                            }
                        }
                    }
                }
            }

            return blocks;
        }
    }

    public static class MeleeAttackGoal extends GoalSelector {

        private final Cooldown cooldown = new Cooldown(Duration.of(5, TimeUnit.SERVER_TICK));
        private final double range;
        private final Duration delay;
        private long lastHit;
        private boolean stop;
        private Entity cachedTarget;

        /**
         * @param entityCreature the entity to add the goal to
         * @param range          the allowed range the entity can attack others.
         * @param delay          the delay between each attacks
         * @param timeUnit       the unit of the delay
         */
        public MeleeAttackGoal(@NotNull EntityCreature entityCreature, double range, int delay, @NotNull TemporalUnit timeUnit) {
            this(entityCreature, range, Duration.of(delay, timeUnit));
        }

        /**
         * @param entityCreature the entity to add the goal to
         * @param range          the allowed range the entity can attack others.
         * @param delay          the delay between each attacks
         */
        public MeleeAttackGoal(@NotNull EntityCreature entityCreature, double range, Duration delay) {
            super(entityCreature);
            this.range = range;
            this.delay = delay;
        }

        public static void navigator(long time, Entity target, Navigator navigator, Cooldown cooldown) {
            final var pathPosition = navigator.getPathPosition();
            final var targetPosition = target.getPosition();
            if (pathPosition == null || !pathPosition.samePoint(targetPosition)) {
                if (cooldown.isReady(time)) {
                    cooldown.refreshLastUpdate(time);
                    navigator.setPathTo(targetPosition);
                }
            }
        }

        public @NotNull Cooldown getCooldown() {
            return this.cooldown;
        }

        @Override
        public boolean shouldStart() {
            this.cachedTarget = findTarget();
            return this.cachedTarget != null;
        }

        @Override
        public void start() {
            final Point targetPosition = this.cachedTarget.getPosition();
            entityCreature.getNavigator().setPathTo(targetPosition);
        }

        @Override
        public void tick(long time) {
            Entity target;
            if (this.cachedTarget != null) {
                target = this.cachedTarget;
                this.cachedTarget = null;
            } else {
                target = findTarget();
                if (entityCreature instanceof MeleeAttackGoalEntity meleeAttackGoalEntity)
                    meleeAttackGoalEntity.setGoalTarget(target);
            }

            this.stop = target == null;

            if (!stop) {

                // Attack the target entity
                if (entityCreature.getDistanceSquared(target) <= range * range) {
                    entityCreature.lookAt(target);
                    if (!Cooldown.hasCooldown(time, lastHit, delay)) {
                        entityCreature.attack(target, true);
                        this.lastHit = time;
                    }
                    return;
                }

                // Move toward the target entity
                navigator(time, target, entityCreature.getNavigator(), this.cooldown);
            }
        }

        @Override
        public boolean shouldEnd() {
            return stop;
        }

        @Override
        public void end() {
            // Stop following the target
            entityCreature.getNavigator().setPathTo(null);
        }
    }

    public class FerocityRunnable implements Runnable {

        private final SkyblockPlayer player;
        @Setter
        public Task self = null;
        private int ticks;

        public FerocityRunnable(int ticks, SkyblockPlayer player) {
            this.ticks = ticks;
            this.player = player;
        }

        @Override
        public void run() {
            ticks--;
            damage(player, false);
            Particle.Dust dust = Particle.Dust.DUST.withColor(new Color(0xFF0000));
            Pos manage = getPosition();

            Vec a;
            Vec b;
            Pos start;
            if (manage.yaw() >= -45 && manage.yaw() <= 45) {
                start = manage.add(-1, 0.5, 0);
                a = start.asVec();
                b = start.add(2, 1.5, 0).asVec();
            } else {
                if (manage.yaw() >= 135 || manage.yaw() <= -135) {
                    start = manage.add(1, 0.5, 0);
                    a = start.asVec();
                    b = start.add(-2, 1.5, 0).asVec();
                } else {
                    if (manage.yaw() >= 45) {
                        start = manage.add(0, 0.5, -1);
                        a = start.asVec();
                        b = start.add(0, 1.5, 2).asVec();
                    } else {
                        start = manage.add(0, 0.5, 1);
                        a = start.asVec();
                        b = start.add(0, 1.5, -2).asVec();
                    }
                }
            }
            Vec between = b.sub(a);
            double length = between.length();
            between = between.normalize().mul(0.3);
            double steps = length / 0.3D;
            for (int i = 0; i < steps; i++) {
                a = a.add(between);
                ParticleUtils.spawnParticle(instance, a, dust, 1);
            }
            player.getInstance().playSound(SoundType.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR.create(0.1f, 2f), getPosition());
            if (ticks <= 0) self.cancel();
        }
    }
}
