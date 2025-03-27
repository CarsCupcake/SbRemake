package me.carscupcake.sbremake.item.impl.admin;

import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.ItemRarity;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.ability.Ability;
import me.carscupcake.sbremake.item.ability.AbilityType;
import me.carscupcake.sbremake.item.ability.ItemAbility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.Material;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LocationsStick implements ISbItem {
    @Override
    public String getId() {
        return "LOCATION_STICK";
    }

    @Override
    public String getName() {
        return "Location Stick";
    }

    @Override
    public Material getMaterial() {
        return Material.BLAZE_ROD;
    }

    @Override
    public ItemType getType() {
        return ItemType.None;
    }

    @Override
    public ItemRarity getRarity() {
        return ItemRarity.ADMIN;
    }

    private static final List<Pos> positions = new ArrayList<>();

    @Override
    public List<Ability> getDefaultAbilities() {
        return List.of(new ItemAbility<>("Mark Location", AbilityType.RIGHT_CLICK, event -> {
                    if (event.block() == null) return;
                    positions.add(Pos.fromPoint(event.block()));
                    event.player().sendMessage("§aAdded Block " + (event.block().x()) + " " + (event.block().y()) + " " + (event.block().z()) );
                    StringBuilder builder = new StringBuilder("new Pos[]{");
                    Iterator<Pos> iterator = positions.iterator();
                    while (iterator.hasNext()) {
                        Pos pos = iterator.next();
                        builder.append("new Pos(").append(pos.x()).append(", ").append(pos.y()).append(", ").append(pos.z()).append(")").append((iterator.hasNext()) ? "," : "");
                    }
                    event.player().sendMessage(Component.text("§e§lCOPY").clickEvent(ClickEvent.clickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, builder.toString())));
                }, new Lore("Mark the block")),
                new ItemAbility<>("Reset Locations", AbilityType.LEFT_CLICK, event -> {
                    positions.clear();
                    event.player().sendMessage("§aCleared!");
                }, new Lore("Reset the marked blocks")));
    }
}
