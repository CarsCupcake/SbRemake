package me.carscupcake.sbremake.item.impl.armor.slayer.zombie;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.slayer.Slayers;
import me.carscupcake.sbremake.event.PlayerStatEvent;
import me.carscupcake.sbremake.item.*;
import me.carscupcake.sbremake.item.modifiers.gemstone.Gemstone;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneItem;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlotType;
import me.carscupcake.sbremake.item.modifiers.gemstone.GemstoneSlots;
import me.carscupcake.sbremake.item.requirements.SlayerRequirement;
import me.carscupcake.sbremake.util.CoinsCost;
import me.carscupcake.sbremake.util.Cost;
import me.carscupcake.sbremake.util.ItemCost;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.Material;

import java.util.List;
import java.util.Map;

public class ReaperMask implements ISbItem, ISbItem.StatProvider, GemstoneSlots, HeadWithValue, Listener {
    @Override
    public String getId() {
        return "REAPER_MASK";
    }

    @Override
    public String getName() {
        return "Reaper Mask";
    }

    @Override
    public Material getMaterial() {
        return Material.PLAYER_HEAD;
    }

    @Override
    public ItemType getType() {
        return ItemType.Helmet;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.LEGENDARY;
    }

    @Override
    public Map<Stat, Number> stats() {
        return Map.of();
    }

    @Override
    public GemstoneSlotType[] getGemstoneSlots() {
        return new GemstoneSlotType[]{GemstoneSlotType.Ruby, GemstoneSlotType.Sapphire};
    }

    @Override
    public boolean[] getUnlocked() {
        return new boolean[]{true, false};
    }

    @Override
    public Cost[][] getLockedSlotCost() {
        return new Cost[][]{new Cost[0], {new CoinsCost(50_000), new ItemCost(GemstoneItem.gemstones.get(Gemstone.Type.Sapphire).get(Gemstone.Quality.Fine).asItem(), 20)}};
    }

    @Override
    public String value() {
        return "eyJ0aW1lc3RhbXAiOjE1NjgwMzY3MjYwNjYsInByb2ZpbGVJZCI6IjQxZDNhYmMyZDc0OTQwMGM5MDkwZDU0MzRkMDM4MzFiIiwicHJvZmlsZU5hbWUiOiJNZWdha2xvb24iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzFmYzAxODQ0NzNmZTg4MmQyODk1Y2U3Y2JjODE5N2JkNDBmZjcwYmYxMGQzNzQ1ZGU5N2I2YzJhOWM1ZmM3OGYifX19";
    }

    @Override
    public List<Requirement> requirements() {
        return List.of(new SlayerRequirement(Slayers.Zombie, 7));
    }

    @Override
    public Lore getLore() {
        return new Lore("§6Ability: Evil Incarnate\n§7While wearing:\n§a• §7Doubles your " + (Stat.Mending) + "§7.\n§a• §7Doubles your " + (Stat.Vitality) + "§7.\n§a• §7Zombie Armor triggers on all hits.\n§a• §7Store §b2 §7extra necromancer souls.\n§a• §7Summon§b2§7 more necromancy mobs.");
    }

    @Override
    public EventNode<Event> node() {
        return EventNode.all("item.reaper_mask").addListener(PlayerStatEvent.class, event -> {
            if (event.stat() != Stat.Mending && event.stat() != Stat.Vitality) return;
            SbItemStack helmet = SbItemStack.from(event.getPlayer().getHelmet());
            if (helmet == null) return;
            if (helmet.sbItem() instanceof ReaperMask && hasRequirements(event.player(), helmet.item())) {
                event.modifiers().add(new PlayerStatEvent.BasicModifier("Evil Incarnate", 2, PlayerStatEvent.Type.MultiplicativeMultiplier, PlayerStatEvent.StatsCategory.Ability));
            }
        });
    }
}
