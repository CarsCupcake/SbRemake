package me.carscupcake.sbremake.entity.slayer.voidgloom;

import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.item.impl.bow.arrowPoison.TwilightArrowPoison;
import me.carscupcake.sbremake.item.impl.other.combat.SummoningEye;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.*;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterItemLoot;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.LivingEntity;
import net.minestom.server.entity.metadata.monster.EndermanMeta;
import net.minestom.server.entity.metadata.other.ArmorStandMeta;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.particle.Particle;

import java.util.ArrayList;
import java.util.Collections;

public class VoidgloomSeraphII extends VoidgloomSeraphI {
    public static final RngMeterEntry TWILIGHT_ARROW_POISON
            = new RngMeterEntry("TWILIGHT_ARROW_POISON",
                                new RngMeterItemLoot(TwilightArrowPoison.class, 16, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 1_800),
                                new RngMeterItemLoot(TwilightArrowPoison.class, 24, 32, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 1_800),
                                new RngMeterItemLoot(TwilightArrowPoison.class, 60, 64, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 1_800));
    public static final RngMeterEntry SUMMONING_EYE
            = new RngMeterEntry("SUMMONING_EYE",
                                new RngMeterItemLoot(SummoningEye.class, SlayerLootTable.LootTableType.Main, Slayers.Enderman, 80));

    public VoidgloomSeraphII(SkyblockPlayer owner) {
        this(owner, new SlayerLootTable(), 2);
    }
    public VoidgloomSeraphII(SkyblockPlayer owner, SlayerLootTable lootTable, int tier) {
        super(owner, getLootTable(lootTable, tier), tier);
    }

    public static SlayerLootTable getLootTable(SlayerLootTable lootTable, int tier) {
        return lootTable.addLoot(TWILIGHT_ARROW_POISON.loots()[tier - 2]).addLoot(SUMMONING_EYE.loots()[0]);
    }

    @Override
    public double getDamage() {
        return 5_000;
    }

    @Override
    public float getMaxHealth() {
        return 12_000_000;
    }

    @Override
    public int getMaxHits() {
        return 30;
    }

    @Override
    public int getTier() {
        return 2;
    }

    private boolean isYangGlyphActive = false;

    @Override
    protected float onDamage(SkyblockPlayer player, float amount) {
        if (!isYangGlyphActive && getMaxHealth() * 0.5 >= getHealth() - amount) {
            isYangGlyphActive = true;
            scheduleBeaconThrow();
        }
        return super.onDamage(player, amount);
    }
    protected boolean isHolding = false;

    public boolean canThrow() {
        return getHits() == 0;
    }

    private void holdBeacon() {
        var endermanMeta = (EndermanMeta) getEntityMeta();
        endermanMeta.setCarriedBlockID(Block.BEACON.stateId());
        isHolding = true;
        beaconTasks = new TaskScheduler() {
            @Override
            public void run() {
                if (!canThrow()) return;
                throwBeacon();
            }
        };
        assignTask(beaconTasks);
        beaconTasks.delayTask(40);
    }

    private TaskScheduler beaconTasks;

    private void scheduleBeaconThrow() {
        beaconTasks = new TaskScheduler() {
            @Override
            public void run() {
                if (canThrow())
                    holdBeacon();
            }
        };
        assignTask(beaconTasks);
        beaconTasks.delayTask(20 * 8);
    }

    private void throwBeacon() {
        isHolding = false;
        var endermanMeta = (EndermanMeta) getEntityMeta();
        endermanMeta.setCarriedBlockID(Block.AIR.id());
        var possibles = new ArrayList<BlockVec>();
        for (int x = -16; x <= 16; x++) {
            for (int y = -5; y <= 5; y++) {
                for (int z = -16; z <= 16; z++) {
                    if (x * x + z * z < 5 * 5) continue;
                    var root = getPosition().add(x, y, z);
                    if (getInstance().getBlock(root).isAir()) continue;
                    root = root.add(0, 1, 0);
                    if (!getInstance().getBlock(getPosition().add(x, y + 2, z)).isAir()) continue;
                    if (!getInstance().getBlock(getPosition().add(x, y + 3, z)).isAir()) continue;
                    if (!getInstance().getBlock(root.relative(BlockFace.NORTH)).isAir()) continue;
                    if (!getInstance().getBlock(root.relative(BlockFace.SOUTH)).isAir()) continue;
                    if (!getInstance().getBlock(root.relative(BlockFace.EAST)).isAir()) continue;
                    if (!getInstance().getBlock(root.relative(BlockFace.WEST)).isAir()) continue;
                    if (getInstance().getBlock(root).isAir())
                        possibles.add(new BlockVec(root));
                }
            }
        }
        Collections.shuffle(possibles);
        var itterable = possibles.iterator();
        BlockVec pos = null;
        while (itterable.hasNext()) {
            var next = itterable.next();
            if (EntityUtils.blocksInSight(instance, getPosition().add(0, 1,0),
                                      Vec.fromPoint(next.middle().add(0, .5, 0).sub(getPosition().add(0,1, 0))),
                                      next.middle().add(0, .5, 0).distance(getPosition().add(0,1, 0))))
                continue;
            pos = next;
            break;
        }
        if (possibles.isEmpty() || pos == null) {
            Main.LOGGER.warn("No Yang Glyph possibilities found! Pre Check count: {}", possibles.size());
            scheduleBeaconThrow();
            return;
        }
        var entity = new LivingEntity(EntityType.ARMOR_STAND);
        var meta = (ArmorStandMeta) entity.getEntityMeta();
        meta.setInvisible(true);
        entity.setEquipment(EquipmentSlot.HELMET, ItemStack.of(Material.BEACON));
        entity.setNoGravity(true);
        entity.setInstance(getInstance(), getPosition());
        var sine = new EntitySinusMovement(1, 3, 0);
        BlockVec finalPos = pos;
        sine.move(entity, () -> {
                      entity.remove();
                      placeBeacon(finalPos);
                  }, 20, 0, pos.middle(),
                  () -> entity.teleport(entity.getPosition().withYaw(entity.getPosition().yaw() + 20)));
    }

    private FakeBlock beaconBlock;

    private void placeBeacon(BlockVec pos) {
        if (isDead) return;
        var middle = pos.middle();
        beaconBlock = new FakeBlock(pos, getInstance(), Block.BEACON);
        beaconBlock.show(owner);
        var test = new TaskScheduler() {
            int i = 0;
            @Override
            public void run() {
                if (i == 20 * 5) {
                    owner.damage(0, owner.getStat(Stat.Health) * 1_000);
                    beaconBlock.release();
                    cancel();
                    return;
                }
                if (owner.getDistance(middle) <= 2.2) {
                    cancel();
                    beaconBlock.release();
                    scheduleBeaconThrow();
                    instance.playSound(SoundType.BLOCK_GLASS_BREAK.create(1, 1), middle);
                    return;
                }
                ParticleUtils.spawnParticle(getInstance(), middle, Particle.ENCHANT, 5, new Vec(-.5), 1);
                instance.playSound(SoundType.ENTITY_ELDER_GUARDIAN_CURSE.create(0.5F, (float) (0.5 + (i * 0.0125))), middle);
                i++;
            }
        };
        test.repeatTask(1, 1);
        assignTask(test);
    }

    @Override
    public void remove(boolean force) {
        super.remove(force);
        if (beaconBlock != null)
            beaconBlock.release();
    }

    @Override
    protected void onHitPhase() {
        if (beaconTasks != null && beaconTasks.isRunning())
            beaconTasks.cancel();
    }

    @Override
    protected void onHitsLost() {
        super.onHitsLost();
        if (isYangGlyphActive) {
            var task = new TaskScheduler() {

                @Override
                public void run() {
                    if (isHolding)
                        throwBeacon();
                    else
                        holdBeacon();
                }
            };
            assignTask(task);
            task.delayTask(20 * 4);
        }
    }
}
