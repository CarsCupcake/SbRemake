package me.carscupcake.sbremake.util.gui;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.carscupcake.sbremake.item.ISbItem;
import me.carscupcake.sbremake.item.Lore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import net.minestom.server.component.DataComponent;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.item.component.*;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.registry.RegistryKey;

import java.util.*;
import java.util.function.Supplier;

@Slf4j
@Getter
@SuppressWarnings("unused")
public class ItemBuilder {
    private final Material material;
    private String name = "N/A";
    private List<Component> lore = new ArrayList<>();
    private int count = 1;
    private boolean isHead;
    private String headTexture;
    private RGBLike leatherColor;
    private ArrayList<BannerPatterns.Layer> bannerPatterns;
    private final Map<RegistryKey<Enchantment>, Integer> enchants = new HashMap<>();
    private boolean glint = false;
    private boolean hideTooltip = false;

    public ItemBuilder(ItemStack itemStack) {
        this.material = itemStack.material();
        this.name = ((TextComponent) Objects.requireNonNull(itemStack.get(DataComponents.CUSTOM_NAME))).content();
        lore.addAll(Objects.requireNonNull(itemStack.get(DataComponents.LORE)));
        count = itemStack.amount();
        isHead = material == Material.PLAYER_HEAD && itemStack.get(DataComponents.PROFILE) != null;
        if (isHead)
            headTexture = Objects.requireNonNull(Objects.requireNonNull(itemStack.get(DataComponents.PROFILE)).skin()).textures();
        leatherColor = itemStack.get(DataComponents.DYED_COLOR);
        BannerPatterns patterns = itemStack.get(DataComponents.BANNER_PATTERNS);
        if (patterns != null) {
            bannerPatterns = new ArrayList<>();
            bannerPatterns.addAll(patterns.layers());
        }
        EnchantmentList list = itemStack.get(DataComponents.ENCHANTMENTS);
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

    public ItemBuilder setLore(Lore lore) {
        this.lore = lore.build(null, null).stream().map(s -> (Component) Component.text(s)).toList();
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

    public ItemBuilder addLoreIf(Supplier<Boolean> predicate, String... lore) {
        if (!predicate.get()) return this;
        for (String l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addLoreIf(Supplier<Boolean> predicate, List<String> lore) {
        if (!predicate.get()) return this;
        for (String l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addLoreIf(Supplier<Boolean> predicate, TextComponent... lore) {
        if (!predicate.get()) return this;
        for (TextComponent l : lore)
            addLoreRow(l);
        return this;
    }

    public ItemBuilder addLoreIf(Supplier<Boolean> predicate, Supplier<String> l) {
        if (!predicate.get()) return this;
        addLore(l.get());
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

    private static final Set<DataComponent<?>> HIDDEN_COMPONENTS = new HashSet<>(DataComponent.values());

    public ItemStack build() {
        ItemStack.Builder item = ItemStack.builder(material);
        if (!name.equals("N/A"))
            item.set(DataComponents.CUSTOM_NAME, Component.text(name));
        item.set(DataComponents.LORE, lore);
        item.set(DataComponents.ATTRIBUTE_MODIFIERS, AttributeList.EMPTY);
        EnchantmentList enchantmentList = new EnchantmentList(enchants);
        if (glint && enchantmentList.enchantments().isEmpty())
            enchantmentList = enchantmentList.with(Enchantment.PROTECTION, 1);
        item.set(DataComponents.ENCHANTMENTS, enchantmentList);
        if (leatherColor != null) {
            item.set(DataComponents.DYED_COLOR, leatherColor);
        }
        if (bannerPatterns != null) {
            item.set(DataComponents.BANNER_PATTERNS, new BannerPatterns(bannerPatterns));
        }
        if (isHead) {
            item.set(DataComponents.PROFILE, new HeadProfile(new PlayerSkin(headTexture, "")));
        }
        item.set(DataComponents.TOOLTIP_DISPLAY, new TooltipDisplay(hideTooltip, ISbItem.HIDDEN_COMPONENTS));
        item.amount(count);
        return item.build();
    }

    public ItemBuilder setHideTooltip(boolean hideTooltip) {
        this.hideTooltip = hideTooltip;
        return this;
    }

    public ItemBuilder setHeadTexture(String value) {
        isHead = true;
        headTexture = value;
        return this;
    }

    public ItemBuilder setLeatherColor(RGBLike color) {
        this.leatherColor = color;
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
        enchants.put(RegistryKey.unsafeOf(Objects.requireNonNull(Objects.requireNonNull(enchantment.exclusiveSet()).key()).key()), level);
        return this;
    }
}
