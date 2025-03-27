package me.carscupcake.sbremake.util.item;

import lombok.extern.slf4j.Slf4j;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.util.Returnable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.*;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.DynamicRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@SuppressWarnings("unused")
public class ItemBuilder {
    private final Material material;
    private String name = "N/A";
    private final ArrayList<Component> lore = new ArrayList<>();
    private int count = 1;
    private boolean isHead;
    private String headTexture;
    private DyedItemColor leatherColor;
    private ArrayList<BannerPatterns.Layer> bannerPatterns;
    private final HashMap<DynamicRegistry.Key<Enchantment>, Integer> enchants = new HashMap<>();
    private boolean glint = false;

    public ItemBuilder(ItemStack itemStack) {
        this.material = itemStack.material();
        this.name = ((TextComponent) Objects.requireNonNull(itemStack.get(ItemComponent.CUSTOM_NAME))).content();
        lore.addAll(itemStack.get(ItemComponent.LORE));
        count = itemStack.amount();
        isHead = material == Material.PLAYER_HEAD && itemStack.get(ItemComponent.PROFILE) != null;
        if (isHead)
            headTexture = Objects.requireNonNull(Objects.requireNonNull(itemStack.get(ItemComponent.PROFILE)).skin()).textures();
        leatherColor = itemStack.get(ItemComponent.DYED_COLOR);
        BannerPatterns patterns = itemStack.get(ItemComponent.BANNER_PATTERNS);
        if (patterns != null) {
            bannerPatterns = new ArrayList<>();
            bannerPatterns.addAll(patterns.layers());
        }
        EnchantmentList list = itemStack.get(ItemComponent.ENCHANTMENTS);
        if (list != null) {
            enchants.putAll(list.enchantments());
        }
    }

    public ItemBuilder(Material material) {
        this.material = material;
    }

    public ItemBuilder setName(String str) {
        this.name = str;
        return this;
    }

    public ItemBuilder clearLore() {
        lore.clear();
        return this;
    }

    public ItemBuilder addLoreRow(String str) {
        return setLoreRow(lore.size(), str);
    }

    public ItemBuilder setLoreRow(int i, String str) {
        if (i == lore.size())
            lore.add(Component.text(str));
        else
            lore.set(i, Component.text(str));

        return this;
    }

    public ItemBuilder addAllLore(List<String> lore) {
        for (String l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addLoreIf(Returnable<Boolean> predicate, String... lore) {
        if (!predicate.get()) return this;
        for (String l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addLoreIf(Returnable<Boolean> predicate, List<String> lore) {
        if (!predicate.get()) return this;
        for (String l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addLoreIf(Returnable<Boolean> predicate, TextComponent... lore) {
        if (!predicate.get()) return this;
        for (TextComponent l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addLoreRow(TextComponent component) {
        lore.add(component);
        return this;
    }

    public ItemBuilder addAllLore(String... lore) {
        for (String l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addAllLore(TextColor base, String... lore) {
        for (String l : lore)
            addLoreRow(Component.text(l, base));
        return this;
    }

    public ItemBuilder addLore(String s) {
        addAllLore(Lore.refactorLore(s));
        return this;
    }

    public ItemBuilder setAmount(int i) {
        count = i;
        return this;
    }

    public ItemStack build() {
        ItemStack.Builder item = ItemStack.builder(material);
        if (!name.equals("N/A"))
            item.set(ItemComponent.CUSTOM_NAME, Component.text(name));
        item.set(ItemComponent.LORE, lore);
        item.set(ItemComponent.ATTRIBUTE_MODIFIERS, AttributeList.EMPTY.withTooltip(false));
        EnchantmentList enchantmentList = new EnchantmentList(enchants, false);
        if (glint && enchantmentList.enchantments().isEmpty())
            enchantmentList = enchantmentList.with(Enchantment.PROTECTION, 1).withTooltip(false);
        item.set(ItemComponent.ENCHANTMENTS, enchantmentList);
        if (leatherColor != null) {
            item.set(ItemComponent.DYED_COLOR, leatherColor.withTooltip(false));
        }
        if (bannerPatterns != null) {
            item.set(ItemComponent.BANNER_PATTERNS, new BannerPatterns(bannerPatterns));
        }
        if (isHead) {
            item.set(ItemComponent.PROFILE, new HeadProfile(new PlayerSkin(headTexture, "")));
        }
        item.amount(count);
        return item.build();
    }

    public ItemBuilder setHeadTexture(String value) {
        isHead = true;
        headTexture = value;
        return this;
    }

    public ItemBuilder setLeatherColor(DyedItemColor color) {
        this.leatherColor = color;
        return this;
    }

    public ItemBuilder setLeatherColor(RGBLike color) {
        this.leatherColor = new DyedItemColor(color, false);
        return this;
    }

    public ItemBuilder setBannerPatterns(ArrayList<BannerPatterns.Layer> patterns) {
        bannerPatterns = patterns;
        return this;
    }

    public ItemBuilder setGlint(boolean b) {
        glint = b;
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        enchants.put(DynamicRegistry.Key.of(Objects.requireNonNull(enchantment.registry()).namespace()), level);
        return this;
    }
}
