package me.carscupcake.sbremake.blocks;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.player.PlayerCancelDiggingEvent;
import net.minestom.server.event.player.PlayerStartDiggingEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import java.time.Duration;
import java.util.Random;
import java.util.Set;

@Getter
public abstract class MiningBlock {
    private final Block block;

    public MiningBlock(Block block) {
        this.block = block;
    }

    public abstract int blockStrength();

    public int getInstaMineSpeed() {
        return (blockStrength() * 60) + 1;
    }

    public abstract int getBreakingPower();

    public abstract Set<SbItemStack> getDrops(SkyblockPlayer player);

    //Ticks
    public int regenTime() {
        return 5 * 20;
    }

    public Block blockIfBroken() {
        return Block.BEDROCK;
    }

    public Block resetType() {
        return block;
    }

    public long getSoftCap() {
        return Math.round((6d + 2d / 3d) * blockStrength());
    }

    public boolean allowed(SkyblockWorld world) {
        for (MiningBlock b : world.getOres())
            if (b == this) return true;
        return false;
    }

    public int getMiningTicks(SkyblockPlayer player) {
        double mining_speed = player.getStat(Stat.MiningSpeed);
        double SoftCap = getSoftCap();
        if (SoftCap <= mining_speed) mining_speed = SoftCap;

        double MiningTime = (blockStrength() * 30) / mining_speed;
        return (int) MiningTime;
    }


    public void breakBlock(Pos pos, SkyblockPlayer player, BlockFace face) {
        Instance instance = player.getInstance();
        instance.setBlock(pos, blockIfBroken());
        new TaskScheduler() {
            @Override
            public void run() {
                reset(instance, pos);
            }
        }.delayTask(regenTime());
        dropItems(player, pos, face);
        player.getSkill(getSkill()).addXp(getXp());
        instance.playSound(breakingSound(), pos);
    }

    protected void dropItems(SkyblockPlayer player, Pos block, BlockFace face) {
        for (SbItemStack item : getDrops(player))
            if (item != null && item.item().amount() > 0) {
                item.drop(player, player.getInstance(), block.add(face.toDirection().normalX() + 0.5, face.toDirection().normalY(), face.toDirection().normalZ() + 0.5));
            }
    }

    public boolean isInstantBreak(long speed) {
        return speed >= getInstaMineSpeed();
    }

    public Skill getSkill() {
        return Skill.Mining;
    }

    public abstract double getXp();

    public void reset(Instance instance, Pos block) {
        instance.setBlock(block, resetType());
    }

    public Sound breakingSound() {
        return SoundType.BLOCK_STONE_BREAK.create(Sound.Source.BLOCK, 1.0f, 0.8f);
    }

    public static final EventNode<Event> BREAK_NODE = EventNode.all("block.break").addListener(PlayerStartDiggingEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (!player.getWorldProvider().useCustomMining()) return;
        if (player.getBlockBreakScheduler() != null) {
            player.getBlockBreakScheduler().cancel();
            player.setBlockBreakScheduler(null);
        }
        Mining.make(player, Pos.fromPoint(event.getBlockPosition()), event.getBlockFace());
    }).addListener(PlayerCancelDiggingEvent.class, event -> {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (!player.getWorldProvider().useCustomMining()) return;
        if (player.getBlockBreakScheduler() != null) {
            player.getBlockBreakScheduler().cancel();
            player.setBlockBreakScheduler(null);
        }
    });

    public static void init() {
        MinecraftServer.getGlobalEventHandler().addChild(BREAK_NODE);
    }

    public SbItemStack withMiningFortune(ISbItem sbItem, int base, SkyblockPlayer player) {
        double miningFortune = player.getStat(Stat.MiningFortune) / 100d;
        long baseMult = (long) miningFortune;
        double chance = miningFortune - baseMult;
        if (new Random().nextDouble() <= chance) baseMult++;
        return sbItem.create().withAmount((int) (base * (1 + baseMult)));
    }
}
