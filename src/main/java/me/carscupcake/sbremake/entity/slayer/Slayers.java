package me.carscupcake.sbremake.entity.slayer;

import me.carscupcake.sbremake.config.ConfigSection;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.entity.slayer.voidgloom.VoidgloomSeraphI;
import me.carscupcake.sbremake.entity.slayer.voidgloom.VoidgloomSeraphII;
import me.carscupcake.sbremake.entity.slayer.voidgloom.VoidgloomSeraphIII;
import me.carscupcake.sbremake.entity.slayer.voidgloom.VoidgloomSeraphIV;
import me.carscupcake.sbremake.entity.slayer.voidgloom.miniboss.VoidcrazedManiac;
import me.carscupcake.sbremake.entity.slayer.voidgloom.miniboss.VoidlingDevotee;
import me.carscupcake.sbremake.entity.slayer.voidgloom.miniboss.VoidlingRadical;
import me.carscupcake.sbremake.entity.slayer.zombie.*;
import me.carscupcake.sbremake.entity.slayer.zombie.minibosses.*;
import me.carscupcake.sbremake.item.Requirement;
import me.carscupcake.sbremake.item.requirements.SkillRequirement;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.skill.Skill;
import me.carscupcake.sbremake.util.Lazy;
import me.carscupcake.sbremake.util.ParticleUtils;
import me.carscupcake.sbremake.util.SoundType;
import me.carscupcake.sbremake.util.TaskScheduler;
import me.carscupcake.sbremake.util.lootTable.rngMeter.RngMeterEntry;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerLootTable;
import me.carscupcake.sbremake.util.lootTable.rngMeter.SlayerRngMeter;
import net.kyori.adventure.sound.Sound;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityType;
import net.minestom.server.instance.Instance;
import net.minestom.server.particle.Particle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public enum Slayers implements ISlayer {
    Zombie("Zombie Slayer", "Revenant Horror") {
        private double rngXp(RngMeterEntry entry) {
            lootChancesEntries.put(entry, entry.calculateHighestChance(RevenantHorrorI.lootTable, RevenantHorrorII.lootTable, RevenantHorrorIII.lootTable, RevenantHorrorIV.lootTable, RevenantHorrorV.lootTable));
            return entry.calculateRequiredXp(50_000, RevenantHorrorI.lootTable, RevenantHorrorII.lootTable, RevenantHorrorIII.lootTable, RevenantHorrorIV.lootTable, RevenantHorrorV.lootTable);
        }

        @Override
        public SlayerEntity getEntity(int tier, SkyblockPlayer player) {
            return switch (tier) {
                case 1 -> new RevenantHorrorI(player);
                case 2 -> new RevenantHorrorII(player);
                case 3 -> new RevenantHorrorIII(player);
                case 4 -> new RevenantHorrorIV(player);
                case 5 -> new RevenantHorrorV(player);
                default -> throw new IllegalArgumentException("Zombie Slayer Tier " + (tier) + " does not exsist");
            };
        }

        private final int[] xp = new int[]{5, 15, 200, 1_000, 5_000, 20_000, 100_000, 400_000, 1_000_000};

        @Override
        public int requiredXp(int currentLevel) {
            return xp[currentLevel];
        }

        @Override
        public String getTitle(int level) {
            return switch (level) {
                case 1 -> "Noob";
                case 2 -> "Novice";
                case 3 -> "Skilled";
                case 4 -> "Destroyed";
                case 5 -> "Bulldozer";
                case 6 -> "Savage";
                case 7 -> "Deathripper";
                case 8 -> "Necromancer";
                default -> "Grim Reaper";
            };
        }

        private final Map<RngMeterEntry, Double> rngMeterEntries = new HashMap<>();
        private final Map<RngMeterEntry, Double> lootChancesEntries = new HashMap<>();

        private void addRngMeterEntry(RngMeterEntry entry) {
            rngMeterEntries.put(entry, rngXp(entry));
        }

        @Override
        public SlayerRngMeter createRngMeter(SkyblockPlayer player, ConfigSection section) {
            if (rngMeterEntries.isEmpty()) {
                for (RngMeterEntry entry : getRngMeterEntries())
                    addRngMeterEntry(entry);
            }
            return new SlayerRngMeter(player, this, rngMeterEntries, lootChancesEntries, section);
        }

        private final Lazy<List<RngMeterEntry>> entries = new Lazy<>(() -> List.of(RevenantHorrorII.FOUL_FLESH, RevenantHorrorII.PESTILENCE_RUNE,
                                                                                  RevenantHorrorII.UNDEAD_CATALYST, RevenantHorrorIII.SMITE_VI, RevenantHorrorIII.BEHEADED_HORROR, RevenantHorrorII.REVENANT_CATALYST, RevenantHorrorIV.SNAKE_RUNE, RevenantHorrorV.REVENANT_VISCERA, RevenantHorrorIV.SCYTHE_BLADE, RevenantHorrorV.SMITE_VII, RevenantHorrorV.SHARD_OF_THE_SHREDDED, RevenantHorrorV.WARDEN_HEART));

        @Override
        public List<RngMeterEntry> getRngMeterEntries() {
            return entries.get();
        }

        @Override
        public int getRequiredSlayerQuestXp(int tier) {
            return switch (tier) {
                case 1 -> 150;
                case 2 -> 1_440;
                case 3 -> 2_400;
                case 4 -> 4_800;
                case 5 -> 6_000;
                default -> throw new IllegalStateException("Tier " + (tier) + " does not exist!");
            };
        }


        @Override
        public boolean addXp(SkyblockEntity entity, int tier) {
            if (entity.getEntityType() == EntityType.ZOMBIE) {
                double random = new Random().nextDouble();
                switch (tier) {
                    case 3 -> {
                        if (random <= 0.1)
                            spawnMiniBoss(new RevenantSycophant(), entity.getInstance(), entity.getPosition());
                    }
                    case 4 -> {
                        if (random <= 0.1)
                            spawnMiniBoss(new RevenantChampion(), entity.getInstance(), entity.getPosition());
                        else if (random <= 0.2)
                            spawnMiniBoss(new DeformedRevenant(), entity.getInstance(), entity.getPosition());
                    }
                    case 5 -> {
                        if (random <= 0.1)
                            spawnMiniBoss(new AtonedChampion(), entity.getInstance(), entity.getPosition());
                        else if (random <= 0.2)
                            spawnMiniBoss(new AtonedRevenant(), entity.getInstance(), entity.getPosition());
                    }
                }
                return true;
            }
            return false;
        }

        private Requirement[] getRequirements(int tier) {
            return switch (tier) {
                case 2 -> new Requirement[]{new SkillRequirement(Skill.Combat, 5)};
                case 3 -> new Requirement[]{new SkillRequirement(Skill.Combat, 10)};
                case 4 -> new Requirement[]{new SkillRequirement(Skill.Combat, 15)};
                case 5 -> new Requirement[]{new SkillRequirement(Skill.Combat, 25), new SlayerRequirement(this, 7)};
                default -> new Requirement[0];
            };
        }

        @Override
        public boolean startSlayerQuest(int tier, SkyblockPlayer player) {
            double coinCost = getCoinCost(tier);
            if (coinCost > player.getCoins()) {
                player.sendMessage("§cNot enough Coins");
                return false;
            }

            for (Requirement r : getRequirements(tier))
                if (!r.canUse(player, null))
                    return false;

            player.setSlayerQuest(new SlayerQuest(player.getSlayers().get(this), tier, getRequiredSlayerQuestXp(tier)));
            player.playSound(SoundType.ENTITY_ENDER_DRAGON_GROWL, Sound.Source.PLAYER, 1, 1);
            return true;
        }


    }, Enderman("Enderman", "Voidgloom Seraph") {
        @Override
        public SlayerEntity getEntity(int tier, SkyblockPlayer player) {
            return switch (tier) {
                case 1 -> new VoidgloomSeraphI(player);
                case 2 -> new VoidgloomSeraphII(player);
                case 3 -> new VoidgloomSeraphIII(player);
                case 4 -> new VoidgloomSeraphIV(player);
                default -> throw new IllegalStateException("Tier " + (tier) + " does not exist!");
            };
        }

        private final int[] xp = new int[]{10, 30, 250, 1_500, 5_000, 20_000, 100_000, 400_000, 1_000_000};

        @Override
        public int requiredXp(int currentLevel) {
            return xp[currentLevel];
        }

        @Override
        public String getTitle(int level) {
            return switch (level) {
                case 1 -> "Noob";
                case 2 -> "Novice";
                case 3 -> "Skilled";
                case 4 -> "Destroyed";
                case 5 -> "Bulldozer";
                case 6 -> "Savage";
                case 7 -> "Voidwracker";
                case 8 -> "Tall Purple Hate";
                default -> "Definition of End";
            };
        }

        @Override
        public boolean addXp(SkyblockEntity entity, int tier) {
            if (entity.getEntityType() == EntityType.ENDERMAN) {
                double random = new Random().nextDouble();
                switch (tier) {
                    case 3 -> {
                        if (random <= 0.1)
                            spawnMiniBoss(new VoidlingDevotee(), entity.getInstance(), entity.getPosition());
                    }
                    case 4 -> {
                        if (random <= 0.1)
                            spawnMiniBoss(new VoidlingRadical(), entity.getInstance(), entity.getPosition());
                        else if (random <= 0.2)
                            spawnMiniBoss(new VoidcrazedManiac(), entity.getInstance(), entity.getPosition());
                    }
                }
                return true;
            }
            return false;
        }

        private static final Map<RngMeterEntry, Double> rngMeterEntries = new HashMap<>();
        private static final Map<RngMeterEntry, Double> lootChancesEntries = new HashMap<>();
        private static final Lazy<SlayerLootTable[]> lootTables = new  Lazy<>(() -> new SlayerLootTable[]{
                VoidgloomSeraphI.getLootTable(new SlayerLootTable(), 1),
                VoidgloomSeraphI.getLootTable(VoidgloomSeraphII.getLootTable(new SlayerLootTable(), 2), 2),
                VoidgloomSeraphI.getLootTable(VoidgloomSeraphII.getLootTable(VoidgloomSeraphIII.getLootTable(new SlayerLootTable(), 3), 3), 3),
                VoidgloomSeraphI.getLootTable(VoidgloomSeraphII.getLootTable(VoidgloomSeraphIII.getLootTable(VoidgloomSeraphIV.getSlayerLootTable(),
                        4), 4), 4)});

        private double rngXp(RngMeterEntry entry) {
            var l = lootTables.get();
            lootChancesEntries.put(entry, entry.calculateHighestChance(l[0],  l[1], l[2], l[3]));
            return entry.calculateRequiredXp(50_000, l[0],  l[1], l[2], l[3]);
        }

        private void addRngMeterEntry(RngMeterEntry entry) {
            rngMeterEntries.put(entry, rngXp(entry));
        }

        @Override
        public SlayerRngMeter createRngMeter(SkyblockPlayer player, ConfigSection section) {
            if (rngMeterEntries.isEmpty()) {
                for (RngMeterEntry entry : getRngMeterEntries())
                    addRngMeterEntry(entry);
            }
            return new SlayerRngMeter(player, this, rngMeterEntries, lootChancesEntries, section);
        }

        private final Lazy<List<RngMeterEntry>> entries = new Lazy<>(() -> List.of(VoidgloomSeraphII.TWILIGHT_ARROW_POISON,
                                                            VoidgloomSeraphIII.ENDERSNAKE_RUNE,
                                                            VoidgloomSeraphII.SUMMONING_EYE,
                                                            VoidgloomSeraphIII.MANA_STEAL,
                                                            VoidgloomSeraphIII.TRANSMITION_TUNER,
                                                            VoidgloomSeraphIII.NULL_ATOM,
                                                            VoidgloomSeraphIII.HAZMAT_ENDERMAN,
                                                            VoidgloomSeraphIV.POCKET_ESPRESSO_MACHINE,
                                                            VoidgloomSeraphIV.SMARTY_PANTS,
                                                            VoidgloomSeraphIV.END_RUNE,
                                                            VoidgloomSeraphIV.HANDY_BLOOD_CHALICE,
                                                            VoidgloomSeraphIV.SINFUL_DICE,
                                                            VoidgloomSeraphIV.UPGRADER,
                                                            VoidgloomSeraphIV.ETHERWARP_MERGER,
                                                            VoidgloomSeraphIV.JUDGEMENT_CORE,
                                                            VoidgloomSeraphIV.ENCHANT_RUNE,
                                                            VoidgloomSeraphIV.ENDER_SLAYER));

        @Override
        public List<RngMeterEntry> getRngMeterEntries() {
            return entries.get();
        }


        private Requirement[] getRequirements(int tier) {
            return new Requirement[0]; //TODO: Sven requirement
        }

        @Override
        public boolean startSlayerQuest(int tier, SkyblockPlayer player) {
            double coinCost = getCoinCost(tier);
            if (coinCost > player.getCoins()) {
                player.sendMessage("§cNot enough Coins");
                return false;
            }

            for (Requirement r : getRequirements(tier))
                if (!r.canUse(player, null))
                    return false;

            player.setSlayerQuest(new SlayerQuest(player.getSlayers().get(this), tier, getRequiredSlayerQuestXp(tier)));
            player.playSound(SoundType.ENTITY_ENDER_DRAGON_GROWL, Sound.Source.PLAYER, 1, 1);
            return true;
        }

        @Override
        public int getRequiredSlayerQuestXp(int tier) {
            return switch (tier) {
                case 1 -> 2_750;
                case 2 -> 6_600;
                case 3 -> 11_000;
                case 4 -> 22_000;
                default -> throw new IllegalStateException("Tier " + (tier) + " does not exist!");
            };
        }
    };
    private final String name;
    private final String mobName;
    private final String id;

    Slayers(String name, String mobName) {
        this.name = name;
        this.id = name.toLowerCase().replace(' ', '_');
        this.mobName = mobName;
    }

    @Override
    public String getId() {
        return id;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMobName() {
        return mobName;
    }

    protected double getCoinCost(int tier) {
        return switch (tier) {
            case 1 -> 2_000;
            case 2 -> 7_500;
            case 3 -> 20_000;
            case 4 -> 50_000;
            case 5 -> 100_000;
            default -> throw new IllegalStateException("Tier " + (tier) + " does not exist!");
        };
    }

    private static void spawnMiniBoss(SkyblockEntity entity, Instance instance, Pos pos) {
        new TaskScheduler() {
            int i = 0;

            @Override
            public void run() {
                ParticleUtils.spawnParticle(instance, pos.add(0, 0.5, 0), Particle.EXPLOSION, 1);
                instance.playSound(SoundType.ENTITY_GENERIC_EXPLODE.create(.5f, 1 + (i / 20f)), pos);
                if (i == 20) {
                    cancel();
                    entity.setInstance(instance, pos);
                }
                i += 1;
            }
        }.repeatTask(0, 1);
    }
}
