package me.carscupcake.sbremake.item.impl.shard;

import java.lang.Override;
import java.lang.String;
import me.carscupcake.sbremake.item.ItemRarity;

/**
 * The Attribute Shards
 */
public enum Shard implements IAttributeShard {
  AccessorySize("accessory_size", "Hideonring", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "R34", "Accessory Size"),

  Almighty("almighty", "Molthorn", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon,ShardFamily.Dragon}, "L42", "Almighty"),

  AnimalExpertise("animal_expertise", "Bambloom", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "R4", "Animal Expertise"),

  ArthropodRuler("arthropod_ruler", "Flaming Spider", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U33", "Arthropod Ruler"),

  ArthropodResistance("arthropod_resistance", "Voracious Spider", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C15", "Arthropod Resistance"),

  AtomizedCrystals("atomized_crystals", "Thyst", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "R31", "Atomized Crystals"),

  AtomizedGlacite("atomized_glacite", "Stalagmight", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "R51", "Atomized Glacite"),

  AtomizedMithril("atomized_mithril", "Star Sentry", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R21", "Atomized Mithril"),

  AttackSpeed("attack_speed", "Burningsoul", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon}, "L27", "Attack Speed"),

  BattleExperience("battle_experience", "Falcon", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "R58", "Battle Experience"),

  BattleFrog("battle_frog", "Rana", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "U39", "Battle Frog"),

  BeaconZealot("beacon_zealot", "Beaconmite", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R7", "Beacon Zealot"),

  BerryEnjoyer("berry_enjoyer", "Bullfrog", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "R46", "Berry Enjoyer"),

  BerryMogul("berry_mogul", "Fenlord", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "E7", "Berry Mogul"),

  BiggerBox("bigger_box", "Hideoncave", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "U7", "Bigger Box"),

  Blazing("blazing", "Flare", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E18", "Blazing"),

  BlazingFortune("blazing_fortune", "Lava Flame", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[0], "R56", "Blazing Fortune"),

  BlazingResistance("blazing_resistance", "Bezal", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C30", "Blazing Resistance"),

  BoneFont("bone_font", "Lapis Skeleton", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Lapis}, "R15", "Bone Font"),

  BookWisdom("book_wisdom", "Lapis Creeper", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L12", "Book Wisdom"),

  Breeze("breeze", "Wither Specter", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R39", "Breeze"),

  BucketLover("bucket_lover", "Coralot", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Axolotl}, "C23", "Bucket Lover"),

  CatacombsBox("catacombs_box", "Hideongeon", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "R13", "Catacombs Box"),

  CavernWisdom("cavern_wisdom", "Cavernshade", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "E36", "Cavern Wisdom"),

  Charmed("charmed", "Naga", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "L11", "Charmed"),

  Cheapstake("cheapstake", "Cod", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.TropicalFish}, "C5", "Cheapstake"),

  CloakImprovement("cloak_improvement", "Tortoise", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "L8", "Cloak Improvement"),

  Combo("combo", "Soul of the Alpha", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U30", "Combo"),

  CookieEater("cookie_eater", "Bitbug", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R25", "Cookie Eater"),

  CrimsonSerendipity("crimson_serendipity", "Inferno Koi", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R59", "Crimson Serendipity"),

  CropBug("crop_bug", "Cropeetle", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R16", "Crop Bug"),

  CrystalSerendipity("crystal_serendipity", "Silentdepth", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R29", "Crystal Serendipity"),

  Deadeye("deadeye", "Skeletor", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R30", "Deadeye"),

  DecentKarma("decent_karma", "Newt", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "C35", "Decent Karma"),

  DeepDiving("deep_diving", "Joydive", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[0], "R50", "Deep Diving"),

  DeepTechnique("deep_technique", "Bal", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "E15", "Deep Technique"),

  Dominance("dominance", "Thorn", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L48", "Dominance"),

  CrimsonHook("crimson_hook", "Lord Jawbus", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L26", "Crimson Hook"),

  EssenceOfDragons("essence_of_dragons", "Draconic", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Dragon}, "R57", "Essence of Dragons"),

  DragonShortbowImprovement("dragon_shortbow_improvement", "Seer", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U15", "Dragon Shortbow Improvement"),

  DwarvenSerendipity("dwarven_serendipity", "Abyssal Lanternfish", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R23", "Dwarven Serendipity"),

  EarthElemental("earth_elemental", "Terra", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "E1", "Earth Elemental"),

  EchoOfAtomized("echo_of_atomized", "Iguana", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "E10", "Echo of Atomized"),

  EchoOfBoxes("echo_of_boxes", "Cuboa", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "U9", "Echo of Boxes"),

  EchoOfEchoes("echo_of_echoes", "Tiamat", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "L6", "Echo of Echoes"),

  EchoOfElemental("echo_of_elemental", "Starborn", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "L44", "Echo of Elemental"),

  EchoOfEssence("echo_of_essence", "Komodo Dragon", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "E22", "Echo of Essence"),

  EchoOfHunter("echo_of_hunter", "Sea Serpent", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel,ShardFamily.Serpent}, "E32", "Echo of Hunter"),

  EchoOfResistance("echo_of_resistance", "Alligator", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "E6", "Echo of Resistance"),

  EchoOfRuler("echo_of_ruler", "Caiman", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "E30", "Echo of Ruler"),

  EchoOfSharpening("echo_of_sharpening", "Gecko", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "R61", "Echo of Sharpening"),

  EchoOfWisdom("echo_of_wisdom", "Wyvern", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Scaled}, "L7", "Echo of Wisdom"),

  Eelastic("eelastic", "Eel", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "R53", "Eelastic"),

  Elite("elite", "Power Dragon", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Dragon}, "L30", "Elite"),

  EnderRuler("ender_ruler", "Bruiser", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U36", "Ender Ruler"),

  EnderResistance("ender_resistance", "Zealot", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C21", "Ender Resistance"),

  ExcellentKarma("excellent_karma", "Leviathan", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "E5", "Excellent Karma"),

  Experience("experience", "Lapis Zombie", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Lapis}, "C9", "Experience"),

  ExtremePressure("extreme_pressure", "Lumisquid", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Squid}, "R44", "Extreme Pressure"),

  Faker("faker", "Mimic", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E24", "Faker"),

  FancyVisit("fancy_visit", "Invisibug", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R10", "Fancy Visit"),

  FigCollector("fig_collector", "Sparrow", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U4", "Fig Collector"),

  FigSharpening("fig_sharpening", "Crow", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "C19", "Fig Sharpening"),

  Fisherman("fisherman", "Night Squid", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Squid}, "C8", "Fisherman"),

  SeaWisdom("sea_wisdom", "Loch Emperor", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[0], "E20", "Sea Wisdom"),

  FishingSpeed("fishing_speed", "Water Hydra", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[0], "E17", "Fishing Speed"),

  FogElemental("fog_elemental", "Mist", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "C2", "Fog Elemental"),

  ForagingWisdom("foraging_wisdom", "Pandarai", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "E4", "Foraging Wisdom"),

  ForestElemental("forest_elemental", "Sylvan", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "R1", "Forest Elemental"),

  EssenceOfTheForest("essence_of_the_forest", "Dreadwing", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Phantom}, "R49", "Essence of the Forest"),

  ForestFishing("forest_fishing", "Verdant", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.TropicalFish}, "C11", "Forest Fishing"),

  ForestStrength("forest_strength", "Azure", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.TropicalFish}, "C29", "Forest Strength"),

  ForestTrap("forest_trap", "Mossybit", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "U11", "Forest Trap"),

  ParamountFortitude("paramount_fortitude", "End Stone Protector", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L9", "Paramount Fortitude"),

  FrogLegs("frog_legs", "Toad", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "R5", "Frog Legs"),

  FrostElemental("frost_elemental", "Cryo", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "E2", "Frost Elemental"),

  FungyLuck("fungy_luck", "Fungloom", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.CaveDweller}, "R52", "Fungy Luck"),

  GardenWisdom("garden_wisdom", "Dragonfly", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "E37", "Garden Wisdom"),

  GoldBait("gold_bait", "Goldfin", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U5", "Gold Bait"),

  GoodKarma("good_karma", "Salamander", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "U8", "Good Karma"),

  GreatKarma("great_karma", "Lizard King", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Lizard,ShardFamily.Reptile}, "R8", "Great Karma"),

  HappyBox("happy_box", "Hideongift", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "C16", "Happy Box"),

  HumanoidRuler("humanoid_ruler", "Drowned", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R18", "Humanoid Ruler"),

  CreatureFisher("creature_fisher", "Bogged", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[0], "C14", "Creature Fisher"),

  HuntersKarma("hunters_karma", "Megalith", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "L29", "Hunter\\'s Karma"),

  HuntersFang("hunters_fang", "Viper", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "U21", "Hunter\\'s Fang"),

  HuntersGrasp("hunters_grasp", "King Cobra", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "R54", "Hunter\\'s Grasp"),

  HuntersPressure("hunters_pressure", "Python", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "R9", "Hunter\\'s Pressure"),

  HuntersSuppress("hunters_suppress", "Basilisk", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Serpent}, "E9", "Hunter\\'s Suppress"),

  HuntWisdom("hunt_wisdom", "Leatherback", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "E35", "Hunt Wisdom"),

  EssenceOfIce("essence_of_ice", "Glacite Walker", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R6", "Essence of Ice"),

  Ignition("ignition", "Matcho", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R42", "Ignition"),

  Infection("infection", "Magma Slug", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U32", "Infection"),

  Infiltration("infiltration", "Termite", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "U40", "Infiltration"),

  InsectPower("insect_power", "Praying Mantis", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "U22", "Insect Power"),

  KatsFavorite("kats_favorite", "Kiwi", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U34", "Kat\\'s Favorite"),

  KuudrasBox("kuudras_box", "Hideondra", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "R22", "Kuudra\\'s Box"),

  Lifeline("lifeline", "Kada Knight", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R36", "Lifeline"),

  LifeRecovery("life_recovery", "Sycophant", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U27", "Life Recovery"),

  HelpFromAbove("help_from_above", "Shinyfish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L23", "Help From Above"),

  LightElemental("light_elemental", "Flash", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "C3", "Light Elemental"),

  LightningElemental("lightning_elemental", "Bolt", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "R3", "Lightning Elemental"),

  LostAndFound("lost_and_found", "Salmon", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U20", "Lost and Found"),

  LuckyRod("lucky_rod", "Moray Eel", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "E11", "Lucky Rod"),

  LunarPower("lunar_power", "Lunar Moth", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "E13", "Lunar Power"),

  MagicFind("magic_find", "Cinderbat", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[0], "L28", "Magic Find"),

  MagmaticRuler("magmatic_ruler", "Stridersurfer", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Drowned}, "U38", "Magmatic Ruler"),

  ManaRegeneration("mana_regeneration", "XYZ", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[0], "E34", "Mana Regeneration"),

  ManaSteal("mana_steal", "Rain Slime", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U12", "Mana Steal"),

  MangroveCollector("mangrove_collector", "Seagull", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U28", "Mangrove Collector"),

  MangroveSharpening("mangrove_sharpening", "Heron", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "U16", "Mangrove Sharpening"),

  MatriarchCubs("matriarch_cubs", "Hellwisp", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E29", "Matriarch Cubs"),

  MaximalTorment("maximal_torment", "Taurus", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L17", "Maximal Torment"),

  Vitality("vitality", "Kraken", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon}, "L15", "Vitality"),

  MidasTouch("midas_touch", "Golden Ghoul", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C27", "Midas Touch"),

  MoongladeMastery("moonglade_mastery", "Phanflare", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Phantom}, "C7", "Moonglade Mastery"),

  MoongladeSerendipity("moonglade_serendipity", "Piranha", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R11", "Moonglade Serendipity"),

  MossyBox("mossy_box", "Hideonleaf", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "C10", "Mossy Box"),

  MountainClimber("mountain_climber", "Troglobyte", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "U6", "Mountain Climber"),

  NatureElemental("nature_elemental", "Grove", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "C1", "Nature Elemental"),

  NocturnalAnimal("nocturnal_animal", "Phanpyre", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Phantom}, "C4", "Nocturnal Animal"),

  OwlFriend("owl_friend", "Boreal Owl", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "C34", "Owl Friend"),

  Payback("payback", "Spike", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L14", "Payback"),

  PestLuck("pest_luck", "Pest", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "U10", "Pest Luck"),

  PetWisdom("pet_wisdom", "Condor", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "L31", "Pet Wisdom"),

  Pity("pity", "Daemon", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon}, "L18", "Pity"),

  PrettyClothes("pretty_clothes", "Ladybug", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "R43", "Pretty Clothes"),

  PureReptile("pure_reptile", "Crocodile", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Croco,ShardFamily.Reptile}, "R45", "Pure Reptile"),

  QuartzSpeed("quartz_speed", "Quartzfang", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "R33", "Quartz Speed"),

  RabbitCrew("rabbit_crew", "Carrot King", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[0], "R38", "Rabbit Crew"),

  RareBird("rare_bird", "Dodo", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "L34", "Rare Bird"),

  Reborn("reborn", "Prince", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E21", "Reborn"),

  Reptiloid("reptiloid", "Chameleon", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Reptile}, "L4", "Reptiloid"),

  RottenPickaxe("rotten_pickaxe", "Miner Zombie", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "C36", "Rotten Pickaxe"),

  SackSize("sack_size", "Hideonsack", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "E16", "Sack Size"),

  ShadowElemental("shadow_elemental", "Tenebris", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "L1", "Shadow Elemental"),

  Shell("shell", "Shellwise", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile,ShardFamily.Turtle}, "E26", "Shell"),

  SkeletalRuler("skeletal_ruler", "Chill", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C12", "Skeletal Ruler"),

  SnowElemental("snow_elemental", "Blizzard", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "L2", "Snow Elemental"),

  SolarPower("solar_power", "Firefly", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "E31", "Solar Power"),

  Speed("speed", "Obsidian Defender", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U18", "Speed"),

  EssenceOfArthropods("essence_of_arthropods", "Arachne", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Spider}, "R24", "Essence of Arthropods"),

  SpiritAxe("spirit_axe", "Ent", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[0], "U29", "Spirit Axe"),

  StarBait("star_bait", "Moltenfish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Demon}, "L20", "Star Bait"),

  Starborn("starborn", "Sun Fish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L32", "Starborn"),

  StoneElemental("stone_elemental", "Quake", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "U3", "Stone Elemental"),

  StormElemental("storm_elemental", "Tempest", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "L3", "Storm Elemental"),

  StrongArms("strong_arms", "Bambuleaf", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "U25", "Strong Arms"),

  StrongLegs("strong_legs", "Mochibear", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Panda}, "U31", "Strong Legs"),

  TorrentElemental("torrent_elemental", "Cascade", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "R2", "Torrent Elemental"),

  TreeLurker("tree_lurker", "Harpy", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Bird}, "C24", "Tree Lurker"),

  TrophyHunter("trophy_hunter", "Fire Eel", ItemRarity.EPIC, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Eel}, "E14", "Trophy Hunter"),

  TuningBox("tuning_box", "Hideonbox", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Shulker}, "L25", "Tuning Box"),

  UltimateDNA("ultimate_dna", "Galaxy Fish", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[0], "L41", "Ultimate DNA"),

  UndeadRuler("undead_ruler", "Zombie Soldier", ItemRarity.UNCOMMON, ShardCategory.Combat, new ShardFamily[0], "U24", "Undead Ruler"),

  EssenceOfUnliving("essence_of_unliving", "Revenant", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R27", "Essence of Unliving"),

  UndeadResistance("undead_resistance", "Tank Zombie", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C18", "Undead Resistance"),

  UnityIsStrength("unity_is_strength", "Tadgang", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Amphibian,ShardFamily.Frog}, "C20", "Unity is Strength"),

  UnlimitedEnergy("unlimited_energy", "Etherdrake", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon,ShardFamily.Dragon}, "L39", "Unlimited Energy"),

  UnlimitedPower("unlimited_power", "Jormung", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Demon,ShardFamily.Dragon}, "L36", "Unlimited Power"),

  Veil("veil", "Ghost", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.CaveDweller}, "E33", "Veil"),

  Veteran("veteran", "Apex Dragon", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Dragon}, "L33", "Veteran"),

  VisitorBait("visitor_bait", "Mudworm", ItemRarity.COMMON, ShardCategory.Forest, new ShardFamily[0], "C25", "Visitor Bait"),

  Warrior("warrior", "Barbarian Duke X", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[0], "E27", "Warrior"),

  WartEater("wart_eater", "Wartybug", ItemRarity.LEGENDARY, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bug}, "L13", "Wart Eater"),

  WaterElemental("water_elemental", "Tide", ItemRarity.UNCOMMON, ShardCategory.Water, new ShardFamily[]{ShardFamily.Elemental}, "U2", "Water Elemental"),

  WhyNotMore("why_not_more", "Toucan", ItemRarity.EPIC, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "E28", "Why Not More"),

  WindElemental("wind_elemental", "Aero", ItemRarity.EPIC, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Elemental}, "E3", "Wind Elemental"),

  WingsOfDestiny("wings_of_destiny", "Ananke", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[]{ShardFamily.Bird,ShardFamily.Demon}, "L24", "Wings of Destiny"),

  WintersSerendipity("winters_serendipity", "Snowfin", ItemRarity.RARE, ShardCategory.Water, new ShardFamily[]{ShardFamily.TreasureFish}, "R35", "Winter\\'s Serendipity"),

  EssenceOfWither("essence_of_wither", "Wither", ItemRarity.RARE, ShardCategory.Combat, new ShardFamily[0], "R60", "Essence of Wither"),

  WoodElemental("wood_elemental", "Bramble", ItemRarity.UNCOMMON, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Elemental}, "U1", "Wood Elemental"),

  YogMembrane("yog_membrane", "Yog", ItemRarity.COMMON, ShardCategory.Combat, new ShardFamily[0], "C33", "Yog Membrane"),

  Yummy("yummy", "Birries", ItemRarity.COMMON, ShardCategory.Water, new ShardFamily[0], "C17", "Yummy"),

  BayouBiter("bayou_biter", "Titanoboa", ItemRarity.LEGENDARY, ShardCategory.Water, new ShardFamily[]{ShardFamily.Reptile}, "L47", "Bayou Biter"),

  Chop("chop", "Hummingbird", ItemRarity.RARE, ShardCategory.Forest, new ShardFamily[]{ShardFamily.Bird}, "R64", "Chop"),

  CatacombsGraduate("catacombs_graduate", "Scarf", ItemRarity.LEGENDARY, ShardCategory.Combat, new ShardFamily[0], "L51", "Catacombs Graduate");

  private final String displayName;

  private final ItemRarity rarity;

  private final String id;

  private final String shardId;

  private final ShardCategory category;

  private final ShardFamily[] families;

  private final String abilityName;

  Shard(String id, String displayName, ItemRarity rarity, ShardCategory category,
      ShardFamily[] families, String shardId, String abilityName) {
    this.id = id;
    this.displayName = displayName;
    this.rarity = rarity;
    this.category = category;
    this.families = families;
    this.shardId = shardId;
    this.abilityName = abilityName;
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
}
