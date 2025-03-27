package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.event.PlayerDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.util.PlayerDamageEntityListener;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemComponent;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;
import net.minestom.server.tag.Tag;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface SkyblockEnchantment {
    Map<String, SkyblockEnchantment> enchantments = new HashMap<>();

    int getMaxLevel();

    String getName();

    String getId();

    Lore description();

    ItemType[] types();

    default Set<SkyblockEnchantment> conflicts() {
        return new HashSet<>();
    }

    default boolean canApply(SkyblockEnchantment skyblockEnchantment) {
        return !this.conflicts().contains(skyblockEnchantment);
    }

    default ItemStack apply(ItemStack item, int level) {
        CompoundBinaryTag tag = (CompoundBinaryTag) item.getTag(Tag.NBT("ExtraAttributes"));
        CompoundBinaryTag enchantments = tag.getCompound("enchantments", CompoundBinaryTag.from(new HashMap<>()));
        if (level > 0) {
            enchantments = enchantments.putInt(getId(), level);
        } else
            enchantments = enchantments.remove(getId());
        if (enchantments.size() == 1) {
            item = item.with(ItemComponent.ENCHANTMENTS, new EnchantmentList(Enchantment.PROTECTION, 1).withTooltip(false));
        }
        return item.withTag(Tag.NBT("ExtraAttributes"), tag.put("enchantments", enchantments));
    }

    EventNode<Event> LISTENER = EventNode.all("enchantments");

    static void initListener() {
        LISTENER.register(new PlayerDamageEntityListener(event -> {
            SbItemStack item = event.getPlayer().getSbItemInHand(Player.Hand.MAIN);
            if (item == null) return;
            Map<SkyblockEnchantment, Integer> enchantments = item.getEnchantments();
            if (enchantments.containsKey(NormalEnchantment.Sharpness))
                event.addAdditiveMultiplier(EnchantmentUtils.getSharpnessBonus(enchantments.get(NormalEnchantment.Sharpness)));
            if (enchantments.containsKey(NormalEnchantment.Smite) && EnchantmentUtils.SMITE_TYPES.contains(event.getTarget().getEntityType()))
                event.addAdditiveMultiplier(EnchantmentUtils.getSmiteBonus(enchantments.get(NormalEnchantment.Smite)));
            if (enchantments.containsKey(NormalEnchantment.Cleave) && event instanceof PlayerMeleeDamageEntityEvent) {
                Integer smiteLevel = enchantments.get(NormalEnchantment.Cleave);
                event.getPostEvent().add(e -> {
                    double range = 3d + (0.3 * (double) smiteLevel);
                    e.getTarget().getInstance().getNearbyEntities(e.getTarget().getPosition(), range).forEach(entity -> {
                        if (entity instanceof SkyblockEntity sbEntity && sbEntity != e.getTarget()) {
                            PlayerDamageEntityEvent entityEvent = new PlayerDamageEntityEvent(e.getPlayer(), sbEntity, EnchantmentUtils.getCleaveBonus(smiteLevel) * ((e.isCrit()) ? e.calculateCritHit() : e.calculateHit()));
                            event.setCanDoFerocity(false);
                            MinecraftServer.getGlobalEventHandler().call(entityEvent);
                            sbEntity.damage(entityEvent);
                        }
                    });
                });
            }
        }));
    }
}
