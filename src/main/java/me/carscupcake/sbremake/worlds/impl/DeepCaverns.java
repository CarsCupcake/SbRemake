package me.carscupcake.sbremake.worlds.impl;

import me.carscupcake.sbremake.entity.impl.deepCaverns.*;
import me.carscupcake.sbremake.event.PlayerInteractEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.Pair;
import me.carscupcake.sbremake.util.TemplateItems;
import me.carscupcake.sbremake.util.gui.Gui;
import me.carscupcake.sbremake.util.gui.InventoryBuilder;
import me.carscupcake.sbremake.util.gui.ItemBuilder;
import me.carscupcake.sbremake.util.quest.Dialog;
import me.carscupcake.sbremake.worlds.EntitySpawner;
import me.carscupcake.sbremake.worlds.Launchpad;
import me.carscupcake.sbremake.worlds.Npc;
import me.carscupcake.sbremake.worlds.SkyblockWorld;
import me.carscupcake.sbremake.worlds.region.CuboidRegion;
import me.carscupcake.sbremake.worlds.region.PolygonalRegion;
import net.minestom.server.collision.BoundingBox;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeepCaverns extends SkyblockWorld.WorldProvider {

    public DeepCaverns() {
        super(List.of(new Launchpad(5, 89, 2, 88, 156, SkyblockWorld.GoldMines, new Pos(0, 105, 181))));
    }

    @Override
    public SkyblockWorld type() {
        return SkyblockWorld.DeepCaverns;
    }

    @Override
    public Pos spawn() {
        return new Pos(4, 157, 85, 180, 0);
    }

    @Override
    public Region[] regions() {
        return Region.values();
    }

    private final Set<EntitySpawner> spawners = new HashSet<>();

    @Override
    protected void register() {
        spawners.add(new EntitySpawner(new Pos[]{new Pos(37.0, 119.0, 17.0), new Pos(21.0, 119.0, 7.0), new Pos(-3.0, 120.0, 18.0), new Pos(-9.0, 119.0, 28.0), new Pos(-18.0, 123.0, 25.0), new Pos(-32.0, 120.0, 25.0), new Pos(-30.0, 120.0, 7.0), new Pos(-44.0, 119.0, 14.0), new Pos(-34.0, 126.0, 32.0), new Pos(-55.0, 120.0, 39.0), new Pos(-42.0, 118.0, 46.0), new Pos(-34.0, 120.0, 30.0), new Pos(-1.0, 118.0, 8.0), new Pos(-4.0, 121.0, -5.0), new Pos(6.0, 123.0, -7.0), new Pos(24.0, 123.0, -4.0), new Pos(38.0, 122.0, -19.0), new Pos(52.0, 125.0, -25.0), new Pos(55.0, 126.0, -13.0)}
                , 200, new EntitySpawner.BasicConstructor(LapisZombies::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-39.0, 36.0, 36.0), new Pos(-51.0, 36.0, 43.0), new Pos(-54.0, 35.0, 52.0), new Pos(-63.0, 36.0, 29.0), new Pos(-57.0, 36.0, 11.0), new Pos(-50.0, 37.0, -6.0), new Pos(-34.0, 38.0, -27.0), new Pos(-21.0, 38.0, -34.0), new Pos(-2.0, 37.0, -35.0), new Pos(10.0, 36.0, -28.0), new Pos(11.0, 36.0, -20.0), new Pos(8.0, 35.0, -5.0), new Pos(-6.0, 34.0, 11.0), new Pos(3.0, 35.0, 23.0), new Pos(20.0, 36.0, 30.0), new Pos(21.0, 36.0, 53.0), new Pos(12.0, 35.0, 59.0), new Pos(38.0, 37.0, 16.0), new Pos(26.0, 36.0, 12.0), new Pos(9.0, 34.0, 12.0), new Pos(3.0, 35.0, 24.0), new Pos(-14.0, 34.0, 8.0), new Pos(-6.0, 34.0, 2.0)},
                200, new EntitySpawner.BasicConstructor(MinerZombie15::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(38.0, 37.0, 23.0), new Pos(30.0, 36.0, 35.0), new Pos(24.0, 36.0, 49.0), new Pos(11.0, 35.0, 59.0), new Pos(3.0, 37.0, 47.0), new Pos(-9.0, 37.0, 48.0), new Pos(-25.0, 36.0, 56.0), new Pos(-40.0, 35.0, 63.0), new Pos(-53.0, 35.0, 51.0), new Pos(-63.0, 35.0, 38.0), new Pos(-62.0, 36.0, 25.0), new Pos(-53.0, 36.0, 10.0), new Pos(-40.0, 34.0, 6.0), new Pos(-46.0, 37.0, -8.0), new Pos(-47.0, 37.0, -18.0), new Pos(-27.0, 38.0, -29.0), new Pos(-1.0, 37.0, -35.0), new Pos(42.0, 36.0, -18.0), new Pos(34.0, 34.0, 3.0), new Pos(5.0, 35.0, 23.0), new Pos(23.0, 36.0, 36.0)},
                200, new EntitySpawner.BasicConstructor(MinerSkeleton15::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(33.0, 11.0, 17.0), new Pos(18.0, 11.0, 24.0), new Pos(4.0, 13.0, 38.0), new Pos(-13.0, 11.0, 47.0), new Pos(-13.0, 11.0, 31.0), new Pos(-20.0, 10.0, 16.0), new Pos(-38.0, 9.0, 14.0), new Pos(-44.0, 9.0, 3.0), new Pos(-34.0, 11.0, -23.0), new Pos(-10.0, 11.0, -28.0), new Pos(3.0, 11.0, -24.0), new Pos(19.0, 11.0, -13.0), new Pos(5.0, 13.0, 34.0), new Pos(-6.0, 12.0, 39.0), new Pos(-16.0, 11.0, 41.0), new Pos(-43.0, 10.0, 48.0), new Pos(-59.0, 12.0, 47.0), new Pos(-69.0, 11.0, 38.0), new Pos(-68.0, 11.0, 21.0), new Pos(-64.0, 11.0, 4.0), new Pos(-73.0, 11.0, -8.0), new Pos(-66.0, 13.0, -22.0), new Pos(-48.0, 12.0, -25.0), new Pos(-32.0, 11.0, -21.0), new Pos(-21.0, 10.0, -4.0), new Pos(1.0, 10.0, 13.0), new Pos(18.0, 11.0, 23.0)}
                , 200, new EntitySpawner.BasicConstructor(MinerZombie20::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(33.0, 11.0, 17.0), new Pos(30.0, 11.0, 3.0), new Pos(12.0, 12.0, -15.0), new Pos(-13.0, 12.0, -27.0), new Pos(-28.0, 11.0, -17.0), new Pos(-39.0, 11.0, -20.0), new Pos(-52.0, 12.0, -25.0), new Pos(-73.0, 11.0, -6.0), new Pos(-63.0, 11.0, 2.0), new Pos(-42.0, 9.0, 5.0), new Pos(-47.0, 12.0, 22.0), new Pos(-41.0, 10.0, 40.0), new Pos(-42.0, 13.0, 60.0), new Pos(-64.0, 12.0, 43.0), new Pos(-68.0, 11.0, 20.0), new Pos(-70.0, 12.0, 4.0), new Pos(-68.0, 13.0, -20.0), new Pos(-32.0, 11.0, -22.0), new Pos(-1.0, 11.0, -26.0), new Pos(16.0, 11.0, 23.0), new Pos(13.0, 12.0, 16.0), new Pos(-9.0, 12.0, 18.0), new Pos(-23.0, 10.0, 5.0), new Pos(-23.0, 11.0, -10.0)},
                200, new EntitySpawner.BasicConstructor(MinerSkeleton20::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(3.0, 151.0, 24.0), new Pos(-14.0, 152.0, 34.0), new Pos(-16.0, 154.0, 19.0), new Pos(-26.0, 153.0, 26.0), new Pos(-33.0, 158.0, 33.0), new Pos(-39.0, 154.0, 8.0), new Pos(-19.0, 154.0, 8.0), new Pos(-24.0, 154.0, -22.0), new Pos(-9.0, 152.0, -30.0), new Pos(10.0, 154.0, -37.0), new Pos(30.0, 157.0, -30.0), new Pos(53.0, 159.0, -23.0), new Pos(57.0, 158.0, 2.0), new Pos(26.0, 155.0, 16.0), new Pos(17.0, 160.0, 0.0), new Pos(2.0, 154.0, 16.0), new Pos(13.0, 163.0, -7.0), new Pos(25.0, 160.0, -26.0)}
                , 200, new EntitySpawner.BasicConstructor(SneakyCreeper::new), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(-21.0, 63.0, 28.0), new Pos(3.0, 64.0, -14.0), new Pos(23.0, 64.0, 29.0), new Pos(0.0, 64.0, -13.0)},
                200, new EntitySpawner.BasicConstructor(() -> new EmeraldSlime(15)), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(29.0, 64.0, 29.0), new Pos(26.0, 65.0, 54.0), new Pos(38.0, 69.0, 54.0), new Pos(13.0, 65.0, 63.0), new Pos(3.0, 64.0, 54.0), new Pos(-34.0, 64.0, 41.0), new Pos(-21.0, 64.0, 19.0), new Pos(15.0, 64.0, -3.0), new Pos(35.0, 64.0, 1.0), new Pos(32.0, 64.0, -20.0)},
                200, new EntitySpawner.BasicConstructor(() -> new EmeraldSlime(10)), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(14.0, 64.0, -2.0), new Pos(8.0, 66.0, -6.0), new Pos(-2.0, 65.0, -1.0), new Pos(-11.0, 65.0, 14.0), new Pos(25.0, 65.0, 36.0), new Pos(25.0, 65.0, 53.0), new Pos(19.0, 65.0, 62.0), new Pos(-5.0, 64.0, 49.0), new Pos(-28.0, 65.0, 47.0), new Pos(-25.0, 63.0, 33.0), new Pos(-10.0, 64.0, 37.0), new Pos(21.0, 64.0, 31.0), new Pos(39.0, 69.0, 45.0), new Pos(38.0, 69.0, 54.0), new Pos(-36.0, 66.0, 6.0)},
                200, new EntitySpawner.BasicConstructor(() -> new EmeraldSlime(5)), container));
        spawners.add(new EntitySpawner(new Pos[]{new Pos(37.0, 100.0, 24.0), new Pos(36.0, 99.0, 13.0), new Pos(34.0, 97.0, 2.0), new Pos(23.0, 100.0, 3.0), new Pos(10.0, 101.0, -2.0), new Pos(11.0, 100.0, -12.0), new Pos(22.0, 101.0, -27.0), new Pos(37.0, 99.0, -23.0), new Pos(39.0, 98.0, -8.0), new Pos(-6.0, 99.0, -1.0), new Pos(4.0, 100.0, 14.0), new Pos(12.0, 97.0, 19.0), new Pos(22.0, 96.0, 36.0), new Pos(18.0, 101.0, 50.0), new Pos(-14.0, 106.0, -2.0), new Pos(-8.0, 88.0, -20.0), new Pos(2.0, 95.0, -21.0)},
                200, new EntitySpawner.BasicConstructor(RedstonePigman::new), container));
        npcs = new Npc[]{buildLiftOperator(new Pos(45.5, 150, 15.5)), buildLiftOperator(new Pos(45.5, 121, 15.5)), buildLiftOperator(new Pos(45.5, 101, 17.5)), buildLiftOperator(new Pos(45.5, 66, 15.5)), buildLiftOperator(new Pos(45.5, 38, 15.5)), buildLiftOperator(new Pos(45.5, 13, 15.5))};
        super.register();
    }

    private Npc buildLiftOperator(Pos pos) {
        return (Npc) new Npc(pos, container, "§bLift Operator", new PlayerSkin("eyJ0aW1lc3RhbXAiOjE1NTQ1NzE2OTkxOTAsInByb2ZpbGVJZCI6IjNmYzdmZGY5Mzk2MzRjNDE5MTE5OWJhM2Y3Y2MzZmVkIiwicHJvZmlsZU5hbWUiOiJZZWxlaGEiLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg2NTJhMDZmN2I5OWU0ODIyYjQ5NWJhOWI2MDg5ZDRkNTE1MWU4M2JlZDk1NmE3Y2NjYjg0N2VhNDZhMjU3MjUifX19",
                "W8e6ip++dGgqglbYiPyLxCEHUXpOMioWrf6bCJwMNPHmEFTJux+M0H4GZaDjs1+d7FcSHjg6rEbMWS1sZAL4TUh7Q9vCtEYlx0PfnkY42l1Qqo/tIpJvgY5J6RHZ1j1cvtfrgXfKU8AKpjZVDNNyAqc5iJWcgqAm1gj3SPH6SoVvfzZgx9avAKo3z440CRsIhLYwgwEtq8/sbkqi0y6cuwlCZ+reo7yy2Ohe5AwG0Sx7Tkkv0DIVC4wO2RYoP+4xw2MYi1SRWk9yv3ZKqjwhTP8ugB/xqK/R480vVrr7MLhCpLrbUzpuLiaAbfruF9/TmvBV2hXFSCrlqypo4EmLk1E3WSuJX5ls11+juht0M12MIUlmmYcsFjgnAux5tgJ+fQq3KWhrbYedEY4CG7swavIG71/ZZI/ugRCXz/KONOq7bXKn939CyIpPdfBBAo2RZhjk2QXG/bWODeTfJ6z5VlIivNzo65FCAdwJ54VQzmUcP86ID3/jrWQ5fE5fhPkGhpYtOlVn5S4xKdFtu6RfYYUX6cEPD6MRPcOzXB4ZvCzgD7QhKxIQDBc1S9XY35c6+ZDYHPokQa87iD053Yfft4PH/pZA21ovOcf7+Xa+AFu72wnsjbynkaZUqSrFz3mCdOl+TFynZe89SwgDX0t1mIPtGtJmtK5jbZpwFA4bqDE=")).withInteraction(new LiftOperator());
    }

    @Override
    protected void unregister() {
        spawners.forEach(EntitySpawner::stop);
        spawners.clear();
        super.unregister();
    }

    //Gunpowder 255 - 132
    //Lapis 132 - 115
    //Redstone 115 - 74
    //Emerald 74 - 44
    //Dia1 44 - 19
    //Dia1 / Obi 19 - 0
    public enum Region implements me.carscupcake.sbremake.worlds.region.Region {
        GunpowderMines("§bGunpowder Mines", new Pair<>(new Pos(80, 175, 50), new Pos(-64, 132, -67))),
        LapisQuarry("§bLapis Quarry", new Pair<>(new Pos(63, 132, 87), new Pos(-64, 115, -67))),
        PigmensDen("§bPigmen's Den", new Pair<>(new Pos(63, 115, 87), new Pos(-64, 74, -67))),
        Slimehill("§bSlimehill", new Pair<>(new Pos(63, 74, 87), new Pos(-64, 44, -67))),
        DiamondReserve("§bDiamond Reserve", new Pair<>(new Pos(63, 44, 87), new Pos(-64, 19, -67))),
        ObsidianSanctuary("§bObsidian Sanctuary", new Pair<>(new Pos(63, 19, 87), new Pos(-64, 0, -67)));
        private final me.carscupcake.sbremake.worlds.region.Region wrapped;

        @SafeVarargs
        Region(String name, Pair<Pos, Pos>... positions) {
            Set<BoundingBox> box = new HashSet<>();
            for (Pair<Pos, Pos> posPair : positions)
                box.add(BoundingBox.fromPoints(posPair.getFirst(), posPair.getSecond()));
            wrapped = new CuboidRegion(name, box);
        }

        Region(String name, Pos... positions) {
            pickP0(positions);
            wrapped = new PolygonalRegion(name, positions, 300, -64);
        }

        private static void pickP0(Pos[] pos2ds) {
            int minX = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;
            int minI = 0;
            for (int i = 0; i < pos2ds.length; i++) {
                if (pos2ds[i].x() < minX || (pos2ds[i].x() == minX && pos2ds[i].y() > maxY)) {
                    minX = pos2ds[i].blockX();
                    maxY = pos2ds[i].blockY();
                    minI = i;
                }
            }
            if (minI == 0) return;
            Pos[] copy = Arrays.copyOf(pos2ds, pos2ds.length);
            int j = 0;
            for (int i = minI; i < pos2ds.length; i++) {
                pos2ds[i] = copy[j];
                j++;
            }
            for (int i = 0; i < minI; i++) {
                pos2ds[i] = copy[j];
                j++;
            }
        }

        @Override
        public boolean isInRegion(Point pos) {
            return wrapped.isInRegion(pos);
        }

        @Override
        public String toString() {
            return wrapped.name();
        }
    }

    public record LiftOperator(Inventory inv) implements Npc.Interaction {
        public LiftOperator() {
            this(new InventoryBuilder(6, "Lift")
                    .fill(TemplateItems.EmptySlot.getItem())
                    .setItem(10, new ItemBuilder(Material.GOLD_INGOT).setName("§aGunpowder Mines")
                            .addAllLore("§7Teleports you to the §bGunpowder", "§bMines§7!")
                            .build()
                    )
                    .setItem(12, new ItemBuilder(Material.LAPIS_LAZULI).setName("§aLapis Quarry")
                            .addAllLore("§7Teleports you to the §bLapis", "§bQuarry§7!")
                            .build()
                    )
                    .setItem(14, new ItemBuilder(Material.REDSTONE).setName("§aPigmen's Den")
                            .addAllLore("§7Teleports you to the §bPigmen's", "§bDen§7!")
                            .build()
                    )
                    .setItem(16, new ItemBuilder(Material.EMERALD).setName("§aSlimehill")
                            .addAllLore("§7Teleports you to the §bSlimehill")
                            .build()
                    )
                    .setItem(28, new ItemBuilder(Material.DIAMOND).setName("§aDiamond Reserves")
                            .addAllLore("§7Teleports you to the §bDiamond", "§bReserves§7!")
                            .build()
                    )
                    .setItem(30, new ItemBuilder(Material.OBSIDIAN).setName("§aObsidian Sanctuary")
                            .addAllLore("§7Teleports you to the §bObsidian", "§bSanctuary§7!")
                            .build()
                    )
                    .setItem(32, new ItemBuilder(Material.PRISMARINE).setName("§aDwarven Mines")
                            .addAllLore("§7Teleports you to the §2Dwarven", "§2Mines§7!")
                            .build()
                    )
                    .build());

        }

        private static Dialog firstTimeDialog = new Dialog("§e[NPC] Lift Operator§f:", 20).addLine("Hey Feller!")
                .addLine("I control this lift here behind me.")
                .addLine("Once you've explored an area I can give you a safe ride back there.")
                .addLine("Be careful not to fall down the shaft though, it's a long fall!")
                .addLine("Good luck on your adventures.");

        @Override
        public void interact(SkyblockPlayer player, PlayerInteractEvent.Interaction interaction) {
            if (!player.getTags().contains("Lift Operator")) {
                firstTimeDialog.build(player);
                player.getTags().add("Lift Operator");
            } else {
                Gui gui = new Gui(inv);
                gui.setCancelled(true);
                gui.getClickEvents().add(10, ignored -> {
                    if (player.getRegion() == Region.GunpowderMines)
                        player.sendMessage("§cYou are already in this room");
                    else
                        player.teleport(new Pos(52.5, 150.5, 15.5, 90, 0));
                    player.closeGui();
                    return false;
                });
                gui.getClickEvents().add(12, ignored -> {
                    if (player.getRegion() == Region.LapisQuarry)
                        player.sendMessage("§cYou are already in this room");
                    else
                        player.teleport(new Pos(52.5, 121.5, 15.5, 90, 0));
                    player.closeGui();
                    return false;
                });
                gui.getClickEvents().add(14, ignored -> {
                    if (player.getRegion() == Region.PigmensDen)
                        player.sendMessage("§cYou are already in this room");
                    else
                        player.teleport(new Pos(52.5, 101.5, 15.5, 90, 0));
                    player.closeGui();
                    return false;
                });
                gui.getClickEvents().add(16, ignored -> {
                    if (player.getRegion() == Region.Slimehill)
                        player.sendMessage("§cYou are already in this room");
                    else
                        player.teleport(new Pos(52.5, 66.5, 15.5, 90, 0));
                    player.closeGui();
                    return false;
                });
                gui.getClickEvents().add(28, ignored -> {
                    if (player.getRegion() == Region.DiamondReserve)
                        player.sendMessage("§cYou are already in this room");
                    else
                        player.teleport(new Pos(52.5, 38.5, 15.5, 90, 0));
                    player.closeGui();
                    return false;
                });
                gui.getClickEvents().add(30, ignored -> {
                    if (player.getRegion() == Region.ObsidianSanctuary)
                        player.sendMessage("§cYou are already in this room");
                    else
                        player.teleport(new Pos(52.5, 13.5, 15.5, 90, 0));
                    player.closeGui();
                    return false;
                });
                gui.getClickEvents().add(32, ignored -> {
                    SkyblockWorld.sendToBest(SkyblockWorld.DwarvenMines, player);
                    player.closeGui();
                    return false;
                });
                gui.showGui(player);
            }
        }
    }

    @Override
    public Pair<Pos, Pos> getChunksToLoad() {
        return toMinMaxPair(new Pos(80, 0, 313), new Pos(-390, 0, -100));
    }
}
