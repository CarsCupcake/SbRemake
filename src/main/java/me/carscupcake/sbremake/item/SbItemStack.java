package me.carscupcake.sbremake.item;

import net.kyori.adventure.text.Component;
import net.minestom.server.color.DyeColor;
import net.minestom.server.item.ItemMeta;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.tag.Tag;
import org.jetbrains.annotations.NotNull;
import org.jglrxavpok.hephaistos.nbt.NBT;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A Wrapper for an ItemStack to use some Sb featurs
 * @param item the item that gets wrapped
 * @param sbItem the Sb item
 */
public record SbItemStack(@NotNull ItemStack item, @NotNull ISbItem sbItem) {

    /**
     * Use Audiences
     */
    @Deprecated(forRemoval = true, since = "beginning")
    private static final ConcurrentHashMap<String, ISbItem> items = new ConcurrentHashMap<>();

    public static SbItemStack from(ItemStack stack) {
        NBTCompound compound = stack.meta().toNBT().getCompound("ExtraAttributes");
        if (compound == null || !compound.contains("id")) return base(stack.material());
        return new SbItemStack(stack, items.get(compound.getString("id")));
    }

    public static SbItemStack base(Material material) {
        return new SbItemStack(ItemStack.builder(material)
                .set(Tag.NBT("ExtraAttributes"), NBT.Compound(mutableNBTCompound -> mutableNBTCompound.put("id", NBT.String(material.namespace().value()))))
                .build(), items.get(material.namespace().value()));
    }
    public SbItemStack update() {
        if (!sbItem.allowUpdates()) return this;
        return new SbItemStack(item.with(builder -> builder.lore(buildLore().stream().map(Component::text).toList()).displayName(Component.text(getRarity().getPrefix() + displayName()))), sbItem);
    }

    public String displayName() {
        //TODO Implement Modifiers
        return sbItem.getName();
    }
    /*
    Lore Placement:

    Top Row Lore
    Stats
    Gemstones
    --- (row if trl/stats/gemstones are present)
    Above Ability Lore
    --- (if aal is present)
    Ability Lore(s)
    --- (if ability is present)
    Below Ability Lore
    --- (if bal is present)
    Other info (shortbow etc)
    --- (if other info is present)
    Reforgable (only shows if no reforge is applied)
    Soulbound or not
    Rarity - Type String
     */
    public List<String> buildLore() {
        boolean space = false;
        List<String> lore = new ArrayList<>();
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.Top) {
            space = true;
            lore.addAll(sbItem.getLore().build(this));

        }
        //Todo Stats
        //Todo Gemstones
        if (space) {
            space = false;
            lore.add(" ");
        }
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.AboveAbility && sbItem.getLore() != Lore.EMPTY) {
            lore.addAll(sbItem.getLore().build(this));
            lore.add("  ");
        }
        //Todo Ability Lores
        if (sbItem.getLorePlacement() == ISbItem.LorePlace.BelowAbility && sbItem.getLore() != Lore.EMPTY) {
            lore.addAll(sbItem.getLore().build(this));
            lore.add("  ");
        }
        if (sbItem.getType().isReforgable()) /* Todo add reforge check*/ {
            lore.add("§8This item can be reforged!");
        }
        ItemRarity rarity = getRarity();
        lore.add(rarity.getPrefix() + rarity.getDisplay().toUpperCase() + " " + sbItem.getType().getDisplay().toUpperCase());
        return lore;
    }
    public ItemRarity getRarity() {
        //TODO add custom rarity getter
        return sbItem.getRarity();
    }
}
