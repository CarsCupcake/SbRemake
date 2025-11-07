package me.carscupcake.sbremake.item.impl.shard;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.config.KeyClass;
import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.event.ManaRegenEvent;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerProjectileDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.event.eventBinding.PlayerDamageEntityBinding;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.player.SkyblockPlayer;
import me.carscupcake.sbremake.util.ArrayUtil;
import me.carscupcake.sbremake.util.MapList;
import me.carscupcake.sbremake.util.Pair;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IAttributeShard extends KeyClass {
    static @Nullable IAttributeShard fromKey(String id) {
        for (var shard : Shard.values())
            if (shard.getId().equals(id))
                return shard;
        return null;
    }

    @Override
    default String key() {
        return getId();
    }

    String getDisplayName();

    ItemRarity getRarity();

    String getShardId();

    String getAbilityName();

    String getId();

    ShardCategory getCategory();

    ShardFamily[] getFamilies();

    Lore getLore();

    Material getMaterial();

    @Nullable String getHeadValue();

    default int compareTo(@NotNull IAttributeShard shard) {
        var rarity = getRarity().compareTo(shard.getRarity());
        if (rarity != 0) return rarity;
        var myId = Integer.parseInt(getShardId().substring(1));
        var compareId = Integer.parseInt(shard.getShardId().substring(1));
        return Integer.compare(myId, compareId);
    }

    MapList<Stat, Pair<IAttributeShard, Number>> VAlUE_STAT_SHARDS = new MapList.Builder<Stat, Pair<IAttributeShard, Number>>()
            .add(Stat.Health, new Pair<>(Shard.NatureElemental, 2))
            .add(Stat.Intelligence, new Pair<>(Shard.FogElemental, 1))
            .add(Stat.Strength, new Pair<>(Shard.LightElemental, 1))
            .add(Stat.SeaCreatureChance, new Pair<>(Shard.CreatureFisher, 0.5))
            .add(Stat.HeatResistance, new Pair<>(Shard.YogMembrane, 1))
            .add(Stat.MiningSpeed, new Pair<>(Shard.RottenPickaxe, 3))
            .add(Stat.BlockFortune, new Pair<>(Shard.MossyBox, 5))
            .add(Stat.FigFortune, new Pair<>(Shard.MoongladeMastery, 10))
            .add(Stat.MangroveFortune, new Pair<>(Shard.MoongladeMastery, 10))
            .add(Stat.Health, new Pair<>(Shard.WoodElemental, 2))
            .add(Stat.Intelligence, new Pair<>(Shard.WaterElemental, 1))
            .add(Stat.Strength, new Pair<>(Shard.StoneElemental, 1))
            .add(Stat.Speed, new Pair<>(Shard.Speed, 1))
            .add(Stat.Health, new Pair<>(Shard.ForestElemental, 2))
            .add(Stat.Intelligence, new Pair<>(Shard.TorrentElemental, 1))
            .add(Stat.Strength, new Pair<>(Shard.LightningElemental, 1))
            .add(Stat.ForagingFortune, new Pair<>(Shard.AnimalExpertise, 10))
            .add(Stat.PressureResistance, new Pair<>(Shard.ExtremePressure, 5))
            .add(Stat.Respiration, new Pair<>(Shard.DeepDiving, 5))
            .add(Stat.Health, new Pair<>(Shard.EarthElemental, 2))
            .add(Stat.Intelligence, new Pair<>(Shard.FrostElemental, 1))
            .add(Stat.Strength, new Pair<>(Shard.WindElemental, 1))
            .add(Stat.ForagingWisdom, new Pair<>(Shard.ForagingWisdom, 5))
            .add(Stat.TrophyFishChance, new Pair<>(Shard.TrophyHunter, 2))
            .add(Stat.FishingSpeed, new Pair<>(Shard.FishingSpeed, 3))
            .add(Stat.ForagingWisdom, new Pair<>(Shard.SeaWisdom, 0.5))
            .add(Stat.TrueDefense, new Pair<>(Shard.Veil, 1))
            .add(Stat.MiningWisdom, new Pair<>(Shard.CavernWisdom, 0.5))
            .add(Stat.FarmingWisdom, new Pair<>(Shard.GardenWisdom, 0.5))
            .add(Stat.HuntingWisdom, new Pair<>(Shard.HuntWisdom, 0.5))
            .add(Stat.Health, new Pair<>(Shard.ShadowElemental, 2))
            .add(Stat.Intelligence, new Pair<>(Shard.SnowElemental, 1))
            .add(Stat.Strength, new Pair<>(Shard.StormElemental, 1))
            .add(Stat.EnchantingWisdom, new Pair<>(Shard.Charmed, 0.5))
            .add(Stat.Vitality, new Pair<>(Shard.Vitality, 2))
            .add(Stat.HealthRegen, new Pair<>(Shard.HelpFromAbove, 1.25))
            .add(Stat.AttackSpeed, new Pair<>(Shard.AttackSpeed, 1))
            .add(Stat.MagicFind, new Pair<>(Shard.MagicFind, 0.5))
            .add(Stat.HunterFortune, new Pair<>(Shard.HuntersKarma, 1))
            .add(Stat.TamingWisdom, new Pair<>(Shard.PetWisdom, 0.5))
            .add(Stat.CombatWisdom, new Pair<>(Shard.Veteran, 1))
            .add(Stat.MiningFortune, new Pair<>(Shard.UltimateDNA, 1))
            .add(Stat.FarmingFortune, new Pair<>(Shard.UltimateDNA, 1))
            .add(Stat.ForagingFortune, new Pair<>(Shard.UltimateDNA, 1))
            .add(Stat.PetLuck, new Pair<>(Shard.RareBird, 1))
            .build();

    MapList<Stat, Pair<IAttributeShard, Number>> MULT_STAT_SHARDS = new MapList.Builder<Stat, Pair<IAttributeShard, Number>>()
            .add(Stat.Intelligence, new Pair<>(Shard.MaximalTorment, 0.001))
            .add(Stat.CritDamage, new Pair<>(Shard.UnlimitedEnergy, 0.001))
            .add(Stat.Strength, new Pair<>(Shard.UnlimitedPower, 0.001))
            .add(Stat.Defense, new Pair<>(Shard.ParamountFortitude, 0.002))
            .build();

    MapList<MobType, Pair<IAttributeShard, Number>> MOB_TYPE_DAMAGE_SHARDS = new MapList.Builder<MobType, Pair<IAttributeShard, Number>>()
            .add(MobType.Skeletal, new Pair<>(Shard.SkeletalRuler, 0.03))
            .add(MobType.Undead, new Pair<>(Shard.UndeadRuler, 0.03))
            .add(MobType.Arthropod, new Pair<>(Shard.ArthropodRuler, 0.03))
            .add(MobType.Magmatic, new Pair<>(Shard.MagmaticRuler, 0.03))
            .add(MobType.Ender, new Pair<>(Shard.EnderRuler, 0.03))
            .add(MobType.Humanoid, new Pair<>(Shard.HumanoidRuler, 0.03))
            .add(MobType.Infernal, new Pair<>(Shard.Blazing, 0.03))
            .build();


    EventNode<@NotNull Event> LISTENER = EventNode.all("shards")
            .addListener(PlayerStatEvent.class, event -> {
                var shards = VAlUE_STAT_SHARDS.get(event.stat());
                if (shards != null && !shards.isEmpty()) {
                    var elementalEcho = event.player().getAttributesShards().get(Shard.EchoOfElemental);
                    var boost = (elementalEcho != null) ? elementalEcho.level() * 0.02 : 0;
                    if (boost != 0) {
                        var echoOfEchos = event.player().getAttributesShards().get(Shard.EchoOfEchoes);
                        boost += boost * (echoOfEchos == null ? 0 : echoOfEchos.level() * 0.05);
                    }
                    for (var entry : shards) {
                        var playerShard = event.player().getAttributesShards().get(entry.getFirst());
                        if (playerShard == null || playerShard.level() == 0) continue;
                        var amount = entry.getSecond().doubleValue() * playerShard.level();
                        if (ArrayUtil.contains(entry.getFirst().getFamilies(), ShardFamily.Elemental)) {
                            amount += amount * boost;
                        }
                        event.modifiers().add(new PlayerStatEvent.BasicModifier(playerShard.attributeShard().getAbilityName(), amount, PlayerStatEvent.Type.Value, PlayerStatEvent.StatsCategory.Attribute));
                    }
                }
                shards = MULT_STAT_SHARDS.get(event.stat());
                if (shards != null && !shards.isEmpty()) {
                    for (var entry : shards) {
                        var playerShard = event.player().getAttributesShards().get(entry.getFirst());
                        if (playerShard == null || playerShard.level() == 0) continue;
                        var amount = entry.getSecond().doubleValue() * playerShard.level();
                        event.modifiers().add(new PlayerStatEvent.BasicModifier(playerShard.attributeShard().getAbilityName(), amount, PlayerStatEvent.Type.AddativeMultiplier, PlayerStatEvent.StatsCategory.Attribute));
                    }
                }

            }).addListener(PlayerMeleeDamageEntityEvent.class, event -> {
                var warrior = event.getPlayer().getAttributesShards().get(Shard.Warrior);
                if (warrior != null && warrior.level() != 0) {
                    event.addAdditiveMultiplier(warrior.level() * 0.025);
                }
            })
            .addListener(PlayerProjectileDamageEntityEvent.class, event -> {
                var deadeye = event.getPlayer().getAttributesShards().get(Shard.Deadeye);
                if (deadeye != null && deadeye.level() != 0) {
                    event.addAdditiveMultiplier(deadeye.level() * 0.025);
                }
            })
            .addListener(ManaRegenEvent.class, event -> {
                var manaRegeneration = ((SkyblockPlayer) event.getPlayer()).getAttributesShards().get(Shard.ManaRegeneration);
                if (manaRegeneration != null && manaRegeneration.level() != 0) {
                    event.setMultiplier(event.getMultiplier() + 0.01 * manaRegeneration.level());
                }
            })
            .register(new PlayerDamageEntityBinding(event -> {
                var rulerEcho = event.getPlayer().getAttributesShards().get(Shard.EchoOfRuler);
                double mult = rulerEcho != null ? (rulerEcho.level() * 0.02) : 0;
                if (mult != 0) {
                    var echoOfEchos = event.getPlayer().getAttributesShards().get(Shard.EchoOfEchoes);
                    mult += mult * (echoOfEchos == null ? 0 : echoOfEchos.level() * 0.05);
                }
                for (var mobtype : event.getTarget().getMobTypes()) {
                    var shards = MOB_TYPE_DAMAGE_SHARDS.get(mobtype);
                    if (shards == null || shards.isEmpty()) continue;
                    for (var entry : shards) {var playerShard = event.getPlayer().getAttributesShards().get(entry.getFirst());
                        if (playerShard == null || playerShard.level() == 0) continue;
                        var amount = entry.getSecond().doubleValue() * playerShard.level();
                        event.addAdditiveMultiplier(amount * (1+mult));
                    }
                }
            }));
}
