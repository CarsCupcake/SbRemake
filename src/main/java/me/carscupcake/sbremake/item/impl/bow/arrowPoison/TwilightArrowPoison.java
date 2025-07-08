package me.carscupcake.sbremake.item.impl.bow.arrowPoison;

import me.carscupcake.sbremake.event.PlayerProjectileDamageEntityEvent;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.NpcSellable;
import net.minestom.server.item.Material;

public class TwilightArrowPoison implements IArrowPoison, NpcSellable {
    @Override
    public void onHit(PlayerProjectileDamageEntityEvent event) {

    }

    @Override
    public String getId() {
        return "TWILIGHT_ARROW_POISON";
    }

    @Override
    public String getName() {
        return "Twilight Arrow Poison";
    }

    @Override
    public Material getMaterial() {
        return Material.PURPLE_DYE;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.UNCOMMON;
    }

    @Override
    public int sellPrice() {
        return 650;
    }

    private final Lore lore = new Lore("§8Consumed on arrow shot\n§7Arrows apply a §5ᛤ poison §7that increases all incoming damage by §c1.1x §7for " +
                                               "§a20s§7.");

    @Override
    public Lore getLore() {
        return lore;
    }
}
