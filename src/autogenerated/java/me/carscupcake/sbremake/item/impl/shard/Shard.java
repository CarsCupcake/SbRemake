package me.carscupcake.sbremake.item.impl.shard;

import java.lang.Override;
import java.lang.String;
import java.util.List;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;

/**
 * The Attribute Shards
 */
public enum Shard implements IAttributeShard {
  AccessorySize("accessory_size", "Hideonring", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "R34", "Accessory Size", new String[]{"§7Grants §a+1 §7extra §aAccessory Bag","§7slots."}),

  Almighty("almighty", "Molthorn", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon,ShardFamily.Dragon}, "L42", "Almighty", new String[]{"§7Your \"Unlimited\" Attributes are §a+5%","§7stronger."}),

  AnimalExpertise("animal_expertise", "Bambloom", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "R4", "Animal Expertise", new String[]{"§7Grants §6+10 ☘ Foraging Fortune§7."}),

  ArthropodRuler("arthropod_ruler", "Flaming Spider", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U33", "Arthropod Ruler", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against §4Ж","§4Arthropod §7mobs."}),

  ArthropodResistance("arthropod_resistance", "Voracious Spider", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C15", "Arthropod Resistance", new String[]{"§7Grants §a+4 ❈ Defense §7against §4Ж","§4Arthropod §7mobs."}),

  AtomizedCrystals("atomized_crystals", "Thyst", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "R31", "Atomized Crystals", new String[]{"§7Gain §a+1% §7extra §dGemstone Powder§7."}),

  AtomizedGlacite("atomized_glacite", "Stalagmight", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "R51", "Atomized Glacite", new String[]{"§7Gain §a+1% §7extra §bGlacite Powder§7."}),

  AtomizedMithril("atomized_mithril", "Star Sentry", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R21", "Atomized Mithril", new String[]{"§7Gain §a+1% §7extra §2Mithril Powder§7."}),

  AttackSpeed("attack_speed", "Burningsoul", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon}, "L27", "Attack Speed", new String[]{"§7Grants §e+1 ⚔ Bonus Attack Speed§7."}),

  BattleExperience("battle_experience", "Falcon", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "R58", "Battle Experience", new String[]{"§7Pets gain §a+1%§7 more EXP from Combat."}),

  BattleFrog("battle_frog", "Rana", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "U39", "Battle Frog", new String[]{"§7Your Frog Pet gains §c+3 ❁ Strength§7."}),

  BeaconZealot("beacon_zealot", "Beaconmite", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R7", "Beacon Zealot", new String[]{"§7The effects of §2Galatea's §7beacons","§7are §a10% §7stronger on you. Gain a §a0.1%","§7chance to find §6Signal Enhancers §7in","§2Tree Gifts§7."}),

  BerryEnjoyer("berry_enjoyer", "Bullfrog", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "R46", "Berry Enjoyer", new String[]{"§7Berries have a §a+10%§7 chance to","§7grant you double points in §dStarlyn","§dContests§7."}),

  BerryMogul("berry_mogul", "Fenlord", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "E7", "Berry Mogul", new String[]{"§7Your Salts last §a+1%§7 longer."}),

  BiggerBox("bigger_box", "Hideoncave", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "U7", "Bigger Box", new String[]{"§7Grants a §a+5%§7 chance to find an","§7extra item in Treasure Chests and","§7the Crystal Nucleus."}),

  Blazing("blazing", "Flare", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E18", "Blazing", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against","§4♨ Infernal §7mobs."}),

  BlazingFortune("blazing_fortune", "Lava Flame", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[0], "R56", "Blazing Fortune", new String[]{"§7Grants §b+1 ✯ Magic Find §7on §c♆","§cMagmatic §7mobs."}),

  BlazingResistance("blazing_resistance", "Bezal", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C30", "Blazing Resistance", new String[]{"§7Grants §a+4 ❈ Defense §7against §4♨","§4Infernal §7mobs."}),

  BoneFont("bone_font", "Lapis Skeleton", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Lapis}, "R15", "Bone Font", new String[]{"§7Grants a §a+2% §7extra chance of","§7finding §aLevel 6+ §7books or higher in","§dSuperpairs§7."}),

  BookWisdom("book_wisdom", "Lapis Creeper", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L12", "Book Wisdom", new String[]{"§7Grants §3+0.5 ☯ Enchanting Wisdom§7."}),

  Breeze("breeze", "Wither Specter", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R39", "Breeze", new String[]{"§7When you haven't been damaged in","§7the last §a5s §7or more, your mana","§7costs are decreased by §a+1%§7."}),

  BucketLover("bucket_lover", "Coralot", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Axolotl}, "C23", "Bucket Lover", new String[]{"§7You have a §a+5% §7chance to gain extra","§7Chums."}),

  CatacombsBox("catacombs_box", "Hideongeon", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "R13", "Catacombs Box", new String[]{"§7Increases the quality of boss","§7rewards in §cThe Catacombs §7by §a+1§7."}),

  CavernWisdom("cavern_wisdom", "Cavernshade", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "E36", "Cavern Wisdom", new String[]{"§7Grants §3+0.5 ☯ Mining Wisdom§7."}),

  Charmed("charmed", "Naga", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "L11", "Charmed", new String[]{"§7Grants a §a+0.05% §7chance to charm the","§9Shard §7of monsters you kill."}),

  Cheapstake("cheapstake", "Cod", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.TropicalFish}, "C5", "Cheapstake", new String[]{"§7Gain §a+1% §7more §6Coins §7from fishing","§7treasures."}),

  CloakImprovement("cloak_improvement", "Tortoise", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "L8", "Cloak Improvement", new String[]{"§7Your §fDavid's Cloak §7gains §f+1 ❂ True","§fDefense§7."}),

  Combo("combo", "Soul of the Alpha", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U30", "Combo", new String[]{"§7Deal §a+0.1% §7more §c❁ Damage §7per","§7Grandma Wolf pet combo. §8(Max 50","§8combo)"}),

  CookieEater("cookie_eater", "Bitbug", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R25", "Cookie Eater", new String[]{"§a5%§7 of your Booster Cookie §bBits §7are","§7gained up front."}),

  CrimsonSerendipity("crimson_serendipity", "Inferno Koi", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R59", "Crimson Serendipity", new String[]{"§7Increase the odds of finding a","§5Vanquisher §7by §a+2%§7."}),

  CropBug("crop_bug", "Cropeetle", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R16", "Crop Bug", new String[]{"§7Increased the odds of finding a","§7Cropie, Squash or Fermento by §a+2%§7."}),

  CrystalSerendipity("crystal_serendipity", "Silentdepth", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R29", "Crystal Serendipity", new String[]{"§7Grants a §a+2%§7 chance to double any","§7loot from §5Crystal Hollows §7Treasure","§7Chests."}),

  Deadeye("deadeye", "Skeletor", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R30", "Deadeye", new String[]{"§7Increases damage from ranged","§7weapons by §a+2.5%§7."}),

  DecentKarma("decent_karma", "Newt", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "C35", "Decent Karma", new String[]{"§7Grants §d+2 ☘ Hunter Fortune","§7towards §f§lCOMMON §7Attributes."}),

  DeepDiving("deep_diving", "Joydive", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[0], "R50", "Deep Diving", new String[]{"§7Grants §3+5 ⚶ Respiration§7."}),

  DeepTechnique("deep_technique", "Bal", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "E15", "Deep Technique", new String[]{"§7Each block you mine has a §a+1%","§7chance to grant 1 extra §5✧ Pristine","§7for that block."}),

  Dominance("dominance", "Thorn", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L48", "Dominance", new String[]{"§7Gain §a+1.5% §7more §c❁ Damage §7when at","§7full health."}),

  CrimsonHook("crimson_hook", "Lord Jawbus", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L26", "Crimson Hook", new String[]{"§7Grants §9+0.5 ⚓ Double Hook Chance","§7while on the §cCrimson Isle§7."}),

  EssenceOfDragons("essence_of_dragons", "Draconic", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Dragon}, "R57", "Essence of Dragons", new String[]{"§7Gain §a+1% §7more §5Dragon Essence§7."}),

  DragonShortbowImprovement("dragon_shortbow_improvement", "Seer", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U15", "Dragon Shortbow Improvement", new String[]{"§7Your §5Dragon Shortbow §7gains §a+2%","§7more §c❁ Damage §7while in §dThe End§7."}),

  DwarvenSerendipity("dwarven_serendipity", "Abyssal Lanternfish", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R23", "Dwarven Serendipity", new String[]{"§7Increases the chance to find Golden","§7and Diamond Goblins by §a+10%§7."}),

  EarthElemental("earth_elemental", "Terra", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "E1", "Earth Elemental", new String[]{"§7Grants §c+2 ❤ Health§7."}),

  EchoOfAtomized("echo_of_atomized", "Iguana", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "E10", "Echo of Atomized", new String[]{"§7Your Atomized Attributes are §a+2%","§7stronger."}),

  EchoOfBoxes("echo_of_boxes", "Cuboa", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "U9", "Echo of Boxes", new String[]{"§7Your \"Box\" Attributes are §a+2%","§7stronger."}),

  EchoOfEchoes("echo_of_echoes", "Tiamat", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "L6", "Echo of Echoes", new String[]{"§7Your Echo Attributes are §a+5%","§7stronger."}),

  EchoOfElemental("echo_of_elemental", "Starborn", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "L44", "Echo of Elemental", new String[]{"§7Buffs all other shards in the","§aElemental §7Family by §a+2%§7."}),

  EchoOfEssence("echo_of_essence", "Komodo Dragon", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "E22", "Echo of Essence", new String[]{"§7Your Essence Attributes are §a+2%","§7stronger."}),

  EchoOfHunter("echo_of_hunter", "Sea Serpent", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel,ShardFamily.Serpent}, "E32", "Echo of Hunter", new String[]{"§7Your Hunter Attributes are §a+2%","§7stronger."}),

  EchoOfResistance("echo_of_resistance", "Alligator", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "E6", "Echo of Resistance", new String[]{"§7Your Resistance Attributes are §a+2%","§7stronger."}),

  EchoOfRuler("echo_of_ruler", "Caiman", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "E30", "Echo of Ruler", new String[]{"§7Your Ruler Attributes are §a+2%","§7stronger."}),

  EchoOfSharpening("echo_of_sharpening", "Gecko", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "R61", "Echo of Sharpening", new String[]{"§7Your Sharpening Attributes are §a+2%","§7stronger."}),

  EchoOfWisdom("echo_of_wisdom", "Wyvern", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "L7", "Echo of Wisdom", new String[]{"§7Your Wisdom Attributes are §a+2%","§7stronger."}),

  Eelastic("eelastic", "Eel", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "R53", "Eelastic", new String[]{"§7Increase Pull by §b+1 §7when using your","§7Fishing Nets."}),

  Elite("elite", "Power Dragon", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Dragon}, "L30", "Elite", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against","§abosses §7and §amini-bosses§7."}),

  EnderRuler("ender_ruler", "Bruiser", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U36", "Ender Ruler", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against","§5⊙ Ender §7mobs."}),

  EnderResistance("ender_resistance", "Zealot", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C21", "Ender Resistance", new String[]{"§7Grants §a+4 ❈ Defense §7against §5⊙","§5Ender §7mobs."}),

  ExcellentKarma("excellent_karma", "Leviathan", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "E5", "Excellent Karma", new String[]{"§7Grants §d+1 ☘ Hunter Fortune","§7towards §5§lEPIC §7Attributes."}),

  Experience("experience", "Lapis Zombie", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Lapis}, "C9", "Experience", new String[]{"§7Grants §a+10% §7more experience orbs","§7from killing mobs."}),

  ExtremePressure("extreme_pressure", "Lumisquid", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Squid}, "R44", "Extreme Pressure", new String[]{"§7Grants §9+5 ❍ Pressure Resistance§7."}),

  Faker("faker", "Mimic", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E24", "Faker", new String[]{"§7Dungeon Blessings are §a+1% §7stronger","§7on you."}),

  FancyVisit("fancy_visit", "Invisibug", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R10", "Fancy Visit", new String[]{"§9§lRARE §7and above §aGarden Visitors","§7have a §a+1% §7higher chance to arrive."}),

  FigCollector("fig_collector", "Sparrow", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U4", "Fig Collector", new String[]{"§7Grants §6+1 ☘ Fig Fortune§7 for every","§7digit in your Fig Log collection. §8(Max","§810M collection)"}),

  FigSharpening("fig_sharpening", "Crow", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "C19", "Fig Sharpening", new String[]{"§7Grants §2+5 ∮ Sweep §7towards Fig","§7Trees."}),

  Fisherman("fisherman", "Night Squid", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Squid}, "C8", "Fisherman", new String[]{"§7Has a §a+1% §7chance not to consume","§7any bait."}),

  SeaWisdom("sea_wisdom", "Loch Emperor", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[0], "E20", "Sea Wisdom", new String[]{"§7Grants §3+0.5 ☯ Fishing Wisdom§7."}),

  FishingSpeed("fishing_speed", "Water Hydra", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[0], "E17", "Fishing Speed", new String[]{"§7Grants §b+3 ☂ Fishing Speed§7."}),

  FogElemental("fog_elemental", "Mist", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "C2", "Fog Elemental", new String[]{"§7Grants §b+1 ✎ Intelligence§7."}),

  ForagingWisdom("foraging_wisdom", "Pandarai", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "E4", "Foraging Wisdom", new String[]{"§7Grants §3+0.5 ☯ Foraging Wisdom§7."}),

  ForestElemental("forest_elemental", "Sylvan", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "R1", "Forest Elemental", new String[]{"§7Grants §c+2 ❤ Health§7."}),

  EssenceOfTheForest("essence_of_the_forest", "Dreadwing", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Phantom}, "R49", "Essence of the Forest", new String[]{"§7Gain §a+1% §7more §2Forest Essence§7."}),

  ForestFishing("forest_fishing", "Verdant", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.TropicalFish}, "C11", "Forest Fishing", new String[]{"§7Grants §b+3 ☂ Fishing Speed§7 while on","§2Foraging Islands§7."}),

  ForestStrength("forest_strength", "Azure", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.TropicalFish}, "C29", "Forest Strength", new String[]{"§7Grants §c+5 ❁ Strength§7 while on","§2Foraging Islands§7."}),

  ForestTrap("forest_trap", "Mossybit", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "U11", "Forest Trap", new String[]{"§7Forest Shards get caught by Traps","§a+1%§7 faster."}),

  ParamountFortitude("paramount_fortitude", "End Stone Protector", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L9", "Paramount Fortitude", new String[]{"§7Your §a❈ Defense §7is increased by","§a+0.2%§7."}),

  FrogLegs("frog_legs", "Toad", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "R5", "Frog Legs", new String[]{"§7Frogs have a §a+10% §7chance to jump 1","§7less time."}),

  FrostElemental("frost_elemental", "Cryo", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "E2", "Frost Elemental", new String[]{"§7Grants §b+1 ✎ Intelligence§7."}),

  FungyLuck("fungy_luck", "Fungloom", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.CaveDweller}, "R52", "Fungy Luck", new String[]{"§7Gain a §a+10% §7chance to gain an","§7additional Chisel §echarge §7upon","§7starting the §6Fossil Excavator§7."}),

  GardenWisdom("garden_wisdom", "Dragonfly", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "E37", "Garden Wisdom", new String[]{"§7Grants §3+0.5 ☯ Farming Wisdom§7."}),

  GoldBait("gold_bait", "Goldfin", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U5", "Gold Bait", new String[]{"§7The cooldown of Golden Fish is","§7reduced by §a+30s§7."}),

  GoodKarma("good_karma", "Salamander", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "U8", "Good Karma", new String[]{"§7Grants §d+2 ☘ Hunter Fortune","§7towards §a§lUNCOMMON §7Attributes."}),

  GreatKarma("great_karma", "Lizard King", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "R8", "Great Karma", new String[]{"§7Grants §d+1 ☘ Hunter Fortune","§7towards §9§lRARE §7Attributes."}),

  HappyBox("happy_box", "Hideongift", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "C16", "Happy Box", new String[]{"§7Grants a §a+2% §7chance to obtain an","§7extra §fWinter Gift §7when finding one."}),

  HumanoidRuler("humanoid_ruler", "Drowned", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R18", "Humanoid Ruler", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against","§e✰ Humanoid §7mobs."}),

  CreatureFisher("creature_fisher", "Bogged", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[0], "C14", "Creature Fisher", new String[]{"§7Grants §3+0.5 α Sea Creature Chance§7."}),

  HuntersKarma("hunters_karma", "Megalith", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "L29", "Hunter\\'s Karma", new String[]{"§7Grants §d+1 ☘ Hunter Fortune§7."}),

  HuntersFang("hunters_fang", "Viper", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "U21", "Hunter\\'s Fang", new String[]{"§7Your Hunting Axes gain §a+1% §7more","§7stats from the weapon they hold."}),

  HuntersGrasp("hunters_grasp", "King Cobra", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "R54", "Hunter\\'s Grasp", new String[]{"§7Your Black Holes gain §d+1% ☘ Hunter","§dFortune§7."}),

  HuntersPressure("hunters_pressure", "Python", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "R9", "Hunter\\'s Pressure", new String[]{"§7Your Black Holes capture creatures","§a+5% §7faster."}),

  HuntersSuppress("hunters_suppress", "Basilisk", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "E9", "Hunter\\'s Suppress", new String[]{"§7Lassos deplete Stamina §a+2% §7faster."}),

  HuntWisdom("hunt_wisdom", "Leatherback", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "E35", "Hunt Wisdom", new String[]{"§7Grants §3+0.5 ☯ Hunting Wisdom§7."}),

  EssenceOfIce("essence_of_ice", "Glacite Walker", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R6", "Essence of Ice", new String[]{"§7Gain §a+1% §7more §bIce Essence§7."}),

  Ignition("ignition", "Matcho", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R42", "Ignition", new String[]{"§7Increases the duration of fire","§7damage that you inflict on enemies by","§a+30%§7."}),

  Infection("infection", "Magma Slug", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U32", "Infection", new String[]{"§7Increases the chance of catching","§7corrupted Sea Creatures by §a+0.2%§7."}),

  Infiltration("infiltration", "Termite", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "U40", "Infiltration", new String[]{"§7Grants §6+3 ☘ Farming Fortune§7 if","§7your Garden Plot currently has a §2ൠ","§2Pest §7in it."}),

  InsectPower("insect_power", "Praying Mantis", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "U22", "Insect Power", new String[]{"§7Your Vacuum deals §a+10% §7extra §c❁","§cDamage§7."}),

  KatsFavorite("kats_favorite", "Kiwi", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U34", "Kat\\'s Favorite", new String[]{"§bKat §7takes §a+1%§7 less time to upgrade a","§7Pet."}),

  KuudrasBox("kuudras_box", "Hideondra", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "R22", "Kuudra\\'s Box", new String[]{"§7Gives §a+5%§7 chance to have an extra","§5Kuudra Teeth §7in the Paid Reward","§7Chest."}),

  Lifeline("lifeline", "Kada Knight", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R36", "Lifeline", new String[]{"§7When under §c20% §7maximum HP, your §c❁","§cDamage §7and §a❈ Defense §7increase by","§a+2.5%§7."}),

  LifeRecovery("life_recovery", "Sycophant", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U27", "Life Recovery", new String[]{"§7Heals for §a+0.1% §7of your max health","§7each time you kill a mob."}),

  HelpFromAbove("help_from_above", "Shinyfish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L23", "Help From Above", new String[]{"§7Grants §c+1.25 ❣ Health Regen§7."}),

  LightElemental("light_elemental", "Flash", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "C3", "Light Elemental", new String[]{"§7Grants §c+1 ❁ Strength§7."}),

  LightningElemental("lightning_elemental", "Bolt", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "R3", "Lightning Elemental", new String[]{"§7Grants §c+1 ❁ Strength§7."}),

  LostAndFound("lost_and_found", "Salmon", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U20", "Lost and Found", new String[]{"§7Grants a §a+0.1% §7chance to find a §5Wet","§5Book§7 when fishing a Treasure."}),

  LuckyRod("lucky_rod", "Moray Eel", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "E11", "Lucky Rod", new String[]{"§7Fish Treasure Shards are §a+1%","§7easier to find."}),

  LunarPower("lunar_power", "Lunar Moth", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "E13", "Lunar Power", new String[]{"§7Grants §6+5 ☘ Farming Fortune§7 during","§7the Night."}),

  MagicFind("magic_find", "Cinderbat", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[0], "L28", "Magic Find", new String[]{"§7Grants §b+0.5 ✯ Magic Find§7."}),

  MagmaticRuler("magmatic_ruler", "Stridersurfer", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Drowned}, "U38", "Magmatic Ruler", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against","§c♆ Magmatic §7mobs."}),

  ManaRegeneration("mana_regeneration", "XYZ", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[0], "E34", "Mana Regeneration", new String[]{"§7Increases your Mana regeneration","§7by §a+1%§7."}),

  ManaSteal("mana_steal", "Rain Slime", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U12", "Mana Steal", new String[]{"§7Regain §a+0.5 §7Mana every time you hit a","§7monster."}),

  MangroveCollector("mangrove_collector", "Seagull", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U28", "Mangrove Collector", new String[]{"§7Grants §6+1 ☘ Mangrove Fortune§7 for","§7every digit in your Mangrove Log","§7collection. §8(Max 10M collection)"}),

  MangroveSharpening("mangrove_sharpening", "Heron", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U16", "Mangrove Sharpening", new String[]{"§7Grants §2+10 ∮ Sweep §7towards","§7Mangrove Trees."}),

  MatriarchCubs("matriarch_cubs", "Hellwisp", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E29", "Matriarch Cubs", new String[]{"§7Grants a §a+2% §7chance to get a","§7second §6Heavy Pearl§7."}),

  MaximalTorment("maximal_torment", "Taurus", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L17", "Maximal Torment", new String[]{"§7Your §b✎ Intelligence §7is increased by","§b+0.1%§7."}),

  Vitality("vitality", "Kraken", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon}, "L15", "Vitality", new String[]{"§7Grants §4+2 ♨ Vitality§7."}),

  MidasTouch("midas_touch", "Golden Ghoul", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C27", "Midas Touch", new String[]{"§7Grants §6+1 Coin §7per kill."}),

  MoongladeMastery("moonglade_mastery", "Phanflare", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Phantom}, "C7", "Moonglade Mastery", new String[]{"§7Grants §6+10 ☘ Fig Fortune §7and §6+10","§6☘ Mangrove Fortune§7."}),

  MoongladeSerendipity("moonglade_serendipity", "Piranha", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R11", "Moonglade Serendipity", new String[]{"§7Increases the odds of finding rare","§7items in §2Tree Gifts §7at §2Moonglade","§2Marsh §7by §a+2%§7."}),

  MossyBox("mossy_box", "Hideonleaf", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "C10", "Mossy Box", new String[]{"§7Grants §6+5 ☘ Block Fortune§7."}),

  MountainClimber("mountain_climber", "Troglobyte", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "U6", "Mountain Climber", new String[]{"§7Commissions grant §a+1% §7more §5HOTM Exp§7."}),

  NatureElemental("nature_elemental", "Grove", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "C1", "Nature Elemental", new String[]{"§7Grants §c+2 ❤ Health§7."}),

  NocturnalAnimal("nocturnal_animal", "Phanpyre", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Phantom}, "C4", "Nocturnal Animal", new String[]{"§7Grants §2+1 ∮ Sweep§7 during the night."}),

  OwlFriend("owl_friend", "Boreal Owl", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "C34", "Owl Friend", new String[]{"§7Your pets gain §a+2%§7 more Pet EXP","§7from Fann."}),

  Payback("payback", "Spike", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L14", "Payback", new String[]{"§7When hit, your next hit deals §a+2%","§7damage"}),

  PestLuck("pest_luck", "Pest", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "U10", "Pest Luck", new String[]{"§7Gain a §a+2% §7chance to obtain §9rare","§9loot §7from §2ൠ Pests§7."}),

  PetWisdom("pet_wisdom", "Condor", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "L31", "Pet Wisdom", new String[]{"§7Grants §3+0.5 ☯ Taming Wisdom§7."}),

  Pity("pity", "Daemon", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon}, "L18", "Pity", new String[]{"§7Your §dRNG Meter §7fills §a+1% §7faster."}),

  PrettyClothes("pretty_clothes", "Ladybug", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R43", "Pretty Clothes", new String[]{"§7Gain §a+1%§7 more §cCopper §7from §aGarden","§aVisitors§7."}),

  PureReptile("pure_reptile", "Crocodile", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "R45", "Pure Reptile", new String[]{"§7Grants a §a+2% §7chance to double","§7shards from a Reptile Fusion."}),

  QuartzSpeed("quartz_speed", "Quartzfang", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "R33", "Quartz Speed", new String[]{"§6⸕ Mining Speed §7from events is","§7increased by §a+10%§7."}),

  RabbitCrew("rabbit_crew", "Carrot King", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[0], "R38", "Rabbit Crew", new String[]{"§7Your §6Chocolate Factory §7gains §a+2","§7more §6Chocolate §7per second."}),

  RareBird("rare_bird", "Dodo", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "L34", "Rare Bird", new String[]{"§7Grants §d+1 ♣ Pet Luck§7."}),

  Reborn("reborn", "Prince", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E21", "Reborn", new String[]{"§7Killing a Prince has a §a+10%§7 chance to","§7add 1 Bonus Score to your Dungeon","§7Run."}),

  Reptiloid("reptiloid", "Chameleon", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile}, "L4", "Reptiloid", new String[]{"§7Fusing a Chameleon with a Shard","§7grants the next 3 Shards from that","§7Shard’s ID line as a result."}),

  RottenPickaxe("rotten_pickaxe", "Miner Zombie", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "C36", "Rotten Pickaxe", new String[]{"§7Grants §6+3 ⸕ Mining Speed§7."}),

  SackSize("sack_size", "Hideonsack", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "E16", "Sack Size", new String[]{"§7Your sacks can hold §a+1% §7more items."}),

  ShadowElemental("shadow_elemental", "Tenebris", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "L1", "Shadow Elemental", new String[]{"§7Grants §c+2 ❤ Health§7."}),

  Shell("shell", "Shellwise", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "E26", "Shell", new String[]{"§7Gain a §a+10%§7 chance to negate","§7knockback."}),

  SkeletalRuler("skeletal_ruler", "Chill", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C12", "Skeletal Ruler", new String[]{"§7Increase damage to §f🦴 Skeletal","§7mobs by §a+3%§7."}),

  SnowElemental("snow_elemental", "Blizzard", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "L2", "Snow Elemental", new String[]{"§7Grants §b+1 ✎ Intelligence§7."}),

  SolarPower("solar_power", "Firefly", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "E31", "Solar Power", new String[]{"§7Grants §6+5 ☘ Farming Fortune§7 during","§7the day."}),

  Speed("speed", "Obsidian Defender", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U18", "Speed", new String[]{"§7Grants §f+1 ✦ Speed§7."}),

  EssenceOfArthropods("essence_of_arthropods", "Arachne", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Spider}, "R24", "Essence of Arthropods", new String[]{"§7Gain §a+1% §7more §5Spider Essence§7."}),

  SpiritAxe("spirit_axe", "Ent", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U29", "Spirit Axe", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against","§2⸙ Woodland §7mobs."}),

  StarBait("star_bait", "Moltenfish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Demon}, "L20", "Star Bait", new String[]{"§7Grants a §a+0.1% §7chance to fish a","§7Shinyfish."}),

  Starborn("starborn", "Sun Fish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L32", "Starborn", new String[]{"§7The effect of §dStarborn Temples §7is","§a+1% §7stronger on you."}),

  StoneElemental("stone_elemental", "Quake", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "U3", "Stone Elemental", new String[]{"§7Grants §c+1 ❁ Strength§7."}),

  StormElemental("storm_elemental", "Tempest", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "L3", "Storm Elemental", new String[]{"§7Grants §c+1 ❁ Strength§7."}),

  StrongArms("strong_arms", "Bambuleaf", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "U25", "Strong Arms", new String[]{"§7Your Axe gains §2+3 ∮ Sweep§7 on","§7throws."}),

  StrongLegs("strong_legs", "Mochibear", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "U31", "Strong Legs", new String[]{"§7Your Axe gains §2+3 ∮ Sweep§7 on melee."}),

  TorrentElemental("torrent_elemental", "Cascade", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "R2", "Torrent Elemental", new String[]{"§7Grants §b+1 ✎ Intelligence§7."}),

  TreeLurker("tree_lurker", "Harpy", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Bird}, "C24", "Tree Lurker", new String[]{"§7Increases the odds of finding","§7monsters from §2Tree Gifts §7by §a+5%§7."}),

  TrophyHunter("trophy_hunter", "Fire Eel", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "E14", "Trophy Hunter", new String[]{"§7Grants §6+2 ♔ Trophy Fish Chance§7."}),

  TuningBox("tuning_box", "Hideonbox", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "L25", "Tuning Box", new String[]{"§7Grants §a+1 §7Tuning Points."}),

  UltimateDNA("ultimate_dna", "Galaxy Fish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L41", "Ultimate DNA", new String[]{"§7Grants §6+1 ☘ Mining Fortune§7, §6☘","§6Farming Fortune§7, and §6☘ Foraging","§6Fortune§7."}),

  UndeadRuler("undead_ruler", "Zombie Soldier", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U24", "Undead Ruler", new String[]{"§7Grants §a+3% §7more §c❁ Damage §7against","§2༕ Undead §7mobs."}),

  EssenceOfUnliving("essence_of_unliving", "Revenant", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R27", "Essence of Unliving", new String[]{"§7Gain §a+1% §7more §4Undead Essence§7."}),

  UndeadResistance("undead_resistance", "Tank Zombie", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C18", "Undead Resistance", new String[]{"§7Grants §a+4 ❈ Defense §7against §2༕","§2Undead §7mobs."}),

  UnityIsStrength("unity_is_strength", "Tadgang", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "C20", "Unity is Strength", new String[]{"§7Grants §2+0.02 ∮ Sweep §7for each","§7unique §f§lCommon §7Attribute you own."}),

  UnlimitedEnergy("unlimited_energy", "Etherdrake", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon,ShardFamily.Dragon}, "L39", "Unlimited Energy", new String[]{"§7Your §9☠ Crit Damage §7is increased by","§9+0.1%§7."}),

  UnlimitedPower("unlimited_power", "Jormung", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon,ShardFamily.Dragon}, "L36", "Unlimited Power", new String[]{"§7Your §c❁ Strength §7is increased by","§c+0.1%§7."}),

  Veil("veil", "Ghost", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "E33", "Veil", new String[]{"§7Grants §f+1 ❂ True Defense§7."}),

  Veteran("veteran", "Apex Dragon", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Dragon}, "L33", "Veteran", new String[]{"§7Grants §3+1 ☯ Combat Wisdom§7."}),

  VisitorBait("visitor_bait", "Mudworm", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[0], "C25", "Visitor Bait", new String[]{"§aGarden Visitors §7arrive §a+1% §7faster."}),

  Warrior("warrior", "Barbarian Duke X", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E27", "Warrior", new String[]{"§7Increases melee damage dealt by","§a+2.5%§7."}),

  WartEater("wart_eater", "Wartybug", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "L13", "Wart Eater", new String[]{"§7Grants a §a+0.01% §7chance to obtain a","§5Warty §7when breaking Nether Warts."}),

  WaterElemental("water_elemental", "Tide", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "U2", "Water Elemental", new String[]{"§7Grants §b+1 ✎ Intelligence§7."}),

  WhyNotMore("why_not_more", "Toucan", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "E28", "Why Not More", new String[]{"§7Your pet's EXP Share grants §a+1%","§7more EXP."}),

  WindElemental("wind_elemental", "Aero", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "E3", "Wind Elemental", new String[]{"§7Grants §c+1 ❁ Strength§7."}),

  WingsOfDestiny("wings_of_destiny", "Ananke", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Bird,ShardFamily.Demon}, "L24", "Wings of Destiny", new String[]{"§7Increases the odds of finding","§9Ananke Feathers §7by §a+2%§7."}),

  WintersSerendipity("winters_serendipity", "Snowfin", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R35", "Winter\\'s Serendipity", new String[]{"§7Increases the odds of finding rare","§7items in §fWinter Gifts §7by §a+2%§7."}),

  EssenceOfWither("essence_of_wither", "Wither", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R60", "Essence of Wither", new String[]{"§7Gain §a+1% §7more §8Wither Essence§7."}),

  WoodElemental("wood_elemental", "Bramble", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "U1", "Wood Elemental", new String[]{"§7Grants §c+2 ❤ Health§7."}),

  YogMembrane("yog_membrane", "Yog", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C33", "Yog Membrane", new String[]{"§7Grants §c+1 ♨ Heat Resistance§7."}),

  Yummy("yummy", "Birries", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[0], "C17", "Yummy", new String[]{"§7Grants a §a+10%§7 chance to drop","§7double Berries and Sea Lumies."}),

  BayouBiter("bayou_biter", "Titanoboa", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile}, "L47", "Bayou Biter", new String[]{"§7Grants §9+0.5 ⚓ Double Hook Chance","§7while on the §2Backwater Bayou§7."}),

  Chop("chop", "Hummingbird", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "R64", "Chop", new String[]{"§7Grants a §a+10% §7chance to Chop §aan","§aextra log§7, on top of the ones you","§7swept."}),

  CatacombsGraduate("catacombs_graduate", "Scarf", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L51", "Catacombs Graduate", new String[]{"§7Gain §a+2% §7more §cDungeons §7class","§7experience."});

  private final String displayName;

  private final ItemRarity rarity;

  private final String id;

  private final String shardId;

  private final ShardCategory category;

  private final ShardFamily[] families;

  private final String abilityName;

  private final Lore lore;

  Shard(String id, String displayName, ItemRarity rarity, ShardCategory category,
      ShardFamily[] families, String shardId, String abilityName, String[] loreRows) {
    this.id = id;
    this.displayName = displayName;
    this.rarity = rarity;
    this.category = category;
    this.families = families;
    this.shardId = shardId;
    this.abilityName = abilityName;
    this.lore = new Lore(List.of(loreRows));
  }

  @Override
  public String getDisplayName() {
    return this.displayName;
  }

  @Override
  public ItemRarity getRarity() {
    return this.rarity;
  }

  @Override
  public String getShardId() {
    return this.shardId;
  }

  @Override
  public String getAbilityName() {
    return this.abilityName;
  }

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public ShardCategory getCategory() {
    return this.category;
  }

  @Override
  public ShardFamily[] getFamilies() {
    return this.families;
  }

  @Override
  public Lore getLore() {
    return this.lore;
  }
}
