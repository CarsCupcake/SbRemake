package me.carscupcake.sbremake.util;

import lombok.Getter;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.network.packet.server.play.BlockChangePacket;

import java.util.HashSet;
import java.util.Set;

@Getter
public class FakeBlock {
    public static final Set<FakeBlock> fakeBlocks = new HashSet<>();
    private final BlockVec block;
    private final Instance instance;
    private Block material;
    private SkyblockPlayer[] players = null;

    public FakeBlock(BlockVec block, Instance instance, Block material) {
        this.block = block;
        this.instance = instance;
        this.material = material;
    }

    private FakeBlock show() {
        fakeBlocks.add(this);
        for (Player player : instance.getPlayers())
            player.sendPacket(new BlockChangePacket(block, material));
        return this;
    }

    public FakeBlock show(SkyblockPlayer... players) {
        if (players == null || players.length == 0) {
            show();
            return this;
        }
        fakeBlocks.add(this);
        for (Player player : players)
            player.sendPacket(new BlockChangePacket(block, material));
        this.players = players;
        return this;
    }

    public void release() {
        fakeBlocks.remove(this);
        Block b = instance.getBlock(block);
        for (FakeBlock fakeBlock : fakeBlocks) {
            if (fakeBlock == this) continue;
            if (fakeBlock.instance != instance) continue;
            if (fakeBlock.block.equals(this.block) && fakeBlock.players == null) {
                b = fakeBlock.material;
                break;
            }
        }
        if (players == null) {
            for (Player player : instance.getPlayers())
                player.sendPacket(new BlockChangePacket(block, b));
        } else {
            for (Player player : players) {
                if (player.getInstance() == instance)
                    player.sendPacket(new BlockChangePacket(block, b));
            }
        }
        players = null;
    }
}
