package me.carscupcake.sbremake.listeners;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import me.carscupcake.sbremake.Main;
import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.blocks.Crop;
import me.carscupcake.sbremake.blocks.FarmingCrystal;
import me.carscupcake.sbremake.blocks.Log;
import me.carscupcake.sbremake.blocks.MiningBlock;
import me.carscupcake.sbremake.event.LogBreakEvent;
import me.carscupcake.sbremake.item.IVanillaPickaxe;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.item.VanillaPickaxeTier;
import me.carscupcake.sbremake.item.impl.other.foraging.Lushlilac;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.lootTable.blockLoot.BlockLootTable;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.impl.*;
import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.BlockVec;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.player.PlayerBlockBreakEvent;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockFace;
import net.minestom.server.timer.TaskSchedule;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;

public class PlayerBlockBreakListener implements Consumer<PlayerBlockBreakEvent> {
    private final String[] pickaxesBlocks;
    private final Map<VanillaPickaxeTier, String[]> needToolTierBlocks = new HashMap<>();

    public PlayerBlockBreakListener() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream resource = classloader.getResourceAsStream("assets/tags/blocks/pickaxe.json")) {
            JsonElement json = JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(resource)));
            pickaxesBlocks = json.getAsJsonObject().get("values").getAsJsonArray().asList().stream().parallel().map(JsonElement::getAsString).toArray(String[]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loadTierBlocks("assets/tags/blocks/needs_diamond_tool.json", VanillaPickaxeTier.Diamond);
        loadTierBlocks("assets/tags/blocks/needs_iron_tool.json", VanillaPickaxeTier.Iron);
        loadTierBlocks("assets/tags/blocks/needs_stone_tool.json", VanillaPickaxeTier.Stone);
    }

    private void loadTierBlocks(String file, VanillaPickaxeTier tier) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        try (InputStream resource = classloader.getResourceAsStream(file)) {
            JsonElement json = JsonParser.parseReader(new InputStreamReader(Objects.requireNonNull(resource)));
            needToolTierBlocks.put(tier, json.getAsJsonObject().get("values").getAsJsonArray().asList().stream().parallel().map(JsonElement::getAsString).toArray(String[]::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void accept(PlayerBlockBreakEvent event) {
        SkyblockPlayer player = (SkyblockPlayer) event.getPlayer();
        if (player.getWorldProvider().type() == SkyblockWorld.Hub) {
            if (player.getRegion() == Hub.Region.Forest) {
                Log log = null;
                for (Log l : Log.logs)
                    if (Objects.requireNonNull(l.block().registry().material()).equals(event.getBlock().registry().material())) {
                        log = l;
                        break;
                    }
                if (log != null) {
                    ((Hub) player.getWorldProvider()).brokenLogs.put(event.getBlockPosition(), new Log.LogInfo(log, event.getBlock().properties()));
                    blockBreakLog(event, player, log);
                    return;
                }
            }
            if (player.getRegion() == Hub.Region.CoalMine) {
                MiningBlock block = null;
                for (MiningBlock miningBlock : player.getWorldProvider().ores(player.getPosition())) {
                    if (miningBlock.getBlock() == event.getBlock()) {
                        block = miningBlock;
                        break;
                    }
                }
                if (block != null && block.allowed(player.getWorldProvider().type())) {
                    block.breakBlock(Pos.fromPoint(event.getBlockPosition()), player, event.getBlockFace());
                    event.setCancelled(true);
                    return;
                }
            }
            for (Crop c : Crop.crops) {
                if (c.block().registry().id() == event.getBlock().registry().id()) {
                    for (SbItemStack item : c.drops(player)) {
                        item.drop(player, player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5));
                    }
                    if (c.xp() > 0)
                        player.getSkill(Skill.Farming).addXp(c.xp());
                    FarmingCrystal closest = null;
                    double distance = Double.MAX_VALUE;
                    for (FarmingCrystal farmingCrystal : ((Hub) player.getWorldProvider()).getCrystals()) {
                        double d = farmingCrystal.location().distanceSquared(event.getBlockPosition());
                        if (d < distance) {
                            closest = farmingCrystal;
                            distance = d;
                        }
                    }
                    if (closest != null && distance < 30 * 30) {
                        closest.blocks().put(event.getBlockPosition(), event.getBlock());
                    } else {
                        MinecraftServer.getSchedulerManager().buildTask(() -> event.getInstance().setBlock(event.getBlockPosition(), event.getBlock())).delay(TaskSchedule.seconds(30)).schedule();
                        Main.LOGGER.info("Crop not in range!");
                    }
                    return;
                }
            }
        } else if (player.getWorldProvider() instanceof ForagingIsle foragingIsle) {
            Log log = null;
            for (Log l : Log.logs)
                if (Objects.requireNonNull(l.block().registry().material()).equals(event.getBlock().registry().material())) {
                    log = l;
                    break;
                }
            if (log != null) {
                foragingIsle.brokenLogs.put(event.getBlockPosition(), new Log.LogInfo(log, event.getBlock().properties()));
                blockBreakLog(event, player, log);
                return;
            }
        } else if (player.getWorldProvider().type() == SkyblockWorld.FarmingIsles) {
            for (Crop c : Crop.crops) {
                if (c.block().registry().id() == event.getBlock().registry().id()) {
                    for (SbItemStack item : c.drops(player)) {
                        item.drop(player, player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5));
                    }
                    if (c.xp() > 0)
                        player.getSkill(Skill.Farming).addXp(c.xp());
                    if (c.block() == Block.SUGAR_CANE) {
                        BlockVec block = event.getBlockPosition();
                        while (player.getInstance().getBlock(block).id() == Block.SUGAR_CANE.id()) {
                            ((FarmingIsles) player.getWorldProvider()).getSugarCane().add(block.blockY(), block);
                            if (block != event.getBlockPosition()) {
                                player.getInstance().setBlock(block, Block.AIR);
                                for (SbItemStack item : c.drops(player)) {
                                    item.drop(player, player.getInstance(), block.add(0.5, 0, 0.5));
                                }
                                if (c.xp() > 0)
                                    player.getSkill(Skill.Farming).addXp(c.xp());
                            }
                            block = block.add(0, 1, 0);
                        }
                        return;
                    }
                    if (c.block() == Block.COCOA) {
                        MinecraftServer.getSchedulerManager().buildTask(() -> event.getInstance().setBlock(event.getBlockPosition(), event.getBlock())).delay(TaskSchedule.seconds(10)).schedule();
                        return;
                    }
                    FarmingCrystal closest = null;
                    double distance = Double.MAX_VALUE;
                    for (FarmingCrystal farmingCrystal : ((FarmingIsles) player.getWorldProvider()).getCrystals()) {
                        double d = farmingCrystal.location().distanceSquared(event.getBlockPosition());
                        if (d < distance) {
                            closest = farmingCrystal;
                            distance = d;
                        }
                    }
                    if (closest != null && distance < 30 * 30) {
                        closest.blocks().put(event.getBlockPosition(), event.getBlock());
                    } else {
                        MinecraftServer.getSchedulerManager().buildTask(() -> event.getInstance().setBlock(event.getBlockPosition(), event.getBlock())).delay(TaskSchedule.seconds(5)).schedule();
                    }
                    return;
                }
            }
        }
        if (((SkyblockPlayer) event.getPlayer()).getWorldProvider().type() == SkyblockWorld.PrivateIsle) {
            boolean b = false;
            for (String s : pickaxesBlocks) {
                if (s.equalsIgnoreCase(event.getBlock().registry().key().asString())) {
                    b = true;
                    break;
                }
            }
            if (b) {
                SbItemStack mainHand = player.getSbItemInHand(PlayerHand.MAIN);
                if (mainHand == null || !(mainHand.sbItem() instanceof IVanillaPickaxe pick)) return;
                switch (pick.getTier()) {
                    case Wood:
                        if (checkBlocks(VanillaPickaxeTier.Stone, event.getBlock()))
                            return;
                    case Stone:
                        if (checkBlocks(VanillaPickaxeTier.Iron, event.getBlock()))
                            return;
                    case Iron:
                        if (checkBlocks(VanillaPickaxeTier.Diamond, event.getBlock()))
                            return;
                }
            }
            BlockLootTable lootTable = BlockLootTable.blockLootTables.get(event.getBlock().registry().id());
            if (lootTable != null) {
                var items = lootTable.loot(player);
                items.forEach(item -> item.drop(player, player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5)));
            }
            return;
        }
        if (player.getWorldProvider() instanceof Galatea galatea) {
            if (event.getBlock().registry().key().equals(Block.FLOWERING_AZALEA.key())) {
                event.setCancelled(false);
                event.setResultBlock(Block.AZALEA);
                SbItemStack.from(Lushlilac.class).calculateFortuneAmount(1, player.getStat(Stat.ForagingFortune)).drop(player, player.getInstance(), event.getBlockPosition().middle().relative(BlockFace.TOP));
                player.getSkill(Skill.Foraging).addXp(10);
                var instance = event.getInstance();
                var pos = event.getBlockPosition();
                galatea.scheduleTask(() -> instance.setBlock(pos, Block.FLOWERING_AZALEA), 2_400);
                return;
            }
        }
        event.setCancelled(true);
    }

    private boolean checkBlocks(VanillaPickaxeTier tier, Block block) {
        for (var s : needToolTierBlocks.get(tier))
            if (s.equalsIgnoreCase(block.registry().key().asString()))
                return true;
        return false;
    }

    private void blockBreakLog(PlayerBlockBreakEvent event, SkyblockPlayer player, Log log) {
        SbItemStack item = log.drops(player);
        LogBreakEvent logBreakEvent = new LogBreakEvent(player, event.getBlockPosition(), log, new ArrayList<>(List.of(item)));
        MinecraftServer.getGlobalEventHandler().call(logBreakEvent);
        for (SbItemStack i : logBreakEvent.drops()) {
            ItemEntity entity = new ItemEntity(i.item());
            entity.setInstance(player.getInstance(), event.getBlockPosition().add(0.5, 0, 0.5));
            entity.addViewer(player);
        }
        player.getSkill(Skill.Foraging).addXp(log.xp());
        return;
    }
}
