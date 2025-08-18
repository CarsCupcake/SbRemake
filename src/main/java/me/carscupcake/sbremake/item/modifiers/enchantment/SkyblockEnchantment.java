package me.carscupcake.sbremake.item.modifiers.enchantment;

import me.carscupcake.sbremake.Stat;
import me.carscupcake.sbremake.entity.MobType;
import me.carscupcake.sbremake.entity.SkyblockEntity;
import me.carscupcake.sbremake.event.GetItemStatEvent;
import me.carscupcake.sbremake.event.PlayerDamageEntityEvent;
import me.carscupcake.sbremake.event.PlayerMeleeDamageEntityEvent;
import me.carscupcake.sbremake.item.ItemType;
import me.carscupcake.sbremake.item.Lore;
import me.carscupcake.sbremake.item.SbItemStack;
import me.carscupcake.sbremake.util.ArrayUtil;
import me.carscupcake.sbremake.util.EnchantmentUtils;
import me.carscupcake.sbremake.event.eventBinding.PlayerDamageEntityBinding;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventNode;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.component.CustomData;
import net.minestom.server.item.component.EnchantmentList;
import net.minestom.server.item.enchant.Enchantment;

import java.util.*;

public interface SkyblockEnchantment {
    Map<String, SkyblockEnchantment> enchantments = new HashMap<>();

    int getMaxLevel();

    String getName();

    String getId();

    Lore description();

    ItemType[] types();

    default Set<? extends SkyblockEnchantment> conflicts() {
        return new HashSet<>();
    }

    default boolean canApply(SkyblockEnchantment skyblockEnchantment) {
        return !this.conflicts().contains(skyblockEnchantment);
    }

    default ItemStack apply(ItemStack item, int level) {
        CompoundBinaryTag tag = Objects.requireNonNull(item.get(DataComponents.CUSTOM_DATA)).nbt();
        CompoundBinaryTag enchantments = tag.getCompound("enchantments", CompoundBinaryTag.from(new HashMap<>()));
        if (level > 0) {
            enchantments = enchantments.putInt(getId(), level);
        } else
            enchantments = enchantments.remove(getId());
        if (enchantments.size() == 1) {
            item = item.with(DataComponents.ENCHANTMENTS, new EnchantmentList(Enchantment.PROTECTION, 1));
        }
        return item.with(DataComponents.CUSTOM_DATA, new CustomData(tag.put("enchantments", enchantments)));
    }

    EventNode<Event> LISTENER = EventNode.all("enchantments")
            .addListener(GetItemStatEvent.class, event -> {
                if (event.getStat() == Stat.MagicFind) {
                    var divineGift = event.getItemStack().getEnchantmentLevel(NormalEnchantments.DivineGift);
                    if (divineGift > 0) event.addValue(EnchantmentUtils.getDivineGiftBonus(divineGift));
                }
                if (event.getStat() == Stat.CritDamage) {
                    var critical = event.getItemStack().getEnchantmentLevel(NormalEnchantments.Critical);
                    if (critical > 0) event.addValue(EnchantmentUtils.getCriticalBonus(critical));
                }
            });

    static void initListener() {
        LISTENER.register(new PlayerDamageEntityBinding(event -> {
            SbItemStack item = event.getPlayer().getSbItemInHand(PlayerHand.MAIN);
            if (item == null) return;
            Map<SkyblockEnchantment, Integer> enchantments = item.getEnchantments();

            if (enchantments.containsKey(NormalEnchantments.Sharpness))
                event.addAdditiveMultiplier(EnchantmentUtils.getSharpnessBonus(enchantments.get(NormalEnchantments.Sharpness)));

            if (enchantments.containsKey(NormalEnchantments.Smite) && ArrayUtil.contains(event.getTarget().getMobTypes(), MobType.Undead))
                event.addAdditiveMultiplier(EnchantmentUtils.getSmiteBonus(enchantments.get(NormalEnchantments.Smite)));

            if (enchantments.containsKey(NormalEnchantments.BaneOfArthropods) && ArrayUtil.contains(event.getTarget().getMobTypes(), MobType.Arthropod))
                event.addAdditiveMultiplier(EnchantmentUtils.getBaneOfArthropodsBonus(enchantments.get(NormalEnchantments.BaneOfArthropods)));

            if (enchantments.containsKey(NormalEnchantments.EnderSlayer) && ArrayUtil.contains(event.getTarget().getMobTypes(), MobType.Ender))
                event.addAdditiveMultiplier(EnchantmentUtils.getEnderSlayerBonus(enchantments.get(NormalEnchantments.EnderSlayer)));

            if (enchantments.containsKey(NormalEnchantments.Cubism) && ArrayUtil.contains(event.getTarget().getMobTypes(), MobType.Cubic))
                event.addAdditiveMultiplier(EnchantmentUtils.getEnderSlayerBonus(enchantments.get(NormalEnchantments.Cubism)));

            if (enchantments.containsKey(NormalEnchantments.Gravity) && ArrayUtil.contains(event.getTarget().getMobTypes(), MobType.Airborne))
                event.addAdditiveMultiplier(EnchantmentUtils.getDragonHunterBonus(enchantments.get(NormalEnchantments.Gravity)));

            if (enchantments.containsKey(NormalEnchantments.Execute)) {
                double missingHealth =  1 - event.getTarget().getHealth() / event.getTarget().getMaxHealth();
                event.addAdditiveMultiplier(100 * missingHealth * EnchantmentUtils.getExecuteBonus(enchantments.get(NormalEnchantments.Execute)));
            }

            if (enchantments.containsKey(NormalEnchantments.Prosecute)) {
                double missingHealth =  event.getTarget().getHealth() / event.getTarget().getMaxHealth();
                event.addAdditiveMultiplier(100 * missingHealth * EnchantmentUtils.getProsecuteBonus(enchantments.get(NormalEnchantments.Prosecute)));
            }

            if (enchantments.containsKey(NormalEnchantments.Cleave) && event instanceof PlayerMeleeDamageEntityEvent) {
                Integer smiteLevel = enchantments.get(NormalEnchantments.Cleave);
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
