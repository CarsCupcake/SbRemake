package me.carscupcake.sbremake.player.potion;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.event.ManaRegenEvent;
import me.carscupcake.sbremake.event.PlayerProjectileDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.player.protocol.SetEntityEffectPacket;
import me.carscupcake.sbremake.util.StringUtils;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.color.Color;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.network.packet.server.play.RemoveEntityEffectPacket;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public enum Potion implements IPotion {
    Absorption("Absorption", "§6", new Color(16750848), new PotionLore("§7Grants §a+%b% §7of absorbtion.", Map.of("%b%", level -> {
        if (level < 6) return String.valueOf(level * 20);
        return switch (level) {
            case 6 -> "150";
            case 7 -> "200";
            default -> String.valueOf((level - 2) * 50);
        };
    })), 8) {
        @Override
        public void start(SkyblockPlayer player, int level, long duration) {
            super.start(player, level, duration);
            player.addAbsorption(buff(level));
        }

        @Override
        public void stop(SkyblockPlayer player, int level) {
            super.stop(player, level);
            player.setAbsorption(0);
        }

        public int buff(int level) {
            if (level < 6) return level * 20;
            return switch (level) {
                case 6 -> 150;
                case 7 -> 200;
                default -> (level - 2) * 50;
            };
        }
    }, Adrenaline("Adrenaline", "§c", new Color(13458603), new PotionLore("§7Grants §a+%b% §7of §6❤ Absorption§7 and §a+%s% " + (Stat.Speed.toString()) + "§7.", Map.of("%b%", level -> {
        if (level < 6) return String.valueOf(level * 20);
        return switch (level) {
            case 6 -> "150";
            case 7 -> "200";
            default -> String.valueOf((level - 2) * 50);
        };
    }, "%s%", integer -> String.valueOf(integer * 5))), 8) {
        @Override
        public void start(SkyblockPlayer player, int level, long duration) {
            super.start(player, level, duration);
            player.addAbsorption(buff(level));
        }

        @Override
        public void stop(SkyblockPlayer player, int level) {
            super.stop(player, level);
            player.setAbsorption(0);
        }

        public int buff(int level) {
            if (level < 6) return level * 20;
            return switch (level) {
                case 6 -> 150;
                case 7 -> 200;
                default -> (level - 2) * 50;
            };
        }

        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.Speed, new PlayerStatEvent.BasicModifier("Adrenaline", level * 5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Agility("Agility", "§5", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.Speed) + "§7 and increases the chance for mob attacks to miss by §a%s%%§7.", Map.of("%b%", level -> String.valueOf(level * 10), "%s%", integer -> String.valueOf(integer * 10))), 4) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.Speed, new PlayerStatEvent.BasicModifier(name(), level * 10, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, AlchemyXPBoost("Alchemy XP Boost", "§a", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.AlchemyWisdom) + "§7.", Map.of("%b%", level -> String.valueOf(level == 1 ? 5 : ((level - 1) * 10)))), 3) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.AlchemyWisdom, new PlayerStatEvent.BasicModifier(name(), level == 1 ? 5 : ((level - 1) * 10), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, CombatXPBoost("Combat XP Boost", "§a", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.CombatWisdom) + "§7.", Map.of("%b%", level -> String.valueOf(level == 1 ? 5 : ((level - 1) * 10)))), 3) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.CombatWisdom, new PlayerStatEvent.BasicModifier(name(), level == 1 ? 5 : ((level - 1) * 10), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, EnchantingXPBoost("Enchanting XP Boost", "§a", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.EnchantingWisdom) + "§7.", Map.of("%b%", level -> String.valueOf(level == 1 ? 5 : ((level - 1) * 10)))), 3) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.EnchantingWisdom, new PlayerStatEvent.BasicModifier(name(), level == 1 ? 5 : ((level - 1) * 10), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, FarmingXPBoost("Farming XP Boost", "§a", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.FarmingFortune) + "§7.", Map.of("%b%", level -> String.valueOf(level == 1 ? 5 : ((level - 1) * 10)))), 3) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.FarmingFortune, new PlayerStatEvent.BasicModifier(name(), level == 1 ? 5 : ((level - 1) * 10), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, FishingXPBoost("Fishing XP Boost", "§a", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.FishingWisdom) + "§7.", Map.of("%b%", level -> String.valueOf(level == 1 ? 5 : ((level - 1) * 10)))), 3) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.FishingWisdom, new PlayerStatEvent.BasicModifier(name(), level == 1 ? 5 : ((level - 1) * 10), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, ForagingXPBoost("Foraging XP Boost", "§a", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.ForagingWisdom) + "§7.", Map.of("%b%", level -> String.valueOf(level == 1 ? 5 : ((level - 1) * 10)))), 3) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.ForagingWisdom, new PlayerStatEvent.BasicModifier(name(), level == 1 ? 5 : ((level - 1) * 10), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, MiningXPBoost("Mining XP Boost", "§a", new Color(0x0000ff), new PotionLore("§7Grants §a+%b% " + (Stat.MiningWisdom) + "§7.", Map.of("%b%", level -> String.valueOf(level == 1 ? 5 : ((level - 1) * 10)))), 3) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.MiningWisdom, new PlayerStatEvent.BasicModifier(name(), level == 1 ? 5 : ((level - 1) * 10), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Archery("Archery", "§b", new Color(0x23FC4D), new PotionLore("§7Increases bow damage by §a%b%%§7.", Map.of("%b%", level -> StringUtils.cleanDouble(level == 1 ? 12.5 : ((level - 1) * 2 * 12.5)))), 4), Blindness("Blindness", "§f", new Color(0x0000ff), new PotionLore("§7Grants blindness to the affected player."), 3) {
        @Override
        public boolean isBuff() {
            return false;
        }

        @Override
        public net.minestom.server.potion.PotionEffect getVanillaEffect() {
            return net.minestom.server.potion.PotionEffect.BLINDNESS;
        }
    }, ColdResistance("Cold Resistance", "§b", new Color(0x7F8392), new PotionLore("§7Gain §b+%b% " + (Stat.ColdResistance) , "%b%", integer -> StringUtils.cleanDouble(2.5 * integer)), 4) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.ColdResistance, new PlayerStatEvent.BasicModifier("Cold Resistance", level * 2.5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Critical("Critical", "§4", new Color(0x271514), new PotionLore("§7Increases §9☣Crit Chance§7 by §a%cc%%§7 and §9☠Crit Damage§7 by §a%cd%%§7.", Map.of("%cc%", integer -> String.valueOf(5 + (5 * integer)), "%cd%", integer -> String.valueOf(10 * integer))), 4) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.CritChance, new PlayerStatEvent.BasicModifier("Critical", 5 + (5 * level), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion), Stat.CritDamage, new PlayerStatEvent.BasicModifier("Critical", 10 * level, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Damage("Damage", "§c", new Color(0x271514), new PotionLore("§7Instantly deals §c%d% ❁Damage", "%d%", integer -> switch (integer) {
        case 1 -> "20";
        case 2 -> "50";
        case 3 -> "100";
        case 4 -> "150";
        default -> String.valueOf((integer - 3) * 100);
    }), 8) {
        @Override
        public void start(SkyblockPlayer player, int level, long duration) {
            super.start(player, level, duration);
            player.damage(0, switch (level) {
                case 1 -> 20;
                case 2 -> 50;
                case 3 -> 100;
                case 4 -> 150;
                default -> (level - 3) * 100;
            });
        }

        @Override
        public boolean isBuff() {
            return false;
        }

        @Override
        public boolean isInstant() {
            return true;
        }
    }, DoucePluieDeStinkyCheese("Douce Pluie de Stinky Cheese", "§e", new Color(16750848), new PotionLore("§7Gain §a+20 " + (Stat.BonusPestChance) + "§7."), 1) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.BonusPestChance, new PlayerStatEvent.BasicModifier(name(), 20, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, HarvestHarbinger("Harvest Harbinger", "§6", new Color(16750848), new PotionLore("§7Gain §a+50 " + (Stat.FarmingFortune) + "§7."), 5) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.FarmingFortune, new PlayerStatEvent.BasicModifier(name(), 50, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Haste("Haste", "§e", new Color(0xD9C043), new PotionLore("§7Gain §a+%b% " + (Stat.MiningSpeed) + "§7.", "%b%", integer -> String.valueOf(50 * integer)), 4) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.MiningSpeed, new PlayerStatEvent.BasicModifier(name(), 50 * level, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }

        @Override
        public void start(SkyblockPlayer player, int level, long duration) {
            super.start(player, level, duration);
            if (!player.getWorldProvider().useCustomMining())
                player.sendPacket(new SetEntityEffectPacket(player.getEntityId(), net.minestom.server.potion.PotionEffect.HASTE.id(), level - 1, (int) duration, (byte) 0));
        }

        @Override
        public void stop(SkyblockPlayer player, int level) {
            super.stop(player, level);
            if (!player.getWorldProvider().useCustomMining())
                player.sendPacket(new RemoveEntityEffectPacket(player.getEntityId(), net.minestom.server.potion.PotionEffect.HASTE));

        }
    }, Healing("Healing", "§c", new Color(16262179), new PotionLore("§7Instantly heals §c%d% " + (Stat.Health) , "%d%", integer -> switch (integer) {
        case 1 -> "20";
        case 2 -> "50";
        case 3 -> "100";
        case 4 -> "150";
        default -> String.valueOf((integer - 3) * 100);
    }), 8) {
        @Override
        public void start(SkyblockPlayer player, int level, long duration) {
            super.start(player, level, duration);
            player.addSbHealth(switch (level) {
                case 1 -> 20;
                case 2 -> 50;
                case 3 -> 100;
                case 4 -> 150;
                default -> (level - 3) * 100;
            });
        }

        @Override
        public boolean isInstant() {
            return true;
        }
    }, Invisibility("Invisibility", "§8", new Color(8356754), new PotionLore("§7Grants invisibility from players and mobs."), 1) {
        @Override
        public net.minestom.server.potion.@Nullable PotionEffect getVanillaEffect() {
            return net.minestom.server.potion.PotionEffect.INVISIBILITY;
        }
    }, JumpBoost("Jump Boost", "§b", new Color(2293580), new PotionLore("§7Increases your jump height to §a%b% blocks§7.", "%b%", integer -> StringUtils.cleanDouble(Math.pow(0.0308354 * integer, 2) + 0.744631 * integer + 1.836131, 1)), 4) {
        @Override
        public net.minestom.server.potion.@Nullable PotionEffect getVanillaEffect() {
            return net.minestom.server.potion.PotionEffect.JUMP_BOOST;
        }
    }, Knockback("Knockback", "§4", new Color(9643043), new PotionLore("§7Damaging enemies deals §a%b%%§7 more knockback.", "%b%", integer -> String.valueOf(20 * integer)), 4),
    MagicFind("Magic Find", "§b", new Color(2293580), new PotionLore("§7Gain §a+%b%%§7 chance to find rare items from monsters and bosses.", "%b%", integer -> integer == 1 ? "10" : String.valueOf(25 * (integer - 1))), 4) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.MagicFind, new PlayerStatEvent.BasicModifier(getName(), level == 1 ? 10 : ((level - 1) * 25), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Mana("Mana", "§9", new Color(2039713), new PotionLore("§7Grants §b+%b% " + (Stat.Intelligence.getSymbol()) + " Mana§7 per second.", "%b%", String::valueOf), 8),
    MushedGlowyTonic("Mushed Glowy Tonic", "§2", new Color(2293580), new PotionLore("§7Grants §b+30 ☂Fishing Speed§7."), 1) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.FishingSpeed, new PlayerStatEvent.BasicModifier(getName(), 30, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, NightVision("Night Vision", "§5", new Color(2039713), new PotionLore("§7Grants greater visibility at night."), 1) {
        @Override
        public net.minestom.server.potion.@Nullable PotionEffect getVanillaEffect() {
            return net.minestom.server.potion.PotionEffect.NIGHT_VISION;
        }
    }, PetLuck("Pet Luck", "§b", new Color(2293580), new PotionLore("§7Gain §a+%b%%§7 chance to find pets and craft higher tier pets.", "%b%", integer -> String.valueOf(integer * 5)), 4) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.PetLuck, new PlayerStatEvent.BasicModifier(getName(), 5 * level, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Rabbit("Rabbit", "§b", new Color(5017135), new PotionLore("§7Grants Jump Boost %jb% and §a+%b% " + (Stat.Speed) + " §7.", Map.of("%jb%", integer -> switch (integer) {
        case 1, 2 -> "I";
        case 3, 4 -> "II";
        default -> "III";
    }, "%b%", integer -> String.valueOf(integer * 10))), 6) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.Speed, new PlayerStatEvent.BasicModifier(getName(), level * 10, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }

        @Override
        public void start(SkyblockPlayer player, int level, long duration) {
            super.start(player, level, duration);
            PotionEffect jumpBoost = player.getPotionEffect(JumpBoost);
            byte jumpBoostLevel = switch (level) {
                case 1, 2 -> 1;
                case 3, 4 -> 2;
                default -> 3;
            };
            if (jumpBoost != null && jumpBoost.amplifier() > jumpBoostLevel) {
                return;
            }
            player.sendPacket(new SetEntityEffectPacket(player, new net.minestom.server.potion.Potion(net.minestom.server.potion.PotionEffect.JUMP_BOOST, jumpBoostLevel, (int) duration)));
        }

        @Override
        public void stop(SkyblockPlayer player, int level) {
            for (Map.Entry<Stat, PlayerStatEvent.PlayerStatModifier> entry : getStatModifiers((byte) level).entrySet()) {
                player.getTemporaryModifiers().removeModifier(entry.getKey(), entry.getValue());
            }
            PotionEffect jumpBoost = player.getPotionEffect(JumpBoost);
            byte jumpBoostLevel = switch (level) {
                case 1, 2 -> 1;
                case 3, 4 -> 2;
                default -> 3;
            };
            if (jumpBoost != null && jumpBoost.amplifier() >= jumpBoostLevel) return;
            player.sendPacket(new RemoveEntityEffectPacket(player.getEntityId(), net.minestom.server.potion.PotionEffect.JUMP_BOOST));
        }
    }, Regeneration("Regeneration", "§4", new Color(13458603), new PotionLore("§7Grants §c+%b% " + (Stat.HealthRegen) + "§7.", "%b%", level -> String.valueOf(level < 7 ? level * 5 : (((level - 3) * 10)))), 9) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.HealthRegen, new PlayerStatEvent.BasicModifier(getName(), level < 7 ? level * 5 : (((level - 3) * 10)), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Resistance("Resistance", "§a", new Color(8889187), new PotionLore("§7Grants §a+%b% " + (Stat.Defense) + "§7.", "%b%", level -> String.valueOf(level < 7 ? level * 5 : (((level - 3) * 10)))), 8) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.Defense, new PlayerStatEvent.BasicModifier(getName(), level < 7 ? level * 5 : (((level - 3) * 10)), PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Speed("Speed", "§9", new Color(8171462), new PotionLore("§7Grants §a+%b% " + (Stat.Speed) + "§7.", "%b%", level -> String.valueOf(level * 5)), 8) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.Speed, new PlayerStatEvent.BasicModifier(getName(), level * 5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Spelunker("Spelunker", "§b", new Color(2039713), new PotionLore("§7Grants §a+%b% " + (Stat.MiningFortune) + "§7.", "%b%", level -> String.valueOf(level * 5)), 5) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.MiningFortune, new PlayerStatEvent.BasicModifier(getName(), level * 5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Spirit("Spirit", "§b", new Color(8171462), new PotionLore("§7Grants §a+%b% " + (Stat.Speed) + "§7 and §a+%b%% " + (Stat.CritDamage) + "§7.", "%b%", level -> String.valueOf(level * 5)), 5) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.Speed, new PlayerStatEvent.BasicModifier(getName(), level * 5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion),
                    Stat.CritDamage, new PlayerStatEvent.BasicModifier(getName(), level * 5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, Strength("Strength", "§4", new Color(9643043), new PotionLore("§7Grants §a+%b% " + (Stat.Strength) + "§7.", "%b%", level -> StringUtils.cleanDouble(switch (level) {
        case 1 -> 5;
        case 2 -> 12.5;
        case 8 -> 75;
        default -> ((level - 1) * 10);
    })), 8) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.Strength, new PlayerStatEvent.BasicModifier(getName(), switch (level) {
                case 1 -> 5;
                case 2 -> 12.5;
                case 8 -> 75;
                default -> ((level - 1) * 10);
            }, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, TrueResistance("True Resistance", "§f", new Color(2039713), new PotionLore("§7Grants §a+%b% " + (Stat.TrueDefense) + "§7.", "%b%", level -> String.valueOf(level * 5)), 4) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.TrueDefense, new PlayerStatEvent.BasicModifier(getName(), level * 5, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    }, WispsIceFlavoredWater("Wisp's Ice-Flavored Water", "§b", new Color(2039713), new PotionLore("§7Grants §4+10 " + (Stat.Vitality) + "§7 and §f+25 " + (Stat.TrueDefense) + "."), 1) {
        @Override
        public Map<Stat, PlayerStatEvent.PlayerStatModifier> getStatModifiers(byte level) {
            return Map.of(Stat.TrueDefense, new PlayerStatEvent.BasicModifier(getName(), 25, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion),
                    Stat.Vitality, new PlayerStatEvent.BasicModifier(getName(), 10, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Potion));
        }
    };
    public static final EventNode<Event> LISTENER = EventNode.all("potions").addListener(PlayerProjectileDamageEntityEvent.class, event -> {
        PotionEffect effect = event.getPlayer().getPotionEffect(Archery);
        if (effect != null)
            event.addAdditiveMultiplier(effect.amplifier() == 1 ? 0.125 : (2 * (effect.amplifier() - 1) * 0.125));
    }).addListener(ManaRegenEvent.class, event -> {
        PotionEffect effect = ((SkyblockPlayer) event.getPlayer()).getPotionEffect(Mana);
        if (effect != null) event.setRegenAmount(event.getRegenAmount() + effect.amplifier());
    });
    private final String name;
    private final String id;
    private final String prefix;
    private final RGBLike color;
    private final PotionLore lore;
    private final byte maxLevel;

    Potion(String name, String prefix, RGBLike color, PotionLore lore, int maxLevel) {
        this.name = name;
        this.id = name.toLowerCase().replace(' ', '_');
        this.color = color;
        this.lore = lore;
        this.maxLevel = (byte) maxLevel;
        this.prefix = prefix;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public RGBLike getPotionColor() {
        return color;
    }

    @Override
    public PotionLore description() {
        return lore;
    }

    @Override
    public boolean isBuff() {
        return true;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public byte getMaxLevel() {
        return maxLevel;
    }
}
