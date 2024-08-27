package me.carscupcake.sbremake.item.modifiers.gemstone;

import lombok.Getter;
import me.carscupcake.sbremake.Stat;
import net.minestom.server.item.Material;

@Getter
public enum GemstoneSlotType {
    Mining("✦", Gemstone.Type.Jade, Gemstone.Type.Amber, Gemstone.Type.Topaz),
    Offensive("☠", Gemstone.Type.Sapphire, Gemstone.Type.Jasper),
    Combat("⚔", Gemstone.Type.Ruby, Gemstone.Type.Amethyst, Gemstone.Type.Sapphire, Gemstone.Type.Jasper, Gemstone.Type.Onyx, Gemstone.Type.Opal),
    Defensive("☤", Gemstone.Type.Amethyst, Gemstone.Type.Ruby, Gemstone.Type.Opal),
    Universal("❂", Gemstone.Type.values()),
    Ruby("❤", Gemstone.Type.Ruby),
    Amber("⸕", Gemstone.Type.Amber),
    Topaz("✧", Gemstone.Type.Topaz),
    Jade("☘", Gemstone.Type.Jade),
    Sapphire("✎", Gemstone.Type.Sapphire),
    Amethyst("❈", Gemstone.Type.Amethyst),
    Jasper("❁", Gemstone.Type.Jasper),
    Opal("❂", Gemstone.Type.Opal),
    Citrine("☘", Gemstone.Type.Citrine),
    Aquamarine(Stat.SeaCreatureChance.getSymbol(), Gemstone.Type.Aquamarine),
    Peridot("☘", Gemstone.Type.Peridot),
    Onyx(Stat.CritChance.getSymbol(), Gemstone.Type.Onyx),
    Chisel("", Gemstone.Type.Citrine, Gemstone.Type.Aquamarine, Gemstone.Type.Peridot, Gemstone.Type.Onyx);
    private final Gemstone.Type[] types;
    private final String symbol;

    GemstoneSlotType(String symbol, Gemstone.Type... types) {
        this.types = types;
        this.symbol = symbol;
    }

    public Material getGlassPane() {
        return switch (this) {
            case Amber, Topaz -> (Material.YELLOW_STAINED_GLASS_PANE);
            case Amethyst, Mining -> (Material.PURPLE_STAINED_GLASS_PANE);
            case Combat, Ruby, Citrine -> (Material.RED_STAINED_GLASS_PANE);
            case Defensive, Jade -> (Material.LIME_STAINED_GLASS_PANE);
            case Jasper -> (Material.PINK_STAINED_GLASS_PANE);
            case Offensive, Aquamarine, Onyx -> (Material.BLUE_STAINED_GLASS_PANE);
            case Opal, Universal, Chisel -> (Material.WHITE_STAINED_GLASS_PANE);
            case Sapphire -> (Material.LIGHT_BLUE_STAINED_GLASS_PANE);
            case Peridot -> Material.GREEN_STAINED_GLASS_PANE;
        };
    }

    public String getPrefix() {
        return switch (this) {
            case Amber -> "§6";
            case Amethyst, Mining -> "§5";
            case Combat -> "§4";
            case Defensive, Jade -> "§a";
            case Jasper -> "§d";
            case Offensive -> "§9";
            case Ruby -> "§c";
            case Sapphire -> "§b";
            case Topaz -> "§e";
            default -> "§f";
        };
    }
}
